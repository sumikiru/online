<script setup>
import { reactive } from 'vue';
import { useRouter } from 'vue-router';

const form = reactive({
  identity: '',
  username: '',
  password: '',
});

const router = useRouter();
const ClientLogin = () => {
  router.push('/manager');
};
</script>

<template>
  <div class="LoginPage">
    <el-container class="LoginContainer">
      <el-header>用户登录</el-header>

      <el-main>
        <el-form model="form" label-width="auto" style="max-width: 400px; justify-content: center">
          <el-form-item label="请选择您的身份" prop="identity" label-position="top">
            <el-select v-model="form.identity" placeholder="选择身份" style="width: 400px">
              <el-option label="学生" value="student" />
              <el-option label="教师" value="teacher" />
              <el-option label="管理员" value="administrator" />
            </el-select>
          </el-form-item>
          <el-form-item label="账号" prop="username" label-position="top">
            <el-input v-model="form.username" placeholder="请输入账号ID" clearable />
          </el-form-item>
          <el-form-item label="密码" prop="password" label-position="top">
            <el-input v-model="form.password" placeholder="请输入账号密码" clearable />
          </el-form-item>
          <el-form-item style="justify-self: right">忘记密码?</el-form-item>
        </el-form>
      </el-main>

      <el-footer>
        <el-button type="primary" @click="ClientLogin">登录</el-button>
      </el-footer>
    </el-container>
  </div>
</template>

<!-- Element Plus采用scss，所以需要加上lang="scss" -->
<style lang="scss" scoped>
.LoginPage {
  /*保证没有白边*/
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #f2f4f7;
  background-size: cover;
  position: fixed;
  z-index: -1; /*注意需要放置到最后*/

  /*让其中的元素居中*/
  display: flex;
  justify-content: center;
  align-items: center;
}
.LoginContainer {
  position: absolute;

  justify-self: center;
  align-items: center;
  border-radius: 20px;
  background: rgba(255, 255, 255, 1);
  backdrop-filter: blur(10px);
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);

  width: 500px;
  height: 450px;
}
.el-header {
  font-size: larger;
  text-align: center;
  line-height: 60px;
}
.el-footer {
  /*需要更改布局以后才能居中*/
  display: flex;
  justify-content: center;
  align-items: start;
  height: 50px;
}
</style>
