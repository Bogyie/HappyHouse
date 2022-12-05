<template>
  <div id="app" class="background">
    <div class="common-layout">
      <el-container>
        <Aside :key="$route.fullPath" />
        <el-container>
          <el-header class="header">
            <Header :key="$route.fullPath" />
          </el-header>
          <el-main>
            <router-view :key="$route.fullPath" />
          </el-main>
        </el-container>
      </el-container>
    </div>
  </div>
</template>

<script>
import Aside from "@/components/include/Aside.vue";
import Header from "@/components/include/Header.vue";

export default {
  name: 'App',
  components: {
    Aside,
    Header,
  }
}
</script>

<style scoped>
@import url("https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css");
@import url("https://rsms.me/inter/inter.css");


.el-header {
  border-bottom: solid 1px;
  background-color: azure;
}

.header {
  text-align: right;
  font-size: 12px;
  height: 56px;
  background-color: #001537;
}

.common-layout {
  width: 100%;
  min-height: 100vh;
  height: 100%;
}

.background {
  margin: 0;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(to bottom,
      rgba(255, 255, 255, 0) 10%,
      rgba(255, 255, 255, 0.5) 50%,
      rgba(255, 255, 255, 0.7) 80%,
      rgba(255, 255, 255, 0.8) 90%,
      rgba(255, 255, 255, 0.9) 100%), url("@/assets/main-bg1.png");
  background-size: 100%;
  background-repeat: no-repeat;
}
</style>
