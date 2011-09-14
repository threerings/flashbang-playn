import sbt._
import Keys._

object FlashbangBuild extends Build {
  lazy val flashbang = Project(
    "flashbang", file("."), settings = Defaults.defaultSettings ++ Seq(
      organization := "com.threerings",
      version      := "1.0-SNAPSHOT",
      name         := "flashbang-playn",
      crossPaths   := false,
      scalaVersion := "2.9.0-1",

      javacOptions ++= Seq("-Xlint", "-Xlint:-serial", "-source", "1.6", "-target", "1.6"),
      fork in Compile := true,

      // TODO: reenable doc publishing when scaladoc doesn't choke on our code
      publishArtifact in (Compile, packageDoc) := false,

      autoScalaLibrary := false, // no scala-library dependency
      libraryDependencies ++= Seq(
        "com.samskivert" % "pythagoras" % "1.1-SNAPSHOT",
        "com.threerings" % "react" % "1.0-SNAPSHOT",
        "com.googlecode.playn" % "playn-core" % "1.0-SNAPSHOT",
        "com.googlecode.playn" % "playn-java" % "1.0-SNAPSHOT",
        "com.threerings" % "tripleplay" % "1.0-SNAPSHOT",
        "com.google.guava" % "guava" % "r09"
      )
    )
  )
}
