import $file.char.decoder

import os.Path

import decoder.CharDecoder

@main
def main(str: Option[String], file: Option[Path]) = {
  (str, file) match {
    case (Some(s), _) => println(CharDecoder.decodeFromString(s))
    case (_, Some(p)) => println(CharDecoder.decodeFromFile(p))
    case _            => throw new Error("argument is missing")
  }
}
