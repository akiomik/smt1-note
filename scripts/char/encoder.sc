import $file.char

import java.nio.file.{Files, Paths}
import os.Path
import scala.io.Source

import char.SmtChar

object CharEncoder {
  def encodeFromString(str: String): String =
    str.map(char => SmtChar.fromChar(char).map(_.toHexString).getOrElse("??")).mkString
}
