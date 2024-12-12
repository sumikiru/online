<template>
  <div class="login-container">
    <div class="login-box">
      <div style="font-weight: bold; font-size: 24px; text-align: center; margin-bottom: 30px; color: #1450aa">欢迎登录在线考试系统</div>
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input show-password :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item prop="role">
          <el-select size="large" v-model="data.form.role" placeholder="请选择身份">
            <el-option value="ADMIN" label="管理员"></el-option>
            <el-option value="TEACHER" label="教师"></el-option>
            <el-option value="STUDENT" label="学生"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" style="width: 100%" @click="login">登 录</el-button>
        </el-form-item>
        <div style="text-align: right">还没有账号？请 <a href="/register">注册</a></div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { User, Lock } from '@element-plus/icons-vue';
import request from '@/utils/request.js';
import { ElMessage } from 'element-plus';
import router from '@/router/index.js';

const data = reactive({
  form: {},
  rules: {
    username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
    role: [{ required: true, message: '请选择身份', trigger: 'change' }],
  },
});

const formRef = ref();

const login = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      // 表示表单校验通过
      request.post('/login', data.form).then((res) => {
        if (res.code === '200') {
          ElMessage.success('登录成功');
          // 存储用户信息到浏览器的缓存
          localStorage.setItem('xm-user', JSON.stringify(res.data));
          setTimeout(() => {
            if ('STUDENT' === res.data.role) {
              location.href = '/front/home';
            } else {
              location.href = '/manager/home';
            }
          }, 500);
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};
</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(to top, #7f7fd5, #86a8e7, #91eae4);
}
.login-box {
  width: 350px;
  padding: 30px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.5);
}
</style>
