import $file.char.encoder

import os.Path

import encoder.CharEncoder

@main
def main(str: Option[String]) = {
  str match {
    case Some(s) => println(CharEncoder.encodeFromString(s))
    case _       => throw new Error("argument is missing")
  }
}
