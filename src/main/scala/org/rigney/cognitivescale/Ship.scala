package org.rigney.cognitivescale

import org.rigney.cognitivescale.FiringPatterns._

case class Ship(position:Point3d, moves: Int = 0, shotsFired:Int = 0, commands: List[List[BoardCommands]] = List()) {
  
  def nextTick():Option[List[BoardCommands]] = {
    commands.take(1).headOption
  }
}

object Ship {
  def fromString(commands:String):List[List[BoardCommands]] = {
    commands.lines.map { command =>
      command.split(" ").map { word =>
        Commands.parse(word.trim)
      }.flatten.toList
    }.toList
  }
}