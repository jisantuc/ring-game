import scala.sys.process._
import scala.language.postfixOps

import sbtwelcome._

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.5.0"

lazy val ringgame =
  (project in file("."))
    .enablePlugins(ScalaJSPlugin)
    .settings( // Normal settings
      name         := "ringgame",
      version      := "0.0.1",
      scalaVersion := "3.1.1",
      organization := "io.github.jisantuc",
      libraryDependencies ++= Seq(
        "dev.optics"      %%% "monocle-core" % "3.1.0",
        "io.indigoengine" %%% "tyrian"       % "0.3.2",
        "org.scalameta"   %%% "munit"        % "0.7.29" % Test
      ),
      testFrameworks += new TestFramework("munit.Framework"),
      scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
      scalafixOnCompile := true,
      semanticdbEnabled := true,
      semanticdbVersion := scalafixSemanticdb.revision,
      autoAPIMappings   := true
    )
    .settings( // Launch VSCode when you type `code` in the sbt terminal
      code := {
        val command = Seq("code", ".")
        val run = sys.props("os.name").toLowerCase match {
          case x if x contains "windows" => Seq("cmd", "/C") ++ command
          case _                         => command
        }
        run.!
      }
    )
    .settings( // Welcome message
      logo := "ring-game (v" + version.value + ")",
      usefulTasks := Seq(
        UsefulTask("", "fastOptJS", "Rebuild the JS (use during development)"),
        UsefulTask(
          "",
          "fullOptJS",
          "Rebuild the JS and optimise (use in production)"
        ),
        UsefulTask("", "code", "Launch VSCode")
      ),
      logoColor        := scala.Console.MAGENTA,
      aliasColor       := scala.Console.BLUE,
      commandColor     := scala.Console.CYAN,
      descriptionColor := scala.Console.WHITE
    )

lazy val code =
  taskKey[Unit]("Launch VSCode in the current directory")
