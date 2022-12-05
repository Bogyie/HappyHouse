<template>
  <el-container id="app" class="app">
    <el-col :span="6" :offset="5">
      <el-card class="container">
        <template #header>
          <div class="Register">
            <span>회원가입</span>
          </div>
        </template>
        <el-form label-width="70px">
          <el-form-item label="Id">
            <el-input v-model="username" size="smail" placeholder="Id...">
            </el-input>
          </el-form-item>
          <el-form-item label="Pwd">
            <el-input v-model="password" size="smail" placeholder="Password..." show-password>
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
          <el-checkbox v-model="role" label="약관에 동의하십니까?" size="large">
          </el-checkbox>
        </el-form>
        <span></span>
        <div class="button">
          <el-form-item>
            <el-button @click="create">
              가입
            </el-button>
          </el-form-item>
        </div>
      </el-card>
    </el-col>
    <el-col :span="6" :offset="1">
      <el-card shadow="always" :body-style="{ width: '405px' }">
        <el-scrollbar height="700px">
          <register-role />
        </el-scrollbar>
      </el-card>
    </el-col>
  </el-container>
</template>

<script>
import { memberClient } from "@/clients"
import RegisterRole from "@/components/user/RegisterRole.vue"

export default {
  name: "#app",
  components: {
    RegisterRole,
  },
  data() {
    return {
      username: '',
      password: '',
      nickname: '',
      email: '',
      address: '',
      role: false,
    }
  },
  methods: {
    async create() {
      // this.$router.push({ name: 'Login' });
      if (this.role) {
        if (!this.username || !this.password || !this.nickname || !this.email || !this.address) {
          alert("입력되지 않은 값이 존재합니다");
          return;
        }
        const { username, password, nickname, email, address } = this;
        try {
          const data = await memberClient.register(username, password, nickname, email, address);
          if (data) {
           this.$router.push({ name: 'Login' });
          }
        } catch {
          alert("회원가입 실패. 이미 존재하는 아이디입니다.");
        }
      } else {
        alert("약관에 동의해주세요");
      }
    }
  }
}
</script>

<style scoped>
.container {
  margin-top: 170px;
}

.app {
  display: flex;
  flex-wrap: wrap;
}

.Register {
  text-align: center;
  font-weight: bold;
  font-size: large;
}

.button {
  margin-top: 20px;
  float: right;
}

.el-checkbox {
  padding-left: 110px;
}
</style>