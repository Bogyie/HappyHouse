<template>
  <el-row>
    <el-col :span="6" :offset="9">
      <el-card class="container">
        <template #header>
          <div class="findpwd">
            <span>비밀번호 변경</span>
          </div>
        </template>
        <el-form label-width="60px">
          <el-form-item label="Pwd">
            <el-input v-model="pwd" size="smail" placeholder="변경할 비밀번호...">
            </el-input>
          </el-form-item>
        </el-form>
        <span></span>
        <div class="button">
          <el-form-item>
            <el-button>
              <router-link to="/register">변경</router-link>
            </el-button>
          </el-form-item>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
export default {

}
</script>

<style scoped>
.container {
  margin-top: 100px;
}

.findpwd {
  text-align: center;
  font-weight: bold;
  font-size: large;
}

.button {
  margin-top: 15px;
  float: right;
}
</style>