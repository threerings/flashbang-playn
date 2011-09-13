import sbt._
import Keys._

// allows projects to be symlinked into the current directory for a direct dependency,
// or fall back to obtaining the project from Maven otherwise
class Locals (locals :(String, String, ModuleID)*) {
  def addDeps (p :Project) = (locals collect {
    case (id, subp, dep) if (file(id).exists) => symproj(file(id), subp)
  }).foldLeft(p) { _ dependsOn _ }
  def libDeps = locals collect {
    case (id, subp, dep) if (!file(id).exists) => dep
  }
  private def symproj (dir :File, subproj :String = null) =
    if (subproj == null) RootProject(dir) else ProjectRef(dir, subproj)
}

object FlashbangBuild extends Build {
  val locals = new Locals(
    ("tripleplay",     null, "com.threerings" % "tripleplay" % "1.0-SNAPSHOT")
  )

  lazy val flashbang = locals.addDeps(Project(
    "flashbang", file("."), settings = Defaults.defaultSettings ++ Seq(
      organization := "com.threerings",
      version      := "1.0-SNAPSHOT",
      name         := "flashbang-playn",
      crossPaths   := false,
      scalaVersion := "2.9.0-1",

      javacOptions ++= Seq("-Xlint", "-Xlint:-serial"),
      fork in Compile := true,

      autoScalaLibrary := false, // no scala-library dependency
      libraryDependencies ++= locals.libDeps ++ Seq(
        "com.google.guava" % "guava" % "r09"
      )
    )
  ))
}