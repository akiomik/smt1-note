# smt1-note

真・女神転生 (Shin Megami Tensei) の日本語版SFCについてのデータをまとめています。

## 文字

文字コード一覧。未使用の場合は `0xFF` が割り当てられる。

|      |  0  |  1  |  2  |  3  |  4  |  5  |  6  |  7  |  8  |  9  |  A  |  B  |  C  |  D  |  E  |  F  |
|------|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|
| 0x00 | N/A |  ０ |  １ |  ２ |  ３ |  ４ |  ５ |  ６ |  ７ |  ８ |  ９ |  Ａ |  Ｂ |  Ｃ |  Ｄ |  Ｅ |
| 0x10 |  Ｆ |  Ｇ |  Ｈ |  Ｉ |  Ｊ |  Ｋ |  Ｌ |  Ｍ |  Ｎ |  Ｏ |  Ｐ |  Ｑ |  Ｒ |  Ｓ |  Ｔ |  Ｕ |
| 0x20 |  Ｖ |  Ｗ |  Ｘ |  Ｙ |  Ｚ |  あ |  い |  う |  え |  お |  か |  き |  く |  け |  こ |  さ |
| 0x30 |  し |  す |  せ |  そ |  た |  ち |  つ |  て |  と |  な |  に |  ぬ |  ね |  の |  は |  ひ |
| 0x40 |  ふ |  へ |  ほ |  ま |  み |  む |  め |  も |  や |  ゆ |  よ |  ら |  り |  る |  れ |  ろ |
| 0x50 |  わ |  を |  ん |  ぁ |  ぉ |  ゃ |  ゅ |  ょ |  っ |  ぅ |  ー |  ￥ |  ♄  |  ア |  イ |  ウ |
| 0x60 |  エ |  オ |  カ |  キ |  ク |  ケ |  コ |  サ |  シ |  ス |  セ |  ソ |  タ |  チ |  ツ |  テ |
| 0x70 |  ト |  ナ |  ニ |  ヌ |  ネ |  ノ |  ハ |  ヒ |  フ |  ヘ |  ホ |  マ |  ミ |  ム |  メ |  モ |
| 0x80 |  ヤ |  ユ |  ヨ |  ラ |  リ |  ル |  レ |  ロ |  ワ |  ヲ |  ン |  ァ |  ィ |  ェ |  ォ |  ャ |
| 0x90 |  ュ |  ョ |  ッ |  ・ |  ！ |  ？ |  ＆ |  ＞ |  ／ |  ＇ |  ： |  ． |  が |  ぎ |  ぐ |  げ |
| 0xA0 |  ご |  ざ |  じ |  ず |  ぜ |  ぞ |  だ |  ぢ |  づ |  で |  ど |  ば |  び |  ぶ |  べ |  ぼ |
| 0xB0 |  ぱ |  ぴ |  ぷ |  ぺ |  ぽ |  ガ |  ギ |  グ |  ゲ |  ゴ |  ザ |  ジ |  ズ |  ゼ |  ゾ |  ダ |
| 0xC0 |  ヂ |  ヅ |  デ |  ド |  バ |  ビ |  ブ |  べ |  ボ |  パ |  ピ |  プ |  ペ |  ポ |  ヴ | 空白|

## マップ

### 3Dダンジョン

メモリ上の3Dダンジョンデータをマップジェネレータ ([scripts/map-generator.sc](scripts/map-generator.sc)) により画像化したもの。

![3d-map](https://github.com/akiomik/smt1-note/blob/main/map.png?raw=true)
![3d-map-with-annotation](https://github.com/akiomik/smt1-note/blob/main/map-annotated.png?raw=true)

### 3Dダンジョンのデータ

3Dダンジョンのデータはメモリ上の `0x048000` 〜 `0x4BFFF` の範囲。
各マスは壁データ1 byteとイベントデータ1byteの合計2 byteで表現され、以下のようなデータ構造を持っている。

<table>
  <tbody>
    <tr>
      <td colspan="8">byte 0 (壁)</td>
      <td colspan="8">byte 1 (イベント)</td>
    </tr>
    <tr>
      <td colspan="2">北</td>
      <td colspan="2">東</td>
      <td colspan="2">南</td>
      <td colspan="2">西</td>
      <td>暗闇</td>
      <td>宝箱</td>
      <td colspan="2">アクセス不可</td>
      <td colspan="4">イベント</td>
    </tr>
    <tr>
      <td>7</td>
      <td>6</td>
      <td>5</td>
      <td>4</td>
      <td>3</td>
      <td>2</td>
      <td>1</td>
      <td>0</td>
      <td>7</td>
      <td>6</td>
      <td>5</td>
      <td>4</td>
      <td>3</td>
      <td>2</td>
      <td>1</td>
      <td>0</td>
    </tr>
  </tbody>
</table>

壁データは北・東・南・西と2bitごとに分かれており、値により以下の意味を持つ。

- `00`: なし
- `01`: 壁 (黒線)
- `10`: 隠しドア (灰色破線)
- `11`: ドア (黒破線)

イベントデータは上位4bitがフラグになっており、下位4bitとの組み合わせによってそのマスで発生するイベントを表している。

- `0x00`: なし
- `0x01`: 回転床
- `0x02`: 毒床 (紫)
- `0x03`: ダメージ床 (Law, Neutralのみ対象。赤)
- `0x04`: ダメージ床 (Neutral, Chaosのみ対象。青)
- `0x05`: 落とし穴 (黒丸)
- `0x06`: ワープ
- `0x07`: 出入り口 (赤丸)
- `0x08`: 上り階段
- `0x09`: 下り階段
- `0x0A`: 看板
- `0x0B`: メッセージ
- `0x0C`: エレベータ
- `0x0D`: イベント
- `0x0E`: イベント
- `0x0F`: 不明
- `0x30`: アクセス不可 (黒)
- `0x40`: 宝箱
- `0x80`: 暗闇 (灰色)

## スクリプト

### マップジェネレータ

[Ammonite](https://ammonite.io) が必要。また、 [doodle](https://www.creativescala.org/doodle/) の [未リリースのコミット](https://github.com/creativescala/doodle/commit/efc3371fbaf3277dcd9a90af5d49f94f115c269e) に依存しているため、事前に `publishLocal` しておく必要がある。

以下のコマンドで `map.png` を生成する。

```bash
# Generates map.png
amm scripts/map-generator.sc
```

### 文字デコーダ

[Ammonite](https://ammonite.io) が必要。

16進数でエンコードされた文字コードを受け取ってUTF-8にデコードした文字を出力する。

```bash
# Decodes characters from hex string
amm scripts/char-decoder.sc --str 668AB9707F

# Decodes characters from binary file
amm scripts/char-decoder.sc --file text.bin
```

## リソース

### 逆アセンブリ

- https://github.com/spannerisms/smt1dasm
