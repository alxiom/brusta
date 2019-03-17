name := "lever"

version := "0.1.0"

scalaVersion := "2.12.8"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

val javaxV = "1"
val guiceV = "4.0"
val akkaV = "2.5.17"
val colossusV = "0.11.0"
val json4sV = "3.5.3"
val loggerV = "3.9.0"
val logbackV = "1.2.3"

libraryDependencies ++= Seq(
  "javax.inject" % "javax.inject" % javaxV,
  "com.google.inject" % "guice" % guiceV,
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.tumblr" %% "colossus" % colossusV,
  "org.json4s" %% "json4s-native" % json4sV,
  "org.json4s" %% "json4s-jackson" % json4sV,
  "com.typesafe.scala-logging" %% "scala-logging" % loggerV,
  "ch.qos.logback" % "logback-classic" % logbackV
)
