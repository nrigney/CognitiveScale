package org.rigney.cognitivescale

import scala.math.Ordering.Implicits._

/** Point is the base trait for coordinate systems.
 *  The requirements already specify 2 coordinate systems, and realistically that's not
 *  an upper bound of coordinate systems.  compass/azimuth is much more likely in 3d systems, for example.
 *  
 */

case class Point2d(x:Int = 0, y:Int = 0) {
  def equals(other:Point2d) = x.equals(other.x) && y.equals(other.y)
}

case class Point3d(x:Int = 0, y:Int = 0, z:Int = 0) {
  def equals(other:Point3d) = x.equals(other.x) && y.equals(other.y) && z.equals(other.z)
  
  def +(other:Point3d):Point3d = Point3d(x + other.x, y + other.y, z + other.z)
  
  def +(other:Point2d):Point3d = Point3d(x + other.x, y + other.y, z)
  
  def passing(other:Point3d):Boolean = {  z == other.z }
  
  def atOrBelow(other:Point3d):Boolean = { x == other.x && y == other.y && z >= other.z }
}

object Point2d {
  implicit def point3dTo2d(other:Point3d):Point2d = Point2d(other.x,other.y)
}

object Point3d {  
  implicit def point2dTo3d(other:Point2d, z:Int):Point3d = Point3d(other.x, other.y, z)
}
