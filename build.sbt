name := """play-getting-started"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  filters,
  "org.postgresql" % "postgresql" % "9.4.1212",
  "com.github.tminglei" %% "slick-pg" % "0.14.3",
  "com.typesafe.play" % "play-slick_2.11" % "2.0.2",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ )
