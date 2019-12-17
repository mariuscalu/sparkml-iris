name := """sparkml-iris"""

assemblyJarName in assembly := "sparkml-iris.jar"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.6.0"
  ,"org.apache.spark" %% "spark-sql" % "1.6.0"
  ,"org.apache.spark" %% "spark-mllib" % "1.6.0"
  ,"org.scalatest"   %% "scalatest"  % "2.2.4" % "test"
)
