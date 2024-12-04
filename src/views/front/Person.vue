<template>
  <div style="width: 40%; margin: 5px auto" class="card">
    <div style="text-align: right; padding: 0 20px">
      <el-button type="warning" @click="updateInit">修改密码</el-button>
    </div>
    <el-form ref="user" :model="data.user" label-width="60px" style="padding: 20px">
      <div style="text-align: center; margin-bottom: 20px">
        <el-upload :action="baseUrl + '/files/upload'" :on-success="handleFileUpload" :before-upload="beforeAvatarUpload" :show-file-list="false" class="avatar-uploader">
          <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar" alt="" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </div>
      <el-form-item prop="username" label="用户名">
        <el-input disabled v-model="data.user.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item prop="name" label="姓名">
        <el-input v-model="data.user.name" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item prop="phone" label="电话">
        <el-input v-model="data.user.phone" placeholder="请输入电话"></el-input>
      </el-form-item>
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="data.user.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>
      <div style="text-align: center">
        <el-button type="primary" @click="update">保 存</el-button>
      </div>
    </el-form>

    <el-dialog title="修改密码" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.user" label-width="70px" style="padding: 20px">
        <el-form-item label="原密码" prop="password">
          <el-input v-model="data.user.password" placeholder="请输入原密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="data.user.newPassword" placeholder="请输入新密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="data.user.confirmPassword" placeholder="请确认新密码" show-password></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="(data.formVisible = false)">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import request from '@/utils/request.js';
import { ElMessage } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import router from '@/router/index.js';

const formRef = ref();
const baseUrl = import.meta.env.VITE_BASE_URL;

const validatePass = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请确认密码'));
  } else {
    if (value !== data.user.newPassword) {
      callback(new Error('确认密码跟新密码不一致!'));
    }
    callback();
  }
};

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  rules: {
    password: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
    newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
    confirmPassword: [{ validator: validatePass, trigger: 'blur' }],
  },
});

// 修改上传图片尺寸，使得默认取最终间部分以控制比例
const beforeAvatarUpload = (file) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = (event) => {
      const img = new Image();
      img.src = event.target.result;
      img.onload = () => {
        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d');

        // 设置裁剪区域和输出大小
        const size = 800;
        const cropSize = Math.min(img.width, img.height); // 取较小边
        const startX = (img.width - cropSize) / 2;
        const startY = (img.height - cropSize) / 2;

        // 裁剪并绘制到 canvas
        canvas.width = size;
        canvas.height = size;
        ctx.drawImage(img, startX, startY, cropSize, cropSize, 0, 0, size, size);

        // 转换为 Blob 并替换原文件上传
        canvas.toBlob(
          (blob) => {
            if (blob) {
              const newFile = new File([blob], file.name, { type: 'image/jpeg' });
              resolve(newFile);
            } else {
              reject(new Error('Image processing failed'));
            }
          },
          'image/jpeg',
          1,
        );
      };
    };
    reader.onerror = reject;
    reader.readAsDataURL(file);
  });
};

const handleFileUpload = (res) => {
  data.user.avatar = res.data;
  ElMessage.success('图片已成功上传，请点击保存');
};

const emit = defineEmits(['updateUser']);
const update = () => {
  if (data.user.role === 'STUDENT') {
    request.put('/student/update', data.user).then((res) => {
      if (res.code === '200') {
        ElMessage.success('保存成功');
        localStorage.setItem('xm-user', JSON.stringify(data.user));
        emit('updateUser');
      } else {
        ElMessage.error(res.msg);
      }
    });
  }
};

const updateInit = () => {
  data.formVisible = true;
};

const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.put('/updatePassword', data.user).then((res) => {
        if (res.code === '200') {
          ElMessage.success('保存成功');
          localStorage.removeItem('xm-user');
          router.push('/login');
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};
</script>

<style>
.avatar-uploader {
  height: 120px;
}
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>
