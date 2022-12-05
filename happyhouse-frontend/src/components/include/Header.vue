<template>
  <el-row style="margin-top:10px" :gutter="15">
    <el-col :span="4">
      <el-cascader :options="SiGunGuOptions" v-model="localCode" clearable :show-all-levels="false"
        :props="{ expandTrigger: 'hover', multiple: false, checkStrictly: false, emitPath: false }"
        @keypress.enter="searchEstateList">
      </el-cascader>
    </el-col>
    <el-col :span="3">
      <el-select v-model.lazy="serviceType">
        <el-option v-for="item in searchTypes" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
    </el-col>
    <el-col :span="3">
      <el-input-number :controls=false v-model.lazy="dealYear" placeholder="2022" @keypress.enter="searchEstateList">
      </el-input-number>
    </el-col>
    <el-col :span="3">
      <el-input-number :controls=false v-model.lazy="dealMonth" placeholder="월..." @keypress.enter="searchEstateList">
      </el-input-number>
    </el-col>
    <el-col :span="1">
      <el-button type="info" @click="searchEstateList">검색</el-button>
    </el-col>
    <el-col :span="3" style="margin-left: auto;">
      <el-dropdown class="user" v-if="loginCheck">
        <i class='bx bxs-user-detail bx-md'>
        </i>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>
              <router-link to="/info">회원정보</router-link>
            </el-dropdown-item>
            <el-dropdown-item @click="moveLogout">로그아웃</el-dropdown-item>
            <el-dropdown-item @click="addBoard">게시판 생성</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <el-dropdown class="user" v-else>
        <i class='bx bx-log-in bx-md'>
        </i>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>
              <router-link to="/login">로그인</router-link>
            </el-dropdown-item>
            <el-dropdown-item>
              <router-link to="/register">회원가입</router-link>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </el-col>
  </el-row>
</template>

<script>
import { TOKEN_KEY, boardClient } from "@/clients";
import SiGunGuOptions from "@/data/SiGunGuOptions";

export default {
  data: () => ({
    localCode: 0,
    serviceType: "",
    dealYear: new Date().getFullYear(),
    dealMonth: new Date().getMonth() + 1,
    SiGunGuOptions,
    searchTypes: [
      { value: 'apt-trade', label: '아파트 거래' },
      { value: 'apt-rent', label: '아파트 전월세' },
      { value: 'offi-trdae', label: '오피스텔 거래' },
      { value: 'offi-rent', label: '오피스텔 전월세' },
      { value: 'rich-trade', label: '연립주택 거래' },
      { value: 'rich-rent', label: '연릭주택 전월세' },
      { value: 'single-trade', label: '단독주택 거래' },
      { value: 'single-rent', label: '단독주택 전월세' },
    ],
    loginCheck: false,
  }),
  created() {
    const token = localStorage.getItem(TOKEN_KEY);
    if (token) {
      this.loginCheck = true;
    }
  },
  methods: {
    moveLogout() {
      localStorage.removeItem(TOKEN_KEY);
      this.$router.go();
      this.$router.push({ name: "home" });
    },
    searchEstateList() {
      const { localCode, serviceType, dealYear, dealMonth } = this;
      localStorage.setItem("searchQuery", JSON.stringify({ localCode, serviceType, dealYear, dealMonth }))
      if (this.$route.path == "/estate/list") {
        this.$router.go();
      }
      this.$router.push("/estate/list");
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
  mounted() {
    const searchQuery = localStorage.getItem("searchQuery");
    if (searchQuery) {
      const { localCode, serviceType, dealYear, dealMonth } = JSON.parse(searchQuery);
      this.localCode = localCode;
      this.serviceType = serviceType;
      this.dealYear = dealYear;
      this.dealMonth = dealMonth;
    }
  }
}
</script>

<style scoped>
span {
  margin-left: 25px;
}

.user {
  margin-left: auto;
}
</style>