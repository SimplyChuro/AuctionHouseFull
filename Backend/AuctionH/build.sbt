name := """auction"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice

lazy val myProject = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

// Database
libraryDependencies += javaJdbc
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.5"
libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"

// Mail
libraryDependencies += "com.typesafe.play" %% "play-mailer" % "6.0.1"
libraryDependencies += "com.typesafe.play" %% "play-mailer-guice" % "6.0.1"

// Amazon Web Services
libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.11.475"
libraryDependencies += "net.java.dev.jets3t" % "jets3t" % "0.9.4"

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

//Akka

val akkaVersion = "2.5.16"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  // Only if you are using Akka Testkit
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion
)

val akkaHTTPVersion = "10.1.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core" % akkaHTTPVersion,
  // Add this one if you are using HTTP/2
  "com.typesafe.akka" %% "akka-http2-support" % akkaHTTPVersion
)
