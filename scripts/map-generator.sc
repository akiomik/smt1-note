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

  def hasTopDoor    = (flags & 0x80) == 0x80
  def hasTopWall    = (flags & 0x40) == 0x40
  def hasRightDoor  = (flags & 0x20) == 0x20
  def hasRightWall  = (flags & 0x10) == 0x10
  def hasBottomDoor = (flags & 0x08) == 0x08
  def hasBottomWall = (flags & 0x04) == 0x04
  def hasLeftDoor   = (flags & 0x02) == 0x02
  def hasLeftWall   = (flags & 0x01) == 0x01

  def isExit       = event == 0x07
  def isUpstairs   = event == 0x08
  def isDownstairs = event == 0x09
  def isTalk       = event == 0x0B
  def isElevator   = event == 0x0C
  def isEvent      = event == 0x0E
  def isUndefined  = event == 0x30
  def isItem       = event == 0x40
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

def createSquareImage(square: Square, width: Double = 10, height: Double = 10, offset: Double = 0, door: Double = 4): Image = {
  val bgColor = if (square.isUndefined) { Color.black } else { Color.white }
  val fgColor = Color.black
  var im = Image.square(width).noStroke.fillColor(bgColor)

  // NOTE: 左下が原点で+xは右、+yは上
  // NOTE: Image#underは元画像の中心を(0, 0)に設定する
  // NOTE: 線を描く場合と描かない場合とでimの幅が変わってしまうため
  //       必ず線を描くことでimの幅が一定になるようにしている

  // 上
  val top = if (square.hasTopDoor) { createTopDoorImage(width, height, offset, door) }
            else { createTopWallImage(width, height, offset) }
  val topColor = if (square.hasTopWall) { fgColor } else { bgColor }
  im = im.under(top.strokeColor(topColor).at(-width/2, -height/2))

  // 右
  val right = if (square.hasRightDoor) { createRightDoorImage(width, height, offset, door) }
              else { createRightWall(width, height, offset) }
  val rightColor = if (square.hasRightWall) { fgColor } else { bgColor }
  im = im.under(right.strokeColor(rightColor).at(-width/2, -height/2))

  // 下
  val bottom = if (square.hasBottomDoor) { createBottomDoorImage(width, height, offset, door) }
               else { createBottomWallImage(width, height, offset) }
  val bottomColor = if (square.hasBottomWall) { fgColor } else { bgColor }
  im = im.under(bottom.strokeColor(bottomColor).at(-width/2, -height/2))

  // 左
  val left = if (square.hasLeftDoor) { createLeftDoorImage(width, height, offset, door) }
             else { createLeftWallImage(width, height, offset) }
  val leftColor = if (square.hasLeftWall) { fgColor } else { bgColor }
  im = im.under(left.strokeColor(leftColor).at(-width/2, -height/2))

  if (square.isExit) {
    im = im.under(Image.circle(width * 0.4).strokeColor(fgColor).fillColor(Color.red))
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
