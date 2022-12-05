<template>
  <el-card class="box-card" style="margin:10px">
    <div class="card-header" v-if="editor">
      <el-input v-model="title" placeholder="Input Title..." style="margin-bottom:10px;" />
      <menu-bar class="editor__header" :editor="editor" />
    </div>

    <!-- eslint-disable vue/no-multiple-template-root -->
    <editor-content style="margin:10px;" :editor="editor" />
    <div style="margin: 10px">
      <el-button @click="save">저장</el-button>
      <el-button @click="remove">삭제</el-button>
      <el-button @click="moveList">목록</el-button>
    </div>
  </el-card>
</template>
  
<script>
import { articleClient } from '@/clients'
import StarterKit from '@tiptap/starter-kit'
import {
  Editor,
  EditorContent,
} from '@tiptap/vue-3'

import MenuBar from '@/components/board/menu/MenuBar.vue'

export default {
  components: {
    EditorContent,
    MenuBar,
  },

  data() {
    return {
      boardId: 0,
      editor: null,
      articleId: 0,
      title: '',
      author: '',
      board: '',
      views: 0,
      createdDate: '',
      modifiedDate: '',
    }
  },

  async mounted() {
    const { data } = await articleClient.read(this.$route.params.id);
    const {
      articleId, title, author,
      content, board, boardId,
      views, createdDate, modifiedDate
    } = data;

    this.boardId = boardId;
    this.articleId = articleId;
    this.title = title;
    this.author = author;
    this.board = board;
    this.views = views;
    this.createdDate = createdDate;
    this.modifiedDate = modifiedDate;
    const body = JSON.parse(content);
    this.editor = new Editor({
      extensions: [
        StarterKit,
      ],
      editable: true,
      content: body,
      editorProps: {
        attributes: {
          class: 'editor'
        }
      }
    })
  },

  methods: {
    async save() {
      const { articleId, title } = this;
      const content = this.editor.getJSON();
      await articleClient.update(articleId, title, content);
      this.$router.push({ path: `/article/detail/${articleId}` });
    },
    moveList() {
      this.$router.push({ path: `/board/${this.boardId}` });
    },
    async remove() {
      await articleClient.delete(this.articleId);
      this.moveList();
    }
  },

  beforeUnmount() {
    this.editor.destroy()
  },
}
</script>
  
<style lang="scss">
@import '@/assets/editor.scss';

.editor {
  height: 50vh;
}
</style>
