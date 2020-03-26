organization := "com.github.mercurievv.jobsearch"
name         := "job_search"

version := "0.1"

scalaVersion := "2.13.1"

lazy val scalaXml = "org.scala-lang.modules" %% "scala-xml"                   % "1.0.2"
lazy val scalaParser = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.1"

lazy val root = (project in file("."))
  .enablePlugins(ScalaxbPlugin)
  .settings(
    scalaxbPackageName in (Compile, scalaxb) := "com.github.mercurievv.rss.generated"
  )
val http4sVersion = "0.21.0"
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl"                           % http4sVersion,
  "org.http4s" %% "http4s-blaze-client"                  % http4sVersion,
  "com.beachape" %% "enumeratum"                         % "1.5.15",
  "com.chuusai" %% "shapeless"                           % "2.3.3",
  "org.scanamo" %% "scanamo"                             % "1.0.0-M12-1",
  "org.scanamo" %% "scanamo-zio"                         % "1.0.0-M12-1",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
  "dev.zio" %% "zio"                                     % "1.0.0-RC18-2",
  "dev.zio" %% "zio-interop-cats"                        % "2.0.0.0-RC12"
)
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.2.0"
//scalacOptions ++= Seq("-Ypartial-unification")
addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)
