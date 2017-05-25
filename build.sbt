scalaVersion := "2.11.8"

scalaOrganization := "org.typelevel"

val monocleV = "1.3.2" // or "1.3.0-SNAPSHOT"
val sparkV = "2.0.2"
val akkaV = "2.4.14"
val shapelessV = "2.3.2"
val spireV = "0.13.0"
//val playV = "2.5.10"
val macwireV = "2.2.5"
val catsV = "0.9.0"
//val alleyCatsV = "0.1.6"
val macroParadiseV = "2.1.0"
val parboiledV = "2.1.3"
val scalazV = "7.2.8"
val kindProjectorV = "0.9.3"
val ammoniteV = "0.8.1"
val nscalaTimeV = "2.14.0"
val scalaXmlV = "1.0.6"
val alleyCatsV = "0.1.8"
val doobieV = "0.4.1"
val mariaDBV = "1.5.5"
val enumeratumV = "1.5.9"

scalacOptions += "-Ypartial-unification"

resolvers += Resolver.sonatypeRepo("releases")

fork in run := true

baseDirectory in run := baseDirectory.value / "working"

resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.sonatypeRepo("snapshots")


libraryDependencies ++= Seq(
//  "org.nd4j" % "nd4j-x86" % "0.4-rc3.7",

  "org.typelevel" %% "cats" % catsV,
  "org.typelevel" %% "alleycats-core" % alleyCatsV,

  "com.chuusai" %% "shapeless" % shapelessV,
  "org.scalaz" %% "scalaz-core" % scalazV,
  "org.scalaz" %% "scalaz-effect" % scalazV,
//  "org.typelevel" %% "scalaz-spire" % "0.2",
  "org.spire-math" %% "spire" % spireV,
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-stream" % akkaV,
  "com.lihaoyi" %% "ammonite-ops" % ammoniteV,

//  "com.typesafe.play" %% "play-json" % playV,
//  "com.typesafe.play" %% "play-iteratees" % playV,
//  "com.typesafe.play" %% "play-ws" % playV,

  "org.apache.spark" %% "spark-core" % sparkV,
  "org.apache.spark" %% "spark-sql" % sparkV,
  "org.apache.spark" %% "spark-graphx" % sparkV,
  "org.apache.spark" %% "spark-mllib" % sparkV,

  //  "com.databricks" %% "spark-csv" % "1.3.0",

  "com.github.julien-truffaut" %% "monocle-core" % monocleV,
  "com.github.julien-truffaut" %% "monocle-generic" % monocleV,
  "com.github.julien-truffaut" %% "monocle-macro" % monocleV,
  "com.github.julien-truffaut" %% "monocle-state" % monocleV,
  "com.github.julien-truffaut" %% "monocle-law" % monocleV % "test" ,

  "org.tpolecat" %% "doobie-core" % doobieV,
  "org.mariadb.jdbc" % "mariadb-java-client" % mariaDBV
)

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % nscalaTimeV

//libraryDependencies += "com.github.melrief" %% "purecsv" % "0.0.2"

libraryDependencies += "org.parboiled" %% "parboiled" % parboiledV

dependencyOverrides ++= Set(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.4"
)

addCompilerPlugin("org.scalamacros" % "paradise" % macroParadiseV cross CrossVersion.full)

addCompilerPlugin("org.spire-math" %% "kind-projector" % kindProjectorV)

scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies += "com.softwaremill.macwire" %% "macros" % macwireV % "provided"

libraryDependencies += "com.softwaremill.macwire" %% "util" % macwireV

libraryDependencies += "com.softwaremill.macwire" %% "proxy" % macwireV

libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % scalaXmlV

libraryDependencies += "com.beachape" %% "enumeratum" % enumeratumV

lazy val newest = project