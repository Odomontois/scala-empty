scalaVersion := "2.11.7"

val monocleVersion = "1.2.0-M1" // or "1.3.0-SNAPSHOT"

resolvers += Resolver.sonatypeRepo("releases")

fork in run := true

baseDirectory in run := baseDirectory.value / "working"

resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.2.5",
  "org.scalaz" %% "scalaz-core" % "7.1.1",
  "org.typelevel" %% "scalaz-spire" % "0.2",
  "com.twitter" %% "finagle-http" % "6.25.0",
  "org.spire-math" %% "spire" % "0.9.1" ,
  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
  "com.lihaoyi" %% "ammonite-ops" % "0.3.2",
  "com.typesafe.play" %% "play-json" % "2.4.3",
  "com.typesafe.akka" %% "akka-actor" % "2.4.0",

  "org.apache.spark" %% "spark-core" % "1.5.1",
  "org.apache.spark" %% "spark-sql" % "1.5.1",

  "com.github.julien-truffaut"  %%  "monocle-core"    % monocleVersion,
  "com.github.julien-truffaut"  %%  "monocle-generic" % monocleVersion,
  "com.github.julien-truffaut"  %%  "monocle-macro"   % monocleVersion,
  "com.github.julien-truffaut"  %%  "monocle-state"   % monocleVersion,
  "com.github.julien-truffaut"  %%  "monocle-law"     % monocleVersion % "test"

)

libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.11.1"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.0.0"

libraryDependencies += "com.github.melrief" %% "purecsv" % "0.0.2"