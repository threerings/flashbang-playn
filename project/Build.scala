import sbt._
import Keys._

object FlashbangBuild extends Build {
  val locals = com.samskivert.condep.Depends(
    ("tripleplay", null, "com.threerings" % "tripleplay" % "1.0-SNAPSHOT")
  )

  lazy val flashbang = locals.addDeps(Project(
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
      libraryDependencies ++= locals.libDeps ++ Seq(
        "com.google.guava" % "guava" % "10.0"
      )
    )
  ))
}
