import $file.map.generator.{map => mapGenerator}
import $file.map.generator.config.{map => mapConfig}
import $file.map.generator.config.{square => squareConfig}
import $file.map.parser

import cats.effect.unsafe.implicits.global
import doodle.core.Color
import doodle.core.font.Font
import doodle.effect.Writer._
import doodle.image.syntax.all._
import doodle.java2d._

import mapGenerator.MapImageGenerator
import mapConfig.MapImageGeneratorConfig
import parser.MapParser
import squareConfig.SquareImageGeneratorConfig

val data = MapParser.parse(os.pwd/'data/"map.txt")

val font = Font.defaultSansSerif.size(7)
val config = MapImageGeneratorConfig(
  font = font,
  squareImageGeneratorConfig = SquareImageGeneratorConfig(
    width = 10,
    height = 10,
    offset = 0,
    door = 4,
    symbolScale = 0.4,
    bgColor = Color.white,
    fgColor = Color.black,
    fakeColor = Color.gray,
    chuteColor = Color.black,
    exitColor = Color.red,
    poisonColor = Color.purple,
    damageFloorNCColor = Color.blue,
    damageFloorLNColor = Color.red,
    inaccessibleColor = Color.black,
    darknessColor = Color.gray,
    font = font,
  )
)

val generator = new MapImageGenerator(config)
val map = generator.createWithIndex(data)
// map.draw()
map.write[Png]("map.png")
