// NOTE: This is local published version of https://github.com/creativescala/doodle/commit/5b59a0733ff731a0f358e468c52eb6be0410d162
import $ivy.`org.creativescala::doodle:0.11.2-CUSTOM compat`

import $file.square

import doodle.core.font.Font

import square.SquareImageGeneratorConfig

case class MapImageGeneratorConfig(
  font: Font,
  squareImageGeneratorConfig: SquareImageGeneratorConfig,
)
