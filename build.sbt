organization  := "com.github.mercurievv.jobsearch"
name := "job_search"

version := "0.1"

scalaVersion := "2.13.1"



lazy val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.0.2"
lazy val scalaParser = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.1"

lazy val root = (project in file(".")).
  enablePlugins(ScalaxbPlugin).
  settings(
    scalaxbPackageName in (Compile, scalaxb)     := "com.github.mercurievv.rss.generated"
  )
val http4sVersion = "0.21.0"
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion
)
scalacOptions ++= Seq("-Ypartial-unification")