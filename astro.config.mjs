import starlight from '@astrojs/starlight';
// @ts-check
import { defineConfig } from 'astro/config';
import rehypeKatex from 'rehype-katex';
import remarkCustomHeadingId from 'remark-custom-heading-id';
import remarkMath from 'remark-math';

// https://astro.build/config
export default defineConfig({
  site: 'https://smt1-note.vercel.app',
  trailingSlash: 'never',
  markdown: {
    remarkPlugins: [remarkCustomHeadingId, remarkMath],
    rehypePlugins: [[rehypeKatex, { output: 'html' }]],
  },
  integrations: [
    starlight({
      title: '真・女神転生1 解析ノート',
      description:
        'アトラスのRPG『真・女神転生』のSFC版に関するデータをまとめています。',
      defaultLocale: 'root',
      locales: {
        root: {
          label: '日本語',
          lang: 'ja',
        },
      },
      customCss: ['./src/styles/custom.css'],
      favicon: '/favicon.ico',
      head: [
        {
          tag: 'link',
          attrs: {
            rel: 'icon',
            type: 'image/svg+xml',
            href: '/favicon.svg',
          },
        },
        {
          tag: 'link',
          attrs: {
            rel: 'stylesheet',
            href: 'https://cdn.jsdelivr.net/npm/katex@0.16.8/dist/katex.min.css',
            integrity:
              'sha384-GvrOXuhMATgEsSwCs4smul74iXGOixntILdUW9XmUC6+HX0sLNAK3q71HotJqlAn',
            crossorigin: 'anonymous',
          },
        },
      ],
      social: [
        {
          icon: 'github',
          label: 'GitHub',
          href: 'https://github.com/akiomik/smt1-note',
        },
      ],
      editLink: {
        baseUrl: 'https://github.com/akiomik/smt1-note/edit/main/',
      },
      sidebar: [
        {
          label: 'ゲームシステム',
          collapsed: false,
          autogenerate: { directory: 'system' },
        },
        {
          label: 'スキル',
          items: [
            { label: '概要', slug: 'skill' },
          ],
        },
        {
          label: '悪魔',
          items: [
            { label: '概要', slug: 'demon' },
            { label: '行動パターン', slug: 'demon/action-pattern' },
            { label: 'バトル報酬', slug: 'demon/battle-reward' },
          ],
        },
        {
          label: 'アイテム',
          items: [
            { label: '概要', slug: 'item' },
            { label: '装備品', slug: 'item/equipment' },
          ],
        },
        {
          label: 'テキスト',
          items: [
            { label: '概要', slug: 'text' },
            { label: '定型句', slug: 'text/phrase' },
          ],
        },
      ],
    }),
  ],
});
