import $file.square

import scala.io.Source

import square.Square

object MapParser {
  def parse(path: os.Path): Seq[Seq[Square]] = {
    val file = Source.fromFile(path.toString)
    val data = file.getLines.map { line =>
      line.split(" ").toSeq.map { Square.fromHexString(_) }
    }.toSeq
    file.close()

    data
  }
}
