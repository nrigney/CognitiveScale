package org.rigney.cognitivescale.test

import org.scalatest.Matchers
import org.scalatest.FlatSpec
import org.scalatest.Inside
import org.rigney.cognitivescale.Point2d
import org.rigney.cognitivescale.Point3d
import org.rigney.cognitivescale.Point3d._
import org.rigney.cognitivescale.Board
import org.rigney.cognitivescale.Ship

class BoardSpec extends FlatSpec with Matchers with Inside {
  "A Board" should "initialize from a string" in {
    implicit val board: Board = Board(List[Point3d](), 0, Ship(Point3d(0, 0, 0)))
    inside(Board.fromString("a")) {
      case Board(mines, initialCount, ship, currentTick, state) =>
        mines shouldBe (List(Point3d(0, 0, 1)))
    }
    inside(Board.fromString("z")) {
      case Board(mines, initialCount, ship, currentTick, state) =>
        mines shouldBe (List(Point3d(0, 0, 26)))
    }
    inside(Board.fromString("A")) {
      case Board(mines, initialCount, ship, currentTick, state) =>
        mines shouldBe (List(Point3d(0, 0, 27)))
    }
    inside(Board.fromString(".")) {
      case Board(mines, initialCount, ship, currentTick, state) =>
        mines shouldBe (List())
    }
    inside(Board.fromString("a.\nb.")) {
      case Board(mines, initialCount, ship, currentTick, state) =>
        mines shouldBe (List(Point3d(0, 0, 1), Point3d(0, 1, 2)))
    }
  }

  it should "print properly from zloc 0" in {
    Board.boardAsString(Board.fromString("b\nc"), 0) shouldBe "b\nc"
  }

  it should "print properly from zloc 1" in {
    Board.boardAsString(Board.fromString("b.\nc."), 1) shouldBe "a\nb"
  }

  it should "add columns to recenter the ship" in {
    Board.boardAsString(Board.fromString("b.\n.c"), 0) shouldBe "b.\n.c"
  }

  it should "add rows to recenter the ship" in {
    println(Board.boardAsString(Board.fromString("b.\n.c\n.c"), 0))
    Board.boardAsString(Board.fromString("b.\n.c\n.c"), 0) shouldBe "b.\n.c\n.c"
  }

  it should "remove mines that would be affected by a torpedo" in {
    val startBoard = Board.fromString(".b.\n..c\n.d.")
    Board.boardAsString(startBoard.addTorpedo(Point3d(1, 0, 0)), 0) shouldBe "..c\n.d."
  }

  it should "not remove mines that have been passed" in {
    val startBoard = Board.fromString(".b.\n..c\n.d.")
    Board.boardAsString(startBoard.addTorpedo(Point3d(1, 0, 5)), 0) shouldBe ".b.\n..c\n.d."
  }

  it should "Initialize the ship at (0,0,0)" in {
    val startBoard = Board.fromString(".b.\n.c.\n..d\n")
    startBoard.ship.position shouldBe Point3d(0, 1, 0)
  }

  it should "move a ship north" in {
    val startBoard = Board.fromString(".b.\n.c.\n..d\n")
    inside(startBoard.moveShip(Point2d(0, 1))) {
      case Board(mines, initialCount, ship, currentTick, state) =>
        inside(ship) {
          case Ship(position, moves, shotsFired, commands) =>
            position shouldBe (Point3d(0, 2, 0))
        }
    }
  }

  it should "move a ship south" in {
    val startBoard = Board.fromString(".b.\n.c.\n..d\n")
    inside(startBoard.moveShip(Point2d(0, -1))) {
      case Board(mines, initialCount, ship, currentTick, state) =>
        inside(ship) {
          case Ship(position, moves, shotsFired, commands) =>
            position shouldBe (Point3d(0, 0, 0))
        }
    }
  }

  it should "move a ship east" in {
    val startBoard = Board.fromString(".b.\n.c.\n..d\n")
    inside(startBoard.moveShip(Point2d(1, 0))) {
      case Board(mines, initialCount, ship, currentTick, state) =>
        inside(ship) {
          case Ship(position, moves, shotsFired, commands) =>
            position shouldBe Point3d(1, 1, 0)
        }
    }
  }

  it should "move a ship west" in {
    val startBoard = Board.fromString(".b.\n.c.\n..d\n")
    inside(startBoard.moveShip(Point2d(-1, 0))) {
      case Board(mines, initialCount, ship, currentTick, state) =>
        inside(ship) {
          case Ship(position, moves, shotsFired, commands) =>
            position shouldBe Point3d(-1, 1, 0)
        }
    }
  }

  it should "increment tickCount when calling tick()" in {
    val startBoard = Board.fromString(".b.\n.c.\n..d\n")
    inside(startBoard.tick) {
      case Board(mines, initialCount, ship, currentTick, state) =>
        currentTick shouldBe 1
    }
  }

  it should "drop the ship 1km when calling tick()" in {
    val startBoard = Board.fromString(".b.\n.c.\n..d\n")
    val nextBoard = startBoard.tick
    inside(nextBoard) {
      case Board(mines, initialCount, ship, currentTick, state) =>
        currentTick shouldBe 1
        inside(ship) { case Ship(position, moves, shotsFired, commands) =>
          position.z shouldBe 1
        }
    }
  }

  it should "execute ticks from a Ship until there are no more commands" in {
    val shipString = "gamma"
    val board = Board.fromString("z", shipString)
    Board.run(board)
  }
  
  it should "initialize and run from a board and script file" in {
    Board.fromFiles(getClass.getResource("/ex1.brd").getPath,getClass.getResource("/ex1.scr").getPath)
  }

  it should "initialize and run from a board and script file" in {
    Board.fromFiles(getClass.getResource("/ex2.brd").getPath,getClass.getResource("/ex2.scr").getPath)
  }
}