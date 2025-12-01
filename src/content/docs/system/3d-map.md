---
title: 3Dダンジョン
description: 3Dダンジョンのマップデータとデータ構造について
---

## マップ {#map}

3Dダンジョンデータをマップジェネレータ ([scripts/map-generator.sc](https://github.com/akiomik/smt1-note/blob/main/scripts/map-generator.sc)) により画像化したもの。
アイコン等の意味は [データ構造](#data-structure) を参照のこと。

[![3d-map](/img/map.png)](/img/map.png)

## 注釈付きマップ {#map-with-annotation}

マップジェネレータにより生成した画像にエリア名の注釈を加えたもの。

[![3d-map-with-annotation](/img/map-annotated.png)](/img/map-annotated.png)

## データ構造 {#data-structure}

3Dダンジョンのデータは `0x048000` 〜 `0x4BFFF` に格納されている。
各マスは壁データ1 byteとイベントデータ1byteの合計2 byteで表現され、以下のようなデータ構造を持っている。

<table>
  <tbody>
    <tr>
      <td colSpan="8">byte 0 (壁)</td>
      <td colSpan="8">byte 1 (イベント)</td>
    </tr>
    <tr>
      <td colSpan="2">北</td>
      <td colSpan="2">東</td>
      <td colSpan="2">南</td>
      <td colSpan="2">西</td>
      <td>暗闇</td>
      <td>宝箱</td>
      <td colSpan="2">アクセス不可</td>
      <td colSpan="4">イベント</td>
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

- `0b00`: なし
- `0b01`: 壁 (黒線)
- `0b10`: 隠しドア (灰色破線)
- `0b11`: ドア (黒破線)

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
