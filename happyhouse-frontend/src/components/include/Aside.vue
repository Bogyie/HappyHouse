<template>
  <el-aside width="200px" class="aside">
    <!-- <el-scrollbar class="aside-main"> -->
    <el-menu router>
      <el-menu-item index="/"><i class='bx bx-home bx-lg' style="margin:15px 0px 15px 0px"></i>
        <span class="home">구해줘 홈즈</span>
      </el-menu-item>
      <el-sub-menu index="1" v-show="loginCheck">
        <template #title>
          <i class='bx bxs-user-account bx-sm'></i><span class="middle">Info</span>
        </template>
        <el-menu-item-group>
          <el-menu-item index="/info"><i class='bx bxs-user bx-xs'></i><span class="middle">Info</span></el-menu-item>
        </el-menu-item-group>
      </el-sub-menu>
      <el-sub-menu index="2">
        <template #title>
          <i class='bx bx-list-ul bx-sm'></i><span class="middle">Board</span>
        </template>
        <el-menu-item-group>
          <el-menu-item v-for="board in boardList" :key="board" :index="`/board/${board.id}`">
            <span class="middle">
              {{ board.name }}
            </span>
          </el-menu-item>
          <el-menu-item>
            <el-button @click="addBoard">게시판 생성</el-button>
          </el-menu-item>
        </el-menu-item-group>
      </el-sub-menu>
      <el-sub-menu index="3">
        <template #title>
          <i class='bx bxs-building-house bx-sm'></i><span class="middle">House</span>
        </template>
        <el-menu-item-group>
          <el-menu-item index="/estate"><i class='bx bx-search bx-xs'></i><span class="middle ">Search</span>
          </el-menu-item>
          <!-- <el-menu-item index="3-2">Option 2</el-menu-item> -->
        </el-menu-item-group>
      </el-sub-menu>
      <div class="menu-gutter"></div>
    </el-menu>
    <!-- </el-scrollbar> -->
  </el-aside>
</template>

<script>
import { boardClient, TOKEN_KEY } from '@/clients';

export default {
  data: () => ({
    boardList: [],
    loginCheck: false,
  }),
  methods: {
    // TODO 아이디체크 
    async getList() {
      this.boardList = await boardClient.getBoards()
    },
    async addBoard() {
      const boardName = prompt("생성할 게시판 이름을 작성해주세요.");

      if (boardName) {
        const result = boardClient.create(boardName);
        if (result) {
          alert("게시판 생성 성공!");
          this.$router.go();
          return;
        }
        alert("게시판 생성 실패");
      }
    }
  },
  created() {
    this.getList();
    const token = localStorage.getItem(TOKEN_KEY);
    if (token) {
      this.loginCheck = true;
    }
  },
}
</script>

<style scoped>
ul[role=menubar]>.el-menu-item {
  background-color: #001537;
}

.el-menu {
  color: #000;
  border-radius: 0 0 10px 0;
}

.menu-gutter {
  height: 8px;
  border-radius: 0 0 8px 0;
}

.home {
  font-style: italic;
  font-weight: bold;
  font-size: 20px;
  color: #fff;
}

i {
  margin-right: 10px;
}

.el-menu:first-child {
  border-right: none;
}

.el-menu:last-child {
  /* border-right: none; */
  border-radius: 0 0 10px 0;
}

.bx {
  color: #2e547c;
}

.el-menu-item {
  font-weight: lighter;
}

.el-aside .el-sub-menu {
  color: #000000;
  border-radius: 0 0 10px 0;
}

.middle {
  color: #2e547c;
}

.el-menu-item-group {
  color: #000000
}
</style>
