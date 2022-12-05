<template>
  <el-card :body-style="{ padding: '20px' }">
    <el-table :data="articleList" highlight-current-row style="width: 100%" @row-click="onRowClick">
      <el-table-column prop="id" label="번호." width="80" sortable />
      <el-table-column prop="title" label="제목."></el-table-column>
      <el-table-column prop="author" label="작성자." width="100"></el-table-column>
      <el-table-column prop="views" label="조회수." width="100" sortable></el-table-column>
      <el-table-column prop="createdDate" label="작성일." width="120" sortable></el-table-column>
    </el-table>
    <div style="margin: 20px">
      <el-button class="left-box" @click="moveWrite()">Add</el-button>
      <el-button :disabled="articleList.length < 10" class="right-box" @click="nextPage">Next</el-button>
      <el-button :disabled="page <= 1" class="right-box" @click="prevPage">Prev</el-button>
    </div>
  </el-card>
</template>
<script>
import { articleClient } from "@/clients";


export default {
  name: "Board",
  data: () => ({
    articleList: [],
    boardId: 0,
    page: 1,
    size: 10,
  }),
  created() {
    this.boardId = this.$route.params.id;
  },
  async mounted() {
    this.getArticles();
  },
  methods: {
    async getArticles() {
      const { boardId, page, size } = this;
      this.articleList = await articleClient.list(boardId, page, size);
    },
    onRowClick(row) {
      this.$router.push(`/board/detail/${row['id']}`)
    },
    moveWrite() {
      this.$router.push({ name: "BoardWrite" });
    },
    nextPage() {
      this.page++;
      this.search();
    },
    prevPage() {
      this.page--;
      this.search();
    },
  },
}
</script>


<style scoped>
.el-button {
  margin-bottom: 20px;
}

.left-box {
  float: left;
}

.right-box {
  float: right;
}
</style>