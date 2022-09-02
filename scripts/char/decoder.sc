import $file.char

import java.nio.file.{Files, Paths}
import os.Path
import scala.io.Source

import char.SmtChar

object CharDecoder {
  def parseString(str: String): Seq[String] = {
    str.toCharArray.sliding(2, 2).toSeq.map(_.mkString)
  }

  def decodeFromString(str: String): String = {
    parseString(str).map(hex => SmtChar.decodeHexString(hex).toChar).mkString
  }

  def decodeFromFile(path: Path): String = {
    val bytes = Files.readAllBytes(Paths.get(path.toString))
    bytes.map(b => SmtChar(b.toInt & 0xFF).toChar).mkString("")
  }
}
