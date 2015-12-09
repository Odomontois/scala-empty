scalaVersion := "2.11.7"

val monocleV = "1.2.0-M1" // or "1.3.0-SNAPSHOT"
val sparkV = "1.5.2"

resolvers += Resolver.sonatypeRepo("releases")

fork in run := true

baseDirectory in run := baseDirectory.value / "working"

resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.sonatypeRepo("snapshots")


libraryDependencies ++= Seq(
  "org.nd4j" % "nd4j-x86" % "0.4-rc3.7",

  "com.chuusai" %% "shapeless" % "2.2.5",
  "org.scalaz" %% "scalaz-core" % "7.2.0",
  "org.typelevel" %% "scalaz-spire" % "0.2",
  "org.spire-math" %% "spire" % "0.9.1",
  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
  "com.lihaoyi" %% "ammonite-ops" % "0.3.2",
  "com.typesafe.play" %% "play-json" % "2.4.4",
  "com.typesafe.play" %% "play-iteratees" % "2.4.4",

  "com.typesafe.akka" %% "akka-actor" % "2.4.0",

  "org.apache.spark" %% "spark-core" % sparkV,
  "org.apache.spark" %% "spark-sql" % sparkV,
  "org.apache.spark" %% "spark-graphx" % sparkV,
  "org.apache.spark" %% "spark-mllib" % sparkV,

  "com.databricks" %% "spark-csv" % "1.3.0",

  "com.github.julien-truffaut" %% "monocle-core" % monocleV,
  "com.github.julien-truffaut" %% "monocle-generic" % monocleV,
  "com.github.julien-truffaut" %% "monocle-macro" % monocleV,
  "com.github.julien-truffaut" %% "monocle-state" % monocleV,
  "com.github.julien-truffaut" %% "monocle-law" % monocleV % "test"
)

libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.11.1"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.0.0"

libraryDependencies += "com.github.melrief" %% "purecsv" % "0.0.2"

libraryDependencies += "org.parboiled" %% "parboiled" % "2.1.0"

dependencyOverrides ++= Set(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.4"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0-M5" cross CrossVersion.full)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.7.1")

scalacOptions ++= Seq("-unchecked", "-deprecation")