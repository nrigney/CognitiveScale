package org.rigney.cognitivescale.test

import org.rigney.cognitivescale.AddTorpedo
import org.rigney.cognitivescale.East
import org.rigney.cognitivescale.FiringPatterns.Alpha
import org.rigney.cognitivescale.MoveShip
import org.rigney.cognitivescale.North
import org.rigney.cognitivescale.Point2d
import org.rigney.cognitivescale.Ship
import org.scalatest.FlatSpec
import org.scalatest.Inside
import org.scalatest.Matchers
import org.rigney.cognitivescale.PrintBoard
import org.rigney.cognitivescale.AddShot

class ShipSpec extends FlatSpec with Matchers with Inside {
  "A Ship" should "initialize from a string" in {
    Ship.fromString("north north alpha east") shouldBe(
        List(List(
            PrintBoard(Some("north")), 
              MoveShip(Point2d(0,1)), 
            PrintBoard(None), 
            PrintBoard(Some("north")), 
              MoveShip(Point2d(0,1)), 
            PrintBoard(None), 
            PrintBoard(Some("alpha")), 
              AddShot(), 
                AddTorpedo(Point2d(-1,1)), 
                AddTorpedo(Point2d(-1,1)), 
                AddTorpedo(Point2d(1,-1)), 
                AddTorpedo(Point2d(1,1)), 
            PrintBoard(None), 
            PrintBoard(Some("east")), 
              MoveShip(Point2d(1,0)), 
            PrintBoard(None)))) 
  }

  "A Ship" should "initialize from a multiline string" in {
    Ship.fromString("north alpha\nnorth alpha east") shouldBe(
        List(List(PrintBoard(Some("north")), 
                    MoveShip(Point2d(0,1)), 
                  PrintBoard(None), 
                  PrintBoard(Some("alpha")), 
                    AddShot(), 
                      AddTorpedo(Point2d(-1,1)), 
                      AddTorpedo(Point2d(-1,1)), 
                      AddTorpedo(Point2d(1,-1)), 
                      AddTorpedo(Point2d(1,1)), 
                  PrintBoard(None)), 
                  List(PrintBoard(Some("north")), 
                         MoveShip(Point2d(0,1)), 
                       PrintBoard(None), 
                       PrintBoard(Some("alpha")), 
                         AddShot(), 
                           AddTorpedo(Point2d(-1,1)), 
                           AddTorpedo(Point2d(-1,1)), 
                           AddTorpedo(Point2d(1,-1)), 
                           AddTorpedo(Point2d(1,1)), 
                       PrintBoard(None), 
                       PrintBoard(Some("east")), 
                         MoveShip(Point2d(1,0)), 
                       PrintBoard(None)))) 
  }

}
