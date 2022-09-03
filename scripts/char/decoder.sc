import $file.char

import java.nio.file.{Files, Paths}
import os.Path
import scala.io.Source

import char.SmtChar

object CharDecoder {
  private def parseString(str: String): Seq[String] = str.toCharArray.sliding(2, 2).toSeq.map(_.mkString)

  def decodeFromString(str: String): String =
    parseString(str).map(hex => SmtChar.decodeHexString(hex).map(_.toChar).getOrElse('＿')).mkString

  def decodeFromFile(path: Path): String = {
    val bytes = Files.readAllBytes(Paths.get(path.toString))
    bytes.map { b => SmtChar.fromByte(b).map(_.toChar).getOrElse('＿') }.mkString("")
  }
}
