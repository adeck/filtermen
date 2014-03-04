scalaVersion := "2.10.1"

scalaOrganization := "org.scala-lang.virtualized"

unmanagedJars in Compile += Attributed.blank(file("../scalapipe.jar"))
