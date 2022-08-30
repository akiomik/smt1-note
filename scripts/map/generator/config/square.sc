// NOTE: This is local published version of https://github.com/creativescala/doodle/commit/5b59a0733ff731a0f358e468c52eb6be0410d162
import $ivy.`org.creativescala::doodle:0.11.2-CUSTOM compat`

import doodle.core.Color
import doodle.core.font.Font

case class SquareImageGeneratorConfig(
  width: Double,
  height: Double,
  offset: Double,
  door: Double,
  symbolScale: Double,
  bgColor: Color,
  fgColor: Color,
  fakeColor: Color,
  chuteColor: Color,
  exitColor: Color,
  poisonColor: Color,
  damageFloorNCColor: Color,
  damageFloorLNColor: Color,
  inaccessibleColor: Color,
  darknessColor: Color,
  font: Font,
)
