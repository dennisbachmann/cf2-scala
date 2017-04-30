name := "cf2-scala"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" %% "spark-core"  % "2.0.0"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.0.0"

libraryDependencies += "org.scala-lang"         % "scala-compiler"                 % "2.11.8"
libraryDependencies += "org.scala-lang.modules" % "scala-parser-combinators_2.11"  % "1.0.4"
libraryDependencies += "org.scala-lang.modules" % "scala-xml_2.11"                 % "1.0.4"