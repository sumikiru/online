<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';

const form = reactive({
  identity: '',
  username: '',
  password: '',
});

const rules = {
  identity: [{ required: true, message: '请选择您的身份', trigger: 'blur' }],
  username: [{ required: true, message: '请输入您的账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入您的密码', trigger: 'blur' }],
};

const router = useRouter();
const formRef = ref(null);

const ClientLogin = async () => {
  try {
    await formRef.value.validate();
    switch (form.identity) {
      case 'student':
        router.push('/student');
        break;
      case 'teacher':
        router.push('/manager');
        break;
      case 'administrator':
        router.push('/administrator');
        break;
      default:
        alert('未知身份');
    }
  } catch {
    console.log('验证失败');
    alert('请检查您的输入是否符合要求');
  }
};
</script>

<template>
  <div class="LoginPage">
    <el-container class="LoginContainer">
      <el-header>用户登录</el-header>

      <el-main>
        <el-form :model="form" label-width="auto" style="max-width: 400px; justify-content: center" :rules="rules" ref="formRef">
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
            <el-input v-model="form.password" placeholder="请输入账号密码" clearable type="password" />
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

<style lang="scss" scoped>
.LoginPage {
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #f2f4f7;
  background-size: cover;
  position: fixed;
  z-index: -1;

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
  display: flex;
  justify-content: center;
  align-items: start;
  height: 50px;
}
</style>