libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.6" % "test"

lazy val commonSettings = Seq(
  organization := "org.rigney",
    version := "0.1.0",
      scalaVersion := "2.11.5"
      )

      lazy val root = (project in file(".")).
        settings(commonSettings: _*).
	  settings(
	      name := "cognitivescale"
	        )
