# smt1-note

真・女神転生 (Shin Megami Tensei) の日本語版SFCについてのデータをまとめています。

## ドキュメントの生成

[Docusaurus 2](https://docusaurus.io/) を使って生成している。

```bash
npm install # インストール
npm run start # ローカルサーバの起動
```

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
amm scripts/char-decoder.sc --str 668AB9707F # => コンゴトモ

# Decodes characters from binary file
amm scripts/char-decoder.sc --file text.bin
```

### 文字エンコーダ

[Ammonite](https://ammonite.io) が必要。

UTF-8の文字列を受け取って、16進数でエンコードされた文字コードを出力する。

```bash
# Encodes characters to hex string
amm scripts/char-encoder.sc --str コンゴトモ # => 668ab9707f
```

## リソース

### 逆アセンブリ

- https://github.com/spannerisms/smt1dasm
