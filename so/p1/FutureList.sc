import scala.concurrent.Future

def find(loginInfo: LoginInfo): Future[Option[models.admin.User]] = {
  val userQuery = for {
    dbLoginInfo <- loginInfoQuery(loginInfo)
    dbUserLoginInfo <- Userlogininfo.filter(_.logininfoid === dbLoginInfo.id)
    dbUser <- User.filter(_.userid === dbUserLoginInfo.userid)
  } yield dbUser

  db.run(userQuery.result.headOption).flatMap { dbUserOption =>
    dbUserOption.map { user =>
      val permissionQuery = for {
        dbUserPermission <- Userpermission.filter(_.userid === user.userid)
        dbPermission <- Permission.filter(_.id === dbUserPermission.permissionid)
      } yield dbPermission

      val rolePermissionQuery = for {
        dbUserRole <- Userrole.filter(_.userid === user.userid)
        dbRole <- Role.filter(_.id === dbUserRole.roleid)
        dbRolePermission <- Rolepermission.filter(_.roleid === dbRole.id)
        dbPermission <- Permission.filter(_.id === dbRolePermission.permissionid)
      } yield dbPermission

      val unionPermissionQuery = permissionQuery union rolePermissionQuery

      db.run(unionPermissionQuery.result).map(_.map(_.name).toList).map(permissions =>
        models.admin.User(
          UUID.fromString(user.userid),
          user.firstname,
          user.lastname,
          user.jobtitle,
          loginInfo,
          user.email,
          user.emailconfirmed,
          Some(persmissions),
          user.enabled))
    }
  }
}