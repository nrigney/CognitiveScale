package org.rigney.cognitivescale.test

import org.scalatest.Matchers
import org.scalatest.FlatSpec
import org.rigney.cognitivescale.Point2d
import org.rigney.cognitivescale.Point3d
import org.rigney.cognitivescale.Point3d._

class PointSpec extends FlatSpec with Matchers {
  "A Point2d" should "detect that 2 points are in the same place" in {
    Point2d(0, 0) == (Point2d(0, 0)) shouldBe (true)
  }
  
  it should "detect that 2 points are not in the same place" in {
    Point2d(0, 0) == (Point2d(1, 0)) shouldBe (false)
  }

  "A Point3d" should "detect that 2 points are in the same place" in {
    Point3d(0, 0, 0) == (Point3d(0, 0, 0)) shouldBe (true)
  }
  
  it should "detect that 2 points are not in the same place" in {
    Point3d(0, 0, 0) == (Point3d(1, 0, 0)) shouldBe (false)
  }

  it should "detect that 2 points are on the same z-coordinate" in {
    Point3d(4,4,4).passing(Point3d(1,1,4)) shouldBe true
  }

  it should "detect that 2 points are not on the same z-coordinate" in {
    Point3d(4,4,4).passing(Point3d(1,1,3)) shouldBe false
  }
}