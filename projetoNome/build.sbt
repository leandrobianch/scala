lazy val root = (project in file("."))
  .settings(
    name := "projetoNome",
    scalaVersion := "2.13.16",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "3.4.0",
      "org.apache.spark" %% "spark-sql" % "3.4.0",
      "com.azure" % "azure-storage-blob" % "12.21.0",
      "org.scalatest" %% "scalatest" % "3.2.15" % Test
    )
       // Enable hot reload
    // Trigger recompilation on file changes
    //Global / onChangedBuildSource := ReloadOnSourceChanges
    // assembly / test := {},
    // assembly / assemblyMergeStrategy := {
    //   case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    //   case _ => MergeStrategy.first
    // }
  )

//enablePlugins(AssemblyPlugin)
