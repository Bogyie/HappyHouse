<template>
  <el-container class="flex-box">
    <div v-for='news in newsList' :key="news">
      <el-card style="width: 480px;height: 500px;">
        <template #header>
          <div class="card-header">
            <span class="title" v-html="news.title">
            </span><br />
            <el-button class="button" text>{{ news.author }}</el-button>
          </div>
          <br />
        </template>
        <el-scrollbar height="400px">
          <div class="page" v-html="news.content"></div>
        </el-scrollbar>
      </el-card>
    </div>
  </el-container>
</template>

<script>
import { newsClient } from '@/clients';

export default {
  data: () => ({
    /**
     * @type {{
   *   title: string,
   *   pubDate: string,
   *   link: string,
   *   guid: string,
   *   author: string,
   *   thumbnail: string,
   *   description: string,
   *   content: string
   * }[]}
     */
    newsList: [],
  }),
  async mounted() {
    this.newsList = await newsClient.getNews();
  },
}
</script>

<style>
.el-card {
  margin-left: 15px;
  margin-bottom: 15px;
}

.flex-box {
  display: flex;
  flex-wrap: wrap;
}

.button {
  float: right;
}

.title {
  font-weight: bold;
}

.page {
  padding: 15px;
}
</style>