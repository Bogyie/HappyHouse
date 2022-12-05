<template>
  <el-card class="box-card" style="margin:10px">
      <template #header>
          <div class="card-header">
              <h2>{{ title }}</h2>
          </div>
      </template>

      <!-- eslint-disable vue/no-multiple-template-root -->
      <editor-content class="p-3" :editor="editor" />
      
      <div style="margin: 10px">
          <el-button @click="modify">수정</el-button>
          <el-button @click="remove">삭제</el-button>
          <el-button @click="moveList">목록</el-button>
      </div>
    <el-card>
      <el-input v-model="comment" placeholder="댓글작성...">
      </el-input>
      <el-button @click="createComment">작성</el-button>
    </el-card>
    <el-table :data="comments" highlight-current-row style="width: 100%" @row-click="onRowClick">
        <el-table-column prop="content" label="댓글"/>
        <el-table-column prop="createdDate" label="작성일"/>
        <el-table-column>
            <template #default="scope">
                <el-button size="small" @click="deleteComment(scope.row)">Delete</el-button>
            </template>
        </el-table-column>
    </el-table>
  </el-card>
</template>

<script>
import StarterKit from '@tiptap/starter-kit'
import {
  Editor,
  EditorContent,
} from '@tiptap/vue-3'
import { articleClient, commentClient } from '@/clients'

export default {
  components: {
      EditorContent,
  },

  data() {
      return {
        editor: null,
          id: 0,
          title: '',
          authorId: 0,
          author : '',
          board : '',
          boardId : 0,
          views : 0,
          createdDate : '',
          modifiedDate : '',
          articleId: 0, 
          secret: false,
          comments: [],
          comment: '',
      }
  },

  async mounted() {
    const { data } = await articleClient.read(this.$route.params.id);
    this.id = this.$route.params.id;
    this.title = data.title;
    this.authorId = data.authorId;
    this.author = data.author;
    this.board = data.board;
    this.boardId = data.boardId;
    this.views = data.views;
    this.createdDate = data.createdDate;
    this.modifiedDate = data.modifiedDate;
    const body = JSON.parse(data.content);
      this.editor = new Editor({
          extensions: [
              StarterKit,
          ],
          editable: false,
          content: body,
      })

      this.comments = await articleClient.comments(this.id);
  },

  methods: {
      modify() {
          this.$router.push({ path: `/article/modify/${this.id}`});
      },
      moveList() {
          this.$router.push({ path: `/board/${this.boardId}` });
      },
      async remove() {
          const id = this.id;
          await articleClient.delete(id);
          this.moveList();
      },
      async createComment() {
        const { comment, id, secret } = this;
        const result = await commentClient.create(comment, id, secret);
        console.log(result)
        if( result?.id ){
            alert("댓글 작성 성공");
            this.comments.push({ id: result.id, content:result.content, createdDate: (new Date(result.createDate)).toLocaleDateString("ko-KR")  });
            return;
        }else{
            alert('댓글 작성 실패');
        }
      },
      async deleteComment(row){
        const result = await commentClient.delete(row['id']);
        if (result) {
            this.comments = this.comments.filter(c => c.id != row['id']);
        }
      }
  },

  beforeUnmount() {
      this.editor.destroy()
  }
}
</script>

<style lang="scss">
@import '@/assets/editor.scss';
</style>
