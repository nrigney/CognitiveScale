
import org.rigney.cognitivescale.Board

object CognitiveScale {
  def main(args: Array[String]): Unit = {
    if(args.length == 2) {
      val fieldFile = args(0)
      val scriptFile = args(1)
      Board.fromFiles(fieldFile, scriptFile)
    } else {
      println("Arguments must be <field file> <script file>")
    }
  }
}
