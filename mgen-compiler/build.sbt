
// Remember, sbt needs empty lines between active settings

name := "mgen-compiler"

organization := "se.culvertsoft"

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

crossPaths := false

retrieveManaged := true

libraryDependencies += "se.culvertsoft" % "mgen-api" % version.value

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

EclipseKeys.withSource := true

EclipseKeys.relativizeLibs := false
