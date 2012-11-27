import sbt._
import sbt.Keys._

object BoardgameboardBuild extends Build {

  lazy val boardgameboard = Project(
    id = "boardgameboard",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "boardgameboard",
      organization := "me.thefalcon",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.9.2",
      libraryDependencies ++= Seq(
        "io.spray" %% "spray-json" % "1.2.2" cross CrossVersion.full,
        "net.sf.opencsv" % "opencsv" % "2.1"
      )
    )
  )
}
