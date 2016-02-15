package org.rigney.cognitivescale

import org.rigney.cognitivescale.FiringPatterns._

/* The big thing to note here is the giant match/case.
 * We take script commands and convert them into Board commands here.  All of the firing pattern commands
 * expand into a list of AddTorpedo commands for the fancy decoupling.  Note the firing commands are external and
 * could be modified without touching this code.
 */
trait Command

trait MovementCommand extends Command

case class North() extends MovementCommand
case class South() extends MovementCommand
case class East() extends MovementCommand
case class West() extends MovementCommand

object Commands {
  def parse(commandString:String):List[BoardCommands] = {
     val expandedCommand = commandString match {
      case "north" =>
        List(MoveShip(Point2d(0,1)))
      case "south" =>
        List(MoveShip(Point2d(0,-1)))
      case "east" =>
        List(MoveShip(Point2d(1,0)))
      case "west" =>
        List(MoveShip(Point2d(-1,0)))
      case "alpha" =>
        List(AddShot()) ++ Alpha().offsets().map{ torpedo => AddTorpedo(torpedo) }
      case "beta" =>
        List(AddShot()) ++ Beta().offsets().map{ torpedo => AddTorpedo(torpedo) }
      case "gamma" =>
        List(AddShot()) ++ Gamma().offsets().map{ torpedo => AddTorpedo(torpedo) }
      case "delta" =>
        List(AddShot()) ++ Delta().offsets().map{ torpedo => AddTorpedo(torpedo) }
    }
     List(PrintBoard(Some(commandString))) ++ expandedCommand
  }
}
