---
sidebar_position: 7
---

# 乱数

乱数生成処理はメモリ上の `0x009643` に存在している。単純なLFSRやLCGではない模様。

状態を3byte (`0x7E0ED7` 〜 `0x7E0ED9`) 、生成された乱数を1byte (`0x7E0ED5`) 保持している。
初期値 (つまりシード) は `0x7E0ED5` に `0x7FFFFF` の値が、それ以外には `0x00` が設定される。

scalaでの再現実装は以下のようになる (以下の `d5` が新規に生成される乱数)。

```scala
type Prng = State[(Int, Int, Int, Int), Int]

def prng: Prng = State(state0 => {
  val (d50, d70, d80, d90) = state0

  val p = d70 & 0x03
  val init = p == 0x00 || p == 0x03

  val state =
    (for {
      d9 <- rol(d90)
      d7 <- rol(d70)
      q  <- adc(d7, 0x22)
      r  <- adc(q, d9)
      d8 <- adc(r ^ 0x5A, d80)
      d5 = d8 ^ d50
    } yield {
      (d5, d7, d8, d9)
    }).runA(init).value

  (state, state._1)
})
```

完全なコードは [scripts/prng.sc](https://github.com/akiomik/smt1-note/blob/main/scripts/prng.sc) を参照のこと。
