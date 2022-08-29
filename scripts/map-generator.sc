import $ivy.`org.creativescala::doodle:0.10.1 compat`

import scala.io.Source

import cats.effect.unsafe.implicits.global
import doodle.core._
import doodle.effect.Writer._
import doodle.image._
import doodle.image.syntax.all._
import doodle.java2d._

case class Square(value: Int) {
  val flags = value >> 8
  val event = value & 0xFF

  def hasNorthDoor     = (flags & 0x80) == 0x80
  def hasNorthWall     = (flags & 0x40) == 0x40
  def hasNorthFakeWall = hasNorthDoor && !hasNorthWall
  def hasEastDoor      = (flags & 0x20) == 0x20
  def hasEastWall      = (flags & 0x10) == 0x10
  def hasEastFakeWall  = hasEastDoor && !hasEastWall
  def hasSouthDoor     = (flags & 0x08) == 0x08
  def hasSouthWall     = (flags & 0x04) == 0x04
  def hasSouthFakeWall = hasSouthDoor && !hasSouthWall
  def hasWestDoor      = (flags & 0x02) == 0x02
  def hasWestWall      = (flags & 0x01) == 0x01
  def hasWestFakeWall  = hasWestDoor && !hasWestWall

  def isRotatingFloor = (event & 0x0F) == 0x01
  def isPoisonFloor   = (event & 0x0F) == 0x02
  def isDamageFloorLN = (event & 0x0F) == 0x03
  def isDamageFloorNC = (event & 0x0F) == 0x04
  def isChute         = (event & 0x0F) == 0x05
  def isTeleport      = (event & 0x0F) == 0x06
  def isExit          = (event & 0x0F) == 0x07
  def isUpStairs      = (event & 0x0F) == 0x08
  def isDownStairs    = (event & 0x0F) == 0x09
  def hasSign         = (event & 0x0F) == 0x0A
  def hasMessage      = (event & 0x0F) == 0x0B
  def isElevator      = (event & 0x0F) == 0x0C
  def isEvent1        = (event & 0x0F) == 0x0D
  def isEvent2        = (event & 0x0F) == 0x0E
  def isUnknown       = (event & 0x0F) == 0x0F
  def isInaccessible  = (event & 0x30) == 0x30
  def hasChest        = (event & 0x40) == 0x40
  def isDarkness      = (event & 0x80) == 0x80
}

object Square {
  def fromHexString(hex: String): Square = Square(Integer.parseInt(hex, 16))
}

def createTopWallImage(width: Double, height: Double, offset: Double): Image = {
  Image.openPath(List(
    PathElement.moveTo(0 + offset, height - offset),
    PathElement.lineTo(width - offset, height - offset),
  ))
}

def createTopDoorImage(width: Double, height: Double, offset: Double, door: Double): Image = {
  Image.openPath(List(
    PathElement.moveTo(0 + offset, height - offset),
    PathElement.lineTo((width/2) - (door/2), height - offset),
    PathElement.moveTo((width/2) + (door/2), height - offset),
    PathElement.lineTo(width - offset, height - offset),
  ))
}

def createRightWall(width: Double, height: Double, offset: Double): Image = {
  Image.openPath(List(
    PathElement.moveTo(width - offset, height - offset),
    PathElement.lineTo(width - offset, 0 + offset),
  ))
}

def createRightDoorImage(width: Double, height: Double, offset: Double, door: Double): Image = {
  Image.openPath(List(
    PathElement.moveTo(width - offset, height - offset),
    PathElement.lineTo(width - offset, (height/2) + (door/2)),
    PathElement.moveTo(width - offset, (height/2) - (door/2)),
    PathElement.lineTo(width - offset, 0 + offset),
  ))
}

def createBottomWallImage(width: Double, height: Double, offset: Double): Image = {
  Image.openPath(List(
    PathElement.moveTo(0 + offset, 0 + offset),
    PathElement.lineTo(width - offset, 0 + offset),
  ))
}

def createBottomDoorImage(width: Double, height: Double, offset: Double, door: Double): Image = {
  Image.openPath(List(
    PathElement.moveTo(0 + offset, 0 + offset),
    PathElement.lineTo((width/2) - (door/2), 0 + offset),
    PathElement.moveTo((width/2) + (door/2), 0 + offset),
    PathElement.lineTo(width - offset, 0 + offset),
  ))
}

def createLeftWallImage(width: Double, height: Double, offset: Double): Image = {
  Image.openPath(List(
    PathElement.moveTo(0 + offset, height - offset),
    PathElement.lineTo(0 + offset, 0 + offset),
  ))
}

def createLeftDoorImage(width: Double, height: Double, offset: Double, door: Double): Image = {
  Image.openPath(List(
    PathElement.moveTo(0 + offset, height - offset),
    PathElement.lineTo(0 + offset, (height/2) + (door/2)),
    PathElement.moveTo(0 + offset, (height/2) - (door/2)),
    PathElement.lineTo(0 + offset, 0 + offset),
  ))
}

def createCircleSymbolImage(size: Double, scale: Double = 0.4, borderColor: Color = Color.black, fillColor: Color = Color.white): Image = {
  Image.circle(size * scale).strokeColor(borderColor).fillColor(fillColor)
}

def createSquareImage(square: Square, width: Double = 10, height: Double = 10, offset: Double = 0, door: Double = 4): Image = {
  val bgColor =
    if (square.isDarkness) { Color.gray }
    else if (square.isPoisonFloor) { Color.purple }
    else if (square.isDamageFloorNC) { Color.blue }
    else if (square.isDamageFloorLN) { Color.red }
    else if (square.isInaccessible) { Color.black }
    else { Color.white }
  val fgColor = Color.black
  val fakeColor = Color.gray
  var im = Image.square(width).noStroke.fillColor(bgColor)

  // NOTE: 左下が原点で+xは右、+yは上
  // NOTE: Image#underは元画像の中心を(0, 0)に設定する
  // NOTE: 線を描く場合と描かない場合とでimの幅が変わってしまうため
  //       必ず線を描くことでimの幅が一定になるようにしている

  // 上
  val top = if (square.hasNorthDoor) { createTopDoorImage(width, height, offset, door) }
            else { createTopWallImage(width, height, offset) }
  val topColor = if (square.hasNorthFakeWall) { fakeColor } else if (square.hasNorthWall) { fgColor } else { bgColor }
  im = im.under(top.strokeColor(topColor).at(-width/2, -height/2))

  // 右
  val right = if (square.hasEastDoor) { createRightDoorImage(width, height, offset, door) }
              else { createRightWall(width, height, offset) }
  val rightColor = if (square.hasEastFakeWall) { fakeColor } else if (square.hasEastWall) { fgColor } else { bgColor }
  im = im.under(right.strokeColor(rightColor).at(-width/2, -height/2))

  // 下
  val bottom = if (square.hasSouthDoor) { createBottomDoorImage(width, height, offset, door) }
               else { createBottomWallImage(width, height, offset) }
  val bottomColor = if (square.hasSouthFakeWall) { fakeColor } else if (square.hasSouthWall) { fgColor } else { bgColor }
  im = im.under(bottom.strokeColor(bottomColor).at(-width/2, -height/2))

  // 左
  val left = if (square.hasWestDoor) { createLeftDoorImage(width, height, offset, door) }
             else { createLeftWallImage(width, height, offset) }
  val leftColor = if (square.hasWestFakeWall) { fakeColor } else if (square.hasWestWall) { fgColor } else { bgColor }
  im = im.under(left.strokeColor(leftColor).at(-width/2, -height/2))

  if (square.isExit) {
    im = im.under(createCircleSymbolImage(width, borderColor = fgColor, fillColor = Color.red))
  }

  if (square.isChute) {
    im = im.under(createCircleSymbolImage(width, borderColor = fgColor, fillColor = Color.black))
  }

  im
}

def createRowImage(line: Seq[String]): Image = {
  line.foldLeft(None: Option[Image]) { (acc, hex) =>
    val square = Square.fromHexString(hex)
    acc match {
      case Some(im) => Some(im.beside(createSquareImage(square)))
      case None => Some(createSquareImage(square))
    }
  }.get
}

def createMapImage(lines: Seq[Seq[String]]): Image = {
  lines.foldLeft(None: Option[Image]) { (acc, line) =>
    acc match {
      case Some(im) => Some(im.above(createRowImage(line)))
      case None => Some(createRowImage(line))
    }
  }.get
}

val path = os.pwd/'data/"map.txt"
val dataFile = Source.fromFile(path.toString)
val lines = dataFile.getLines.map(_.split(" ").toSeq).toSeq
dataFile.close()

val map = createMapImage(lines)
// map.draw()
map.write[Png]("map.png")
