//addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.12")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.0")
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.4.9")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.16")
addSbtPlugin("net.vonbuchholtz" % "sbt-dependency-check" % "2.0.0")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.5.1")

addSbtPlugin("org.scalaxb" % "sbt-scalaxb" % "1.7.3")
addSbtPlugin("com.gilt.sbt" % "sbt-aws-lambda" % "0.7.0")

resolvers += Resolver.sonatypeRepo("public")
