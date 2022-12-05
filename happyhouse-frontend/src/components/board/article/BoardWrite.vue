<template>
  <el-card class="box-card" style="margin:10px">
    <div class="card-header" v-if="editor">
      <el-input v-model="title" placeholder="Input Title..." style="margin-bottom:10px;" />
      <el-select v-model="boardId" placeholder="게시판" size="default">
        <el-option v-for="board in boardList" :key="board.id" :label="board.name" :value="board.id"></el-option>
      </el-select>
      <!-- <el-dropdown split-button type="">
        <span class="el-dropdown-link">
          <div>{{ setBoard }}</div>
          <arrow-down />
        </span>
        
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>선택해주세요</el-dropdown-item>
            <el-dropdown-item @click="choiceBoard(board.name, board.id)" v-for="board in boardList" :key="board"
              :icon="Plus">{{
                  board.name
              }}
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown> -->
      <menu-bar class="editor__header" :editor="editor" />
    </div>

    <!-- eslint-disable vue/no-multiple-template-root -->
    <editor-content style="margin:10px;" :editor="editor" />
    <div style="margin: 10px">
      <el-button @click="save">저장</el-button>
      <el-button @click="moveList">목록</el-button>
    </div>
  </el-card>
</template>


<script>
import StarterKit from '@tiptap/starter-kit'
import {
  Editor,
  EditorContent,
} from '@tiptap/vue-3'

import MenuBar from '@/components/board/menu/MenuBar.vue';
import { boardClient } from '@/clients';
import { articleClient, TOKEN_KEY } from '@/clients'
import { getUserIdFromToken } from '@/tools/JwtTools';

export default {
  components: {
    EditorContent,
    MenuBar,
  },
  data() {
    return {
      editor: null,
      title: '',
      boardId: null,
      secret: false,
      boardList: [],
      setBoard: '',
    }
  },
  mounted() {
    this.editor = new Editor({
      extensions: [
        StarterKit,
      ],
      content: 'input here...',
      editorProps: {
        attributes: {
          class: 'editor'
        }
      }
    })

  },
  created() {
    this.getList();
  },
  methods: {

    async save() {
      const token = localStorage.getItem(TOKEN_KEY);
      const authorId = getUserIdFromToken(token);
      const { title, boardId, secret } = this;

      if (!authorId) {
        alert("글쓰기는 로그인이 필요합니다.");
      }
      if (!boardId) {
        alert("게시판을 선택해 주세요");
        return;
      }

      const content = this.editor.getJSON();
      const data = await articleClient.write(title, content, authorId, boardId, secret);
      if (data) {
        this.$router.push({ path: `/article/detail/${data.articleId}` });
      } else {
        alert("글 작성 실패");
        this.$router.push({ path: "/" });
      }
    },
    moveList() {
      this.$router.push({ path: "/board" });
    },
    async getList() {
      this.boardList = await boardClient.getBoards()
      this.setBoard = this.boardList[0].name;
    },
    choiceBoard(name, id) {
      this.boardId = id;
      this.setBoard = name;
    },
  },

}
</script>

<style lang="scss">
@import '@/assets/editor.scss';

.editor {
  height: 50vh;
}

.example-showcase .el-dropdown+.el-dropdown {
  margin-left: 15px;
}

.example-showcase .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}
</style>