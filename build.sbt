import sbt.ExclusionRule

lazy val commonSettings = Seq(
  version := "1.0",
  organization := "com.hortonworks",
  scalaVersion := "2.11.12",
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
  test in assembly := {}
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "sparksolr",
    mainClass in Compile := Some("solution.SolrReader")
  )

resolvers += "Restlet Repositories" at "http://maven.restlet.org"
resolvers += "SparkPackages" at "https://dl.bintray.com/spark-packages/maven/"

// additional libraries
libraryDependencies ++= Seq(
//  "org.apache.spark" %% "spark-core" % "2.3.1" % "provided",
//  "org.apache.spark" %% "spark-sql" % "2.3.1" % "provided",
//  HDP 3.0.1
//  "com.lucidworks.spark" % "spark-solr" % "3.5.5" excludeAll (
//    ExclusionRule("org.apache.hadoop")
//    ),
//  "org.apache.hadoop" % "hadoop-client" % "3.1.0",
//  HDP 2.6.3
    "com.lucidworks.spark" % "spark-solr" % "3.2.2" excludeAll (
      ExclusionRule("org.apache.hadoop")
      ),
  "org.apache.hadoop" % "hadoop-client" % "2.7.3",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.2",
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.8.2"
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case PathList("javax", "servlet", xs@_*) => MergeStrategy.first
  case PathList(ps@_*) if ps.last endsWith ".html" => MergeStrategy.first
  case "application.conf" => MergeStrategy.concat
  case "unwanted.txt" => MergeStrategy.discard
  case x => MergeStrategy.first
}
