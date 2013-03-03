name := "scala-ga"

scalaVersion := "2.10.0"

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.cc/",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "sonatype releases"  at "https://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies ++= Seq(
  "io.spray" % "spray-can" % "1.1-M7",
  "io.spray" % "spray-client" % "1.1-M7",
  "io.spray" % "spray-util" % "1.1-M7",
  "io.spray" % "spray-http" % "1.1-M7",
  "io.spray" % "spray-httpx" % "1.1-M7",
  "io.spray" %%  "spray-json" % "1.2.3",
  "com.typesafe.akka" %% "akka-actor" % "2.1.1",
  "com.typesafe.akka" %% "akka-agent" % "2.1.1",
  "io.spray" % "spray-testkit" % "1.1-M7" % "test",
  "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test",
  "ch.qos.logback" %   "logback-classic" % "1.0.9"
)

