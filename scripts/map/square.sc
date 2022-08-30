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
