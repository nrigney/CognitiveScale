package org.rigney.cognitivescale.test

import org.scalatest.Matchers
import org.scalatest.FlatSpec
import org.scalatest.Inside
import org.rigney.cognitivescale.Point2d
import org.rigney.cognitivescale.Point3d
import org.rigney.cognitivescale.Point3d._
import org.rigney.cognitivescale.Board
import org.rigney.cognitivescale.Ship
import org.rigney.cognitivescale.Command
import org.rigney.cognitivescale.Commands
import org.rigney.cognitivescale.FiringPatterns._
import org.rigney.cognitivescale.East
import org.rigney.cognitivescale.West
import org.rigney.cognitivescale.South
import org.rigney.cognitivescale.North
import org.rigney.cognitivescale.AddTorpedo
import org.rigney.cognitivescale.MoveShip
import org.rigney.cognitivescale.PrintBoard
import org.rigney.cognitivescale.AddShot

class CommandSpec extends FlatSpec with Matchers with Inside {
  "A Command" should "initialize from a string" in {
    Commands.parse("alpha") shouldBe List(PrintBoard(Some("alpha")),AddShot(),AddTorpedo(Point2d(-1,1)), AddTorpedo(Point2d(-1,1)), AddTorpedo(Point2d(1,-1)), AddTorpedo(Point2d(1,1)), PrintBoard(None))
    Commands.parse("beta") shouldBe List(PrintBoard(Some("beta")),AddShot(),AddTorpedo(Point2d(-1,0)), AddTorpedo(Point2d(0,-1)), AddTorpedo(Point2d(0,1)), AddTorpedo(Point2d(1,0)), PrintBoard(None))
    Commands.parse("gamma") shouldBe List(PrintBoard(Some("gamma")),AddShot(),AddTorpedo(Point2d(-1,0)), AddTorpedo(Point2d(0,0)), AddTorpedo(Point2d(1,0)), PrintBoard(None))
    Commands.parse("delta") shouldBe List(PrintBoard(Some("delta")),AddShot(),AddTorpedo(Point2d(0,-1)), AddTorpedo(Point2d(0,0)), AddTorpedo(Point2d(0,1)), PrintBoard(None))
    Commands.parse("north") shouldBe List(PrintBoard(Some("north")),MoveShip(Point2d(0,1)), PrintBoard(None))
    Commands.parse("south") shouldBe List(PrintBoard(Some("south")),MoveShip(Point2d(0,-1)), PrintBoard(None))
    Commands.parse("east") shouldBe List(PrintBoard(Some("east")),MoveShip(Point2d(1,0)), PrintBoard(None))
    Commands.parse("west") shouldBe List(PrintBoard(Some("west")),MoveShip(Point2d(-1,0)), PrintBoard(None))
  }


}
