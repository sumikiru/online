<template>
  <div style="width: 50%" class="card">
    <el-form ref="user" :model="data.user" label-width="70px" style="padding: 20px">
      <el-form-item prop="avatar" label="头像">
        <el-upload :action="baseUrl + '/files/upload'" :on-success="handleFileUpload" :before-upload="beforeAvatarUpload" :show-file-list="false" class="avatar-uploader">
          <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar" alt="" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
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
  </div>
</template>

<script setup>
import { reactive } from 'vue';
import request from '@/utils/request.js';
import { ElMessage } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';

const baseUrl = import.meta.env.VITE_BASE_URL;

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
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
  if (data.user.role === 'ADMIN') {
    request.put('/admin/update', data.user).then((res) => {
      if (res.code === '200') {
        ElMessage.success('保存成功');
        localStorage.setItem('xm-user', JSON.stringify(data.user));
        emit('updateUser');
      } else {
        ElMessage.error(res.msg);
      }
    });
  }
  if (data.user.role === 'TEACHER') {
    request.put('/teacher/update', data.user).then((res) => {
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
  border-radius: 6px;
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
