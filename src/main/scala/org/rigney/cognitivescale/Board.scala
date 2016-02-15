package org.rigney.cognitivescale

import scala.collection.immutable.StringLike
import scala.math._
import scala.io.Source

/* Board state is manipulated (after initial load) by a series of BoardCommands that are built from the Ship script.
 * See the Command definitions for more details on command expansion.  The BoardCommands trait serves to unify the commands
 * so they can be manipulated in mass (and we have some loose type checking on the command queue)  
 */
trait BoardCommands

/* Move a ship in a 2d plane.  The sender of the command can't effect the z-axis (only the Board itself can do that) */
case class MoveShip(position: Point2d) extends BoardCommands
/* Add a single torpedo to the board.  I don't want to teach a Board about firing patterns -- that logic belongs to the Ship.
 * However, the Board _does_ need to understand that there are torpedos.  This allows that.  Notice that again we can't put
 * torpedos at random spots -- they have to come in the game at the same z-loc as the ship, which is in the base "rules"
 */
case class AddTorpedo(position: Point2d) extends BoardCommands
/* Add a shot.  The rules have a little bit of ambiguity here -- one would think a "shot" would be a single torpedo, but it isn't in the
 * examples.  Since we've decoupled the patterns and the torpedo placement, we have to have some way to say "this counts as a shot."
 * AddShot does this by incrementing the shot count on the ship.  Handling this in the ship could be done as well, but this was quicker. 
 */
case class AddShot() extends BoardCommands
/* Print the board.  Again since we've decoupled the script with the actual board commands, we have to have a way to say "print the board".
 * There's an optional "command" to print when we print the board.  This is an option because I was putting a PrintBoard at the end of a 
 * command set as well, but it turns out that was just me trying to be symmetric in the code.  The reality is that we only print the board
 * after each command _set_, so we can print it automatically in the tick code.
 */
case class PrintBoard(command: Option[String]) extends BoardCommands

/**
 * Board contains the rules for scoring, printing, and loading a board definition.
 * Board also holds a ship, a list of mines, a count of mines we've destroyed (or scoring), a game state, and a current tick in the game.
 *
 */
case class Board(mines: List[Point3d], destroyedMineCount: Int, ship: Ship, currentTick: Int = 0, state: Int = 0) {
  val initialMineCount = mines.length + destroyedMineCount

  /** Score the board.  Left() is for fail, Right() is for pass, in standard scala Either[] convention*/
  def score: Either[Int, Int] = {
    val passMine = mines.exists(_.z <= ship.position.z)
    val minesRemain = mines.length > 0
    val scriptRemains = !ship.commands.isEmpty
    if (passMine || (scriptRemains && minesRemain))
      Left(0)
    else if (!minesRemain && scriptRemains)
      Right(1)
    else {
      Right((10 * initialMineCount) - min(5 * ship.shotsFired, 5 * initialMineCount) - min(2 * ship.moves, 3 * initialMineCount))
    }
  }
  
  /* The helper for adding a torpedo that get the ship position automatically*/
  def addTorpedo(position: Point2d): Board = addTorpedo(Point3d(position.x, position.y, ship.position.z))

  /* Internal method to add a torpedo at a specific point in space*/
  def addTorpedo(position: Point3d): Board = {
    //Find the longest set of mines that are at or blow the torpedos coordinates
    val newMines = mines.dropWhile { mine =>
      mine.atOrBelow(position)
    }
    this.copy(mines = newMines, destroyedMineCount + mines.length - newMines.length, ship = ship.copy(shotsFired = ship.shotsFired + 1))
  }

  /* Move the ship. */
  def moveShip(newOffset: Point2d): Board = Board(mines, destroyedMineCount, ship.copy(position = ship.position + newOffset, moves = ship.moves + 1), currentTick)

  /* Execute a single tick in the game.  Will iterate over all the commands that should execute in this tick, evaluate the board for pass/fail,
   * then drop the ship and return the new board state.
   */
  def tick = {
    val newTick = currentTick + 1
    val commands = ship.nextTick()
    val newBoard = commands match {
      case None =>
        this.copy(state = 1)
      case Some(commandsToExecute) =>
        println(s"Step ${newTick}")
        commandsToExecute.foldLeft(this) { (board, command) =>
          command match {
            case MoveShip(position) =>
              board.moveShip(position)
            case AddTorpedo(position) =>
              val after = board.addTorpedo(position)
              after
            case AddShot() =>
              board.copy(ship = board.ship.copy(shotsFired = board.ship.shotsFired + 1))
            case PrintBoard(command) =>
              println(Board.boardAsString(board) + "\n")
              command.map { c =>
                println(c + "\n")
              }
              board
          }
        }
    }

    /* Most of this could be done in a more functional way, but this works well enough and is a little clearer */
    val newShip = newBoard.ship.copy(commands = newBoard.ship.commands.drop(1), position = newBoard.ship.position + Point3d(0, 0, 1))
    val minesMissed = newBoard.mines.exists(_.z <= newShip.position.z)
    val minesCleared = newBoard.mines.length == 0
    val newState = if (minesMissed || minesCleared || newBoard.state == 1) 1 else 0
    val toReturn = newBoard.copy(currentTick = newTick, ship = newShip, state = newState)
    println(Board.boardAsString(toReturn))
    toReturn
  }

}

object Board {
  
  /* Load the board and script from files.  Doesn't do a lot of error checking, which we would do in production code */
  def fromFiles(boardFile: String, scriptFile: String) = {
    //NOTE: Don't use this in production code, but for this toy it'll work
    val boardString = Source.fromFile(boardFile).mkString
    val scriptString = Source.fromFile(scriptFile).mkString
    Board.run(fromString(boardString, scriptString))
  }
  
  /* Initialize the board and ship from a string. We really ought to make this pull from iterators, but that's a bigger project*/
  def fromString(initialString: String, shipString: String = ""): Board = {
    val (initlines, widthIter) = initialString.lines.duplicate
    val length = widthIter.size
    val width = widthIter.foldLeft(0) { (size, row) =>
      scala.math.max(size, row.length())
    }
    Board(initlines.zipWithIndex.flatMap {
      case (row, rowIndex) =>
        row.zipWithIndex.flatMap {
          case (col, colIndex) =>
            col match {
              case d if d >= 'a' && d <= 'z' =>
                Some(Point3d(colIndex, rowIndex, d.asDigit - 9))
              case d if d >= 'A' && d <= 'Z' =>
                Some(Point3d(colIndex, rowIndex, d.asDigit - 9 + 26))
              case '.' =>
                None
            }
        }
    }.toList, 0, Ship(position = Point3d(scala.math.floor(width / 2).toInt, scala.math.floor(length / 2).toInt, 0), commands = Ship.fromString(shipString)))
  }

  /* Print the board.  Prints relative to the ship -- really a helper function */
  def boardAsString(board: Board): String = boardAsString(board, board.ship.position.z)

  /* Print the board from an arbitrary zloc */
  def boardAsString(board: Board, zloc: Int): String = {
    val shipx = board.ship.position.x
    val shipy = board.ship.position.y
    /* Find the min and max board we need to see.  This code has a bug (or at least it doesn't match the examples, 
    	 possibly because of ship placement */
    val minMaxXY = board.mines.foldLeft((shipx, shipy, shipx, shipy)) { (minXY, mine) =>
      (min(minXY._1, mine.x), min(minXY._2, mine.y), max(minXY._3, mine.x), max(minXY._4, mine.y))
    }

    /* The char math here is unfortunate, but I really don't need a lot of finesse here */
    (minMaxXY._2 to minMaxXY._4).map { y =>
      (minMaxXY._1 to minMaxXY._3).map { x =>
        board.mines.find { mine =>
          mine.x == x && mine.y == y
        }.flatMap { m =>
          (m.z - zloc) match {
            case d if d <= 0 =>
              Some('*')
            case d if d <= 26 =>
              Some((d + 'a' - 1).toChar)
            case d if d > 27 =>
              Some((d - 26 + 'A' - 1).toChar)
          }
        }.getOrElse('.')
      }.mkString
    }.mkString("\n")
  }

  /* Run the entire game.  Just execute ticks until the board status is not 0.
   * Given a better design, the board would allow me to run a fold across it, which
   * would get rid of the unfortunate var.  However, in a larger application with much
   * larger data sets, it's likely the var would remain as a speedup measure, or we'd use
   * a Stream.
   */
  def run(board: Board) = {
    var currentBoard: Board = board
    while (currentBoard.state != 1) {
      currentBoard = currentBoard.tick
    }

    /* Score final board result and print the resulting values */
    currentBoard.score match {
      case Left(score) =>
        println(s"fail (${score})")
      case Right(score) =>
        println(s"pass (${score})")
    }

  }

}