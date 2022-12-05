<template>
  <el-container style="margin: auto; max-width: 600px;">
    <el-card class="container">
      <template #header>
        <div class="login">
          <span>로그인</span>
        </div>
      </template>
      <el-form label-width="80px">
        <el-form-item label="Id">
          <el-input v-model="username" size="smail" placeholder="Id...">
          </el-input>
        </el-form-item>
        <el-form-item label="Password">
          <el-input v-model="password" size="smail" type="password" placeholder="Password...">
          </el-input>
        </el-form-item>
      </el-form>
      <div>
        <router-link :to="{ name: 'FindPwd' }"><span class="findpwd">비밀번호를 잊으셨나요?</span></router-link>
      </div>
      <span></span>
      <div class="button">
        <el-form-item>
          <el-button @click="login">로그인</el-button>
          <el-button>
            <router-link to="/register">회원가입</router-link>
          </el-button>
        </el-form-item>
      </div>
    </el-card>
  </el-container>
</template>

<script>
import { memberClient } from '@/clients';

export default {
  data: () => ({
    username: "",
    password: "",
  }),
  methods: {
    async login() {
      const { username, password } = this;
      const result = await memberClient.login(username, password);
      if (result) {
        this.$router.push({ name: 'home' });
      } else {
        alert("아이디 또는 비밀번호가 틀렸습니다.")
        this.$router.push({ name: 'Login' });
      }
    }
  }
}
</script>

<style scoped>
.container {
  margin-top: 100px;
}

.login {
  text-align: center;
  font-weight: bold;
  font-size: large;
}

.button {
  margin-top: 15px;
  float: right;
}

.findpwd {
  font-weight: bold;
  font-size: 12px;
  margin-left: 120px;
}
</style>