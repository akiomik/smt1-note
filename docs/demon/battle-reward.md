---
sidebar_position: 2
---

# 戦闘報酬

## 基本経験値の計算 {#base-exp}

[悪魔一覧表](/docs/demon/#normal-demon) のうち、「経験値&ドロップ率」列の上位4bitが経験値係数。

$L$ をレベル、 $x$ を符号付き4bit整数で表現された経験値係数としたとき、

$$
f_{exp}(L, x) =
\begin{cases}
4Lx & (x \geq 0)\\
\lfloor L / 2 \rfloor (x + 9) & (x < 0)
\end{cases}
$$

が基本経験値となる。

例えば超人ドウマンの場合、レベル15、「経験値&ドロップ率」の上位4bitが `0x4` (10進数で4) となっているため、
基本経験値は $f_{exp}(15, 4) = 4 \times 15 \times 4 = 240$ となる。

## 獲得マッカおよびマグネタイトの計算 {#macca-and-mag}

[悪魔一覧表](/docs/demon/#normal-demon) のうち、「マッカ&MAG」列の上位4bitがマッカ係数、下位4bitがマグネタイト係数。

$L$ をレベル、 $x$ を符号付き4bit整数で表現されたマッカ係数としたとき、

$$
f_{macca}(L, x) =
\begin{cases}
8Lx & (x \geq 0)\\
L (x + 9) & (x < 0)
\end{cases}
$$

が獲得マッカとなる。

同様に、 $L$ をレベル、 $x$ を符号付き4bit整数で表現されたマグネタイト係数としたとき、

$$
f_{mag}(L, x) =
\begin{cases}
4Lx & (x \geq 0)\\
\lfloor (L / 2) \rfloor (x + 9) & (x < 0)
\end{cases}
$$

が獲得マグネタイトとなる。

例えば超人ドウマンの場合、レベル15、「マッカ&MAG」の上位4bitが `0x6` (10進数で6)、下位4bitが `0x5` (10進数で5) となっているため、
獲得マッカは $f_{macca}(15, 6) = 8 \times 15 \times 6 = 720$ 、獲得マグネタイトは $f_{mag}(15, 5) = 4 \times 15 \times 5 = 300$ となる。
