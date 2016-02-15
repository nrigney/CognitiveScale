package org.rigney.cognitivescale.FiringPatterns

import org.rigney.cognitivescale.Point2d
import org.rigney.cognitivescale.Command


/** Implenters of FiringPattern will supply a list of firing offsets
 *  This allows firing pattern definitions to be decoupled from the rest of the system.
 *  The big win there is that Firing Patterns can be imported from entirely different
 *  compilation units.
 *  
 *  Note FiringCommand extends Command so the ship can return a nice type-safe list of operations 
 */
trait FiringPattern extends Command {
  def offsets():List[Point2d]
} 

/* The basic firing patterns defined in the requirements */

/** Alpha fires in each of the cardinal directions
 *  
 */
case class Alpha() extends FiringPattern {
  def offsets():List[Point2d] = List(Point2d(-1,1),Point2d(-1,1),Point2d(1,-1),Point2d(1,1))
}

case class Beta() extends FiringPattern {
  def offsets():List[Point2d] = List(Point2d(-1,0),Point2d(0,-1),Point2d(0,1),Point2d(1,0))
}

case class Gamma() extends FiringPattern {
  def offsets():List[Point2d] = List(Point2d(-1,0),Point2d(0,0),Point2d(1,0))
}

case class Delta() extends FiringPattern {
  def offsets():List[Point2d] = List(Point2d(0,-1),Point2d(0,0),Point2d(0,1))
}