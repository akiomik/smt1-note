// NOTE: This is local published version of https://github.com/creativescala/doodle/commit/5b59a0733ff731a0f358e468c52eb6be0410d162
import $ivy.`org.creativescala::doodle:0.11.2-CUSTOM compat`

import $file.^.{square => data}
import $file.config.{square => config}

import doodle.core.{Color, PathElement}
import doodle.image.Image

import config.SquareImageGeneratorConfig
import data.Square

class SquareImageGenerator(config: SquareImageGeneratorConfig) {
  private def createTopWallImage(): Image = {
    Image.openPath(List(
      PathElement.moveTo(0 + config.offset, config.height - config.offset),
      PathElement.lineTo(config.width - config.offset, config.height - config.offset),
    ))
  }

  private def createTopDoorImage(): Image = {
    Image.openPath(List(
      PathElement.moveTo(0 + config.offset, config.height - config.offset),
      PathElement.lineTo((config.width/2) - (config.door/2), config.height - config.offset),
      PathElement.moveTo((config.width/2) + (config.door/2), config.height - config.offset),
      PathElement.lineTo(config.width - config.offset, config.height - config.offset),
    ))
  }

  private def createRightWall(): Image = {
    Image.openPath(List(
      PathElement.moveTo(config.width - config.offset, config.height - config.offset),
      PathElement.lineTo(config.width - config.offset, 0 + config.offset),
    ))
  }

  private def createRightDoorImage(): Image = {
    Image.openPath(List(
      PathElement.moveTo(config.width - config.offset, config.height - config.offset),
      PathElement.lineTo(config.width - config.offset, (config.height/2) + (config.door/2)),
      PathElement.moveTo(config.width - config.offset, (config.height/2) - (config.door/2)),
      PathElement.lineTo(config.width - config.offset, 0 + config.offset),
    ))
  }

  private def createBottomWallImage(): Image = {
    Image.openPath(List(
      PathElement.moveTo(0 + config.offset, 0 + config.offset),
      PathElement.lineTo(config.width - config.offset, 0 + config.offset),
    ))
  }

  private def createBottomDoorImage(): Image = {
    Image.openPath(List(
      PathElement.moveTo(0 + config.offset, 0 + config.offset),
      PathElement.lineTo((config.width/2) - (config.door/2), 0 + config.offset),
      PathElement.moveTo((config.width/2) + (config.door/2), 0 + config.offset),
      PathElement.lineTo(config.width - config.offset, 0 + config.offset),
    ))
  }

  private def createLeftWallImage(): Image = {
    Image.openPath(List(
      PathElement.moveTo(0 + config.offset, config.height - config.offset),
      PathElement.lineTo(0 + config.offset, 0 + config.offset),
    ))
  }

  private def createLeftDoorImage(): Image = {
    Image.openPath(List(
      PathElement.moveTo(0 + config.offset, config.height - config.offset),
      PathElement.lineTo(0 + config.offset, (config.height/2) + (config.door/2)),
      PathElement.moveTo(0 + config.offset, (config.height/2) - (config.door/2)),
      PathElement.lineTo(0 + config.offset, 0 + config.offset),
    ))
  }

  private def createCircleSymbolImage(color: Color): Image = {
    Image.circle(config.width * config.symbolScale).strokeColor(config.fgColor).fillColor(color)
  }

  def create(square: Square): Image = {
    val bgColor =
      if (square.isDarkness) { config.darknessColor }
      else if (square.isPoisonFloor) { config.poisonColor }
      else if (square.isDamageFloorNC) { config.damageFloorNCColor }
      else if (square.isDamageFloorLN) { config.damageFloorLNColor }
      else if (square.isInaccessible) { config.inaccessibleColor }
      else { config.bgColor }
    var im = Image.square(config.width).noStroke.fillColor(bgColor)

    // NOTE: 左下が原点で+xは右、+yは上
    // NOTE: Image#underは元画像の中心を(0, 0)に設定する
    // NOTE: 線を描く場合と描かない場合とでimの幅が変わってしまうため
    //       必ず線を描くことでimの幅が一定になるようにしている

    // 上
    val top = if (square.hasNorthDoor) { createTopDoorImage }
              else { createTopWallImage }
    val topColor = if (square.hasNorthFakeWall) { config.fakeColor } else if (square.hasNorthWall) { config.fgColor } else { bgColor }
    im = im.under(top.strokeColor(topColor).at(-config.width/2, -config.height/2))

    // 右
    val right = if (square.hasEastDoor) { createRightDoorImage }
                else { createRightWall }
    val rightColor = if (square.hasEastFakeWall) { config.fakeColor } else if (square.hasEastWall) { config.fgColor } else { bgColor }
    im = im.under(right.strokeColor(rightColor).at(-config.width/2, -config.height/2))

    // 下
    val bottom = if (square.hasSouthDoor) { createBottomDoorImage }
                 else { createBottomWallImage }
    val bottomColor = if (square.hasSouthFakeWall) { config.fakeColor } else if (square.hasSouthWall) { config.fgColor } else { bgColor }
    im = im.under(bottom.strokeColor(bottomColor).at(-config.width/2, -config.height/2))

    // 左
    val left = if (square.hasWestDoor) { createLeftDoorImage }
               else { createLeftWallImage }
    val leftColor = if (square.hasWestFakeWall) { config.fakeColor } else if (square.hasWestWall) { config.fgColor } else { bgColor }
    im = im.under(left.strokeColor(leftColor).at(-config.width/2, -config.height/2))

    if (square.isChute) {
      im = im.under(createCircleSymbolImage(config.chuteColor))
    }

    if (square.isExit) {
      im = im.under(createCircleSymbolImage(config.exitColor))
    }

    if (square.isUpStairs) {
      im = im.under(Image.text("U").font(config.font))
    }

    if (square.isDownStairs) {
      im = im.under(Image.text("D").font(config.font))
    }

    if (square.isElevator) {
      im = im.under(Image.text("E").font(config.font))
    }

    im
  }

  def createBlank(): Image = {
    create(Square.fromHexString("00"))
  }
}
