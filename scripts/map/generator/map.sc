// NOTE: This is local published version of https://github.com/creativescala/doodle/commit/5b59a0733ff731a0f358e468c52eb6be0410d162
import $ivy.`org.creativescala::doodle:0.11.2-CUSTOM compat`

import $file.^.{square => data}
import $file.config.{map => config}
import $file.square

import doodle.image.Image

import data.Square
import square.SquareImageGenerator
import config.MapImageGeneratorConfig

class MapImageGenerator(config: MapImageGeneratorConfig) {
  val squareImageGenerator = new SquareImageGenerator(config.squareImageGeneratorConfig)

  private def toHexIndex(i: Int): String = {
    val index = i.toHexString.toUpperCase
    if (index.size == 1) {
      s"0${index}"
    } else {
      index
    }
  }

  private def createRow(line: Seq[Square]): Image = {
    line.foldLeft(None: Option[Image]) { (acc, square) =>
      acc match {
        case Some(im) => Some(im.beside(squareImageGenerator.create(square)))
        case None     => Some(squareImageGenerator.create(square))
      }
    }.get
  }

  private def createColumnIndex(size: Int): Image = {
    val bg = squareImageGenerator.createBlank

    (0 until size).foldLeft(None: Option[Image]) { (acc, i) =>
      val im = Image.text(toHexIndex(i)).font(config.font).on(bg)
      acc match {
        case Some(im0) => Some(im0.beside(im))
        case None      => Some(im)
      }
    }.get
  }

  private def createRowIndex(size: Int): Image = {
    val bg = squareImageGenerator.createBlank

    (0 until size).foldLeft(None: Option[Image]) { (acc, i) =>
      val im = Image.text(toHexIndex(i)).font(config.font).on(bg)
      acc match {
        case Some(im0) => Some(im0.above(im))
        case None      => Some(im)
      }
    }.get
  }

  def create(lines: Seq[Seq[Square]]): Image = {
    lines.foldLeft(None: Option[Image]) { (acc, line) =>
      acc match {
        case Some(im) => Some(im.above(createRow(line)))
        case None     => Some(createRow(line))
      }
    }.get
  }

  def createWithIndex(lines: Seq[Seq[Square]]): Image = {
    val space = squareImageGenerator.createBlank
    val rowIndex = createRowIndex(lines.size)
    val colIndex = createColumnIndex(lines.head.size)
    val map = create(lines)

    (space.above(rowIndex)).beside(colIndex.above(map))
  }
}
