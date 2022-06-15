ThisBuild / scalaVersion := "3.1.2"

addCommandAlias("fmt", "scalafmtAll")

lazy val derive = (project in file("."))
  .settings(
    name := "reporting",
    scalacOptions := compilerOptions,
    libraryDependencies ++= Seq(
      dependencies.scalaTest,
      dependencies.scalaTestFreespec,
    )
  )
  .enablePlugins(JavaAppPackaging)

lazy val compilerOptions =
  Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-unchecked",
    "-Yexplicit-nulls",
    "-Ysafe-init",
    "-new-syntax",
    "-indent",
    // "-rewrite",
  )

lazy val dependencies =
  new {
    val scalaTestV = "3.2.12"

    val scalaTest = "org.scalatest" %% "scalatest" % scalaTestV % "test"
    val scalaTestFreespec =
      "org.scalatest" %% "scalatest-freespec" % scalaTestV % "test"
  }
