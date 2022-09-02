case class SmtChar(code: Int) {
  def toChar(): Char = {
    code match {
      case 0x00 => '＿'
      case _    if code >= 0x01 && code <= 0x0A => (code - 1 + 0xFF10).toChar
      case _    if code >= 0x0B && code <= 0x24 => (code - 11 + 0xFF21).toChar
      case _    if code >= 0x25 && code <= 0x29 => Seq('あ', 'い', 'う', 'え', 'お')(code - 0x25)
      case _    if code >= 0x2A && code <= 0x2E => Seq('か', 'き', 'く', 'け', 'こ')(code - 0x2A)
      case _    if code >= 0x2F && code <= 0x33 => Seq('さ', 'し', 'す', 'せ', 'そ')(code - 0x2F)
      case _    if code >= 0x34 && code <= 0x38 => Seq('た', 'ち', 'つ', 'て', 'と')(code - 0x34)
      case _    if code >= 0x39 && code <= 0x3D => Seq('な', 'に', 'ぬ', 'ね', 'の')(code - 0x39)
      case _    if code >= 0x3E && code <= 0x42 => Seq('は', 'ひ', 'ふ', 'へ', 'ほ')(code - 0x3E)
      case _    if code >= 0x43 && code <= 0x47 => Seq('ま', 'み', 'む', 'め', 'も')(code - 0x43)
      case _    if code >= 0x48 && code <= 0x4A => Seq('や', 'ゆ', 'よ')(code - 0x48)
      case _    if code >= 0x4B && code <= 0x4F => Seq('ら', 'り', 'る', 'れ', 'ろ')(code - 0x4B)
      case _    if code >= 0x50 && code <= 0x52 => Seq('わ', 'を', 'ん')(code - 0x50)
      case _    if code >= 0x53 && code <= 0x59 => Seq('ぁ', 'ぉ', 'ゃ', 'ゅ', 'ょ', 'っ', 'ぅ')(code - 0x53)
      case 0x5A => 'ー'
      case 0x5B => '￥'
      case 0x5C => 'ｈ' // マッカ
      case _    if code >= 0x5D && code <= 0x61 => Seq('ア', 'イ', 'ウ', 'エ', 'オ')(code - 0x5D)
      case _    if code >= 0x62 && code <= 0x66 => Seq('カ', 'キ', 'ク', 'ケ', 'コ')(code - 0x62)
      case _    if code >= 0x67 && code <= 0x6B => Seq('サ', 'シ', 'ス', 'セ', 'ソ')(code - 0x67)
      case _    if code >= 0x6C && code <= 0x70 => Seq('タ', 'チ', 'ツ', 'テ', 'ト')(code - 0x6C)
      case _    if code >= 0x71 && code <= 0x75 => Seq('ナ', 'ニ', 'ヌ', 'ネ', 'ノ')(code - 0x71)
      case _    if code >= 0x76 && code <= 0x7A => Seq('ハ', 'ヒ', 'フ', 'ヘ', 'ホ')(code - 0x76)
      case _    if code >= 0x7B && code <= 0x7F => Seq('マ', 'ミ', 'ム', 'メ', 'モ')(code - 0x7B)
      case _    if code >= 0x80 && code <= 0x82 => Seq('ヤ', 'ユ', 'ヨ')(code - 0x80)
      case _    if code >= 0x83 && code <= 0x87 => Seq('ラ', 'リ', 'ル', 'レ', 'ロ')(code - 0x83)
      case _    if code >= 0x88 && code <= 0x8A => Seq('ワ', 'ヲ', 'ン')(code - 0x88)
      case _    if code >= 0x8B && code <= 0x92 => Seq('ァ', 'ィ', 'ェ', 'ォ', 'ャ', 'ュ', 'ョ', 'ッ')(code - 0x8B)
      case 0x93 => '・'
      case 0x94 => '！'
      case 0x95 => '？'
      case 0x96 => '＆'
      case 0x97 => '＞'
      case 0x98 => '／'
      case 0x99 => '＇'
      case 0x9A => '：'
      case 0x9B => '．'
      case _    if code >= 0x9C && code <= 0xA0 => Seq('が', 'ぎ', 'ぐ', 'げ', 'ご')(code - 0x9C)
      case _    if code >= 0xA1 && code <= 0xA5 => Seq('ざ', 'じ', 'ず', 'ぜ', 'ぞ')(code - 0xA1)
      case _    if code >= 0xA6 && code <= 0xAA => Seq('だ', 'ぢ', 'づ', 'で', 'ど')(code - 0xA6)
      case _    if code >= 0xAB && code <= 0xAF => Seq('ば', 'び', 'ぶ', 'べ', 'ぼ')(code - 0xAB)
      case _    if code >= 0xB0 && code <= 0xB4 => Seq('ぱ', 'ぴ', 'ぷ', 'ぺ', 'ぽ')(code - 0xB0)
      case _    if code >= 0xB5 && code <= 0xB9 => Seq('ガ', 'ギ', 'グ', 'ゲ', 'ゴ')(code - 0xB5)
      case _    if code >= 0xBA && code <= 0xBE => Seq('ザ', 'ジ', 'ズ', 'ゼ', 'ゾ')(code - 0xBA)
      case _    if code >= 0xBF && code <= 0xC3 => Seq('ダ', 'ヂ', 'ヅ', 'デ', 'ド')(code - 0xBF)
      case _    if code >= 0xC4 && code <= 0xC8 => Seq('バ', 'ビ', 'ブ', 'べ', 'ボ')(code - 0xC4)
      case _    if code >= 0xC9 && code <= 0xCD => Seq('パ', 'ピ', 'プ', 'ペ', 'ポ')(code - 0xC9)
      case 0xCF => '　'
      case _    => '＿'
    }
  }
}

object SmtChar {
  def decodeHexString(hex: String): SmtChar = {
    SmtChar(Integer.parseInt(hex, 16))
  }
}
