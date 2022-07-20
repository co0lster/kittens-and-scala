ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "scalaWithCats"
  )

libraryDependencies +=
  "org.typelevel" %% "cats-core" % "2.1.0"

scalacOptions ++= Seq(
  "-Xfatal-warnings"
)