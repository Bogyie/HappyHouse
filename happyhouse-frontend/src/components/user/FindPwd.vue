<template>
  <el-row>
    <el-col :span="6" :offset="9">
      <el-card class="container">
        <template #header>
          <div class="findpwd">
            <span>비밀번호 찾기</span>
          </div>
        </template>
        <el-form label-width="60px">
          <el-form-item label="Id">
            <el-input v-model="username" size="smail" placeholder="비밀번호를 찾을 아이디...">
            </el-input>
          </el-form-item>
          <el-form-item label="Email">
            <el-input v-model="email" size="smail" placeholder="가입 당시 입력한 이메일...">
            </el-input>
          </el-form-item>
          <el-form-item label="NewPwd">
            <el-input v-model="newpwd" size="smail" placeholder="새로 사용할 비밀번호...">
            </el-input>
          </el-form-item>
        </el-form>
        <span></span>
        <div class="button">
          <el-form-item>
            <el-button @click="changePwd">
              변경
            </el-button>
          </el-form-item>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import { memberClient } from '@/clients';
export default {
  data: () => ({
    username: "",
    email: "",
    newpwd: "",
  }),
  methods: {
    async changePwd() {
      const { username, email, newpwd } = this;
      const data = await memberClient.findPwd(username, email, newpwd);
      if (data) {
        alert("변경에 성공하였습니다");
        return;
      }
      alert("아이디가 존재하지 않거나 오류가 발생하였습니다");
    },
  }
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
