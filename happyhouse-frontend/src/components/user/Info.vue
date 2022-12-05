<template>
  <el-row :gutter="10">
    <el-col :span="8">
      <el-card class="container">
        <template #header>
          <div class="login">
            <span>회원정보</span>
          </div>
        </template>
        <el-form label-width="80px">
          <el-form-item label="Id">
            <el-input v-model="username" size="smail" disabled>
            </el-input>
          </el-form-item>
          <el-form-item label="Nickname">
            <el-input v-model="nickname" size="smail" placeholder="Nickname...">
            </el-input>
          </el-form-item>
          <el-form-item label="Email">
            <el-input v-model="email" size="smail" placeholder="Email...">
            </el-input>
          </el-form-item>
          <el-form-item label="Address">
            <el-input v-model="address" size="smail" placeholder="Address...">
            </el-input>
          </el-form-item>
        </el-form>
        <span></span>
        <div class="button">
          <el-form-item>
            <el-button>
              비밀번호 변경
            </el-button>
            <el-button @click="modify">수정</el-button>
            <el-button @click="remove">탈퇴</el-button>
          </el-form-item>
        </div>
      </el-card>
    </el-col>
    <el-col :span="8">
      <el-card class="container">
        <template #header>
          <div class="login">
            <span>게시글 정보</span>
          </div>
        </template>
        <el-table :data="articles" highlight-current-row style="width: 100%" @row-click="onRowClick">
          <el-table-column prop="title" label="제목" />
          <el-table-column prop="author" label="작성자" />
          <el-table-column prop="createdDate" label="작성날짜" sortable />
        </el-table>
      </el-card>
    </el-col>
    <el-col :span="8">
      <el-card class="container">
        <template #header>
          <div class="login">
            <span>댓글 정보</span>
          </div>
        </template>
        <el-table :data="comments" highlight-current-row style="width: 100%" @row-click="onCommentClick">
          <el-table-column prop="content" label="내용" />
          <el-table-column prop="author" label="작성자" />
          <el-table-column prop="createDate" label="작성날짜" sortable />
        </el-table>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import { memberClient, TOKEN_KEY } from '@/clients';
import { getUserIdFromToken } from '@/tools/JwtTools';

export default {
  data() {
    return {
      username: '',
      nickname: '',
      email: '',
      address: '',
      articles: [],
      comments: [],
    }
  },
  created() {
    this.getInfo();
    this.getArticles();
    this.getComments();
  },
  methods: {
    async getInfo() {
      const token = localStorage.getItem(TOKEN_KEY);
      const userid = getUserIdFromToken(token);
      const data = await memberClient.userDetail(userid);
      this.username = data.username;
      this.nickname = data.nickname;
      this.email = data.email;
      this.address = data.address;
    },
    async modify() {
      const { nickname, email, address } = this;
      const token = localStorage.getItem(TOKEN_KEY);
      const userid = getUserIdFromToken(token);
      const data = await memberClient.userModify(userid, nickname, email, address)
      if (data) {
        alert("수정에 성공하셨습니다.");
        return;
      }
      alert("수정에 실패하셨습니다.");
    },
    async remove() {
      const token = localStorage.getItem(TOKEN_KEY);
      const userid = getUserIdFromToken(token);
      const result = await memberClient.userRemove(userid);

      if (result) {
        alert("성공적으로 탈퇴되었습니다");
        localStorage.removeItem(TOKEN_KEY);
        this.$router.push({ name: "home" });
        return;
      }
      alert("오류가 발생하였습니다");
    },
    async getArticles() {
      const token = localStorage.getItem(TOKEN_KEY);
      const userid = getUserIdFromToken(token);
      const data = await memberClient.findArticle(userid);

      if (data) {
        this.articles = data;
        return;
      }
      alert("게시글을 불러오는도중 오류가 발생하였습니다");
    },
    async getComments() {
      const token = localStorage.getItem(TOKEN_KEY);
      const userid = getUserIdFromToken(token);
      const data = await memberClient.findComments(userid);

      if (data) {
        this.comments = data;
        return;
      }
      alert("댓글을 불러오는도중 오류가 발생하였습니다");
    },
    onRowClick(row) {
      this.$router.push(`/board/detail/${row['id']}`)
    },
    onCommentClick(row) {
      this.$router.push(`/board/detail/${row['articleId']}`)
    }
  }
}
</script>

<style scoped>
.login {
  text-align: center;
  font-weight: bold;
  font-size: large;
}

.button {
  margin-top: 20px;
  float: right;
}
</style>