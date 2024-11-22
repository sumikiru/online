<script setup>
import { ElMessage, ElMessageBox } from 'element-plus';
import { Printer, Setting, User } from '@element-plus/icons-vue';
import { reactive, ref, provide } from 'vue';

// 对话框组件AddCandidateDialog
import AddCandidateDialog from '@/components/dialogs/AddCandidateDialog.vue';
import { provideIsAddCandidateDialogOpen } from '@/components/keys.js';
const IsDialogOpen = ref(false);
provide(provideIsAddCandidateDialogOpen, IsDialogOpen);

const Broadcast = () => {
  ElMessageBox.confirm('确定发送通知吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    //发送通知
    .then(() => {
      ElMessage({
        type: 'success',
        message: '通知已发送',
      });
    })
    //取消发送
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '已取消发送',
      });
    });
};
</script>

<template>
  <!--对话框：添加考生到考试-->
  <AddCandidateDialog />

  <div class="sidebar">
    <el-form>
      <el-form-item id="userAvatar">
        <el-avatar size="large" shape="circle" fit="cover">
          <img
            src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
            alt="用户头像占位符,点击跳转到设置"
          />
        </el-avatar>
        <el-text size="large" style="margin-left: 10px">用户名，欢迎回来</el-text>
      </el-form-item>
    </el-form>

    <el-scrollbar>
      <el-menu>
        <hr />
        <el-sub-menu index="1">
          <template #title>
            <el-icon size="30"><User /></el-icon>
            <el-text size="large">考生管理</el-text>
          </template>
          <el-menu-item-group>
            <el-menu-item index="1-1" @click="IsDialogOpen = true">添加考生</el-menu-item>
            <el-menu-item index="1-2" @click="Broadcast">通知提醒</el-menu-item>
          </el-menu-item-group>
        </el-sub-menu>

        <hr />
        <el-sub-menu index="2">
          <template #title>
            <el-icon size="30"><Printer /></el-icon>
            <el-text size="large">考试阅卷</el-text>
          </template>
          <el-menu-item-group>
            <el-menu-item index="2-1">客观题</el-menu-item>
            <el-menu-item index="2-2">主观题</el-menu-item>
          </el-menu-item-group>
        </el-sub-menu>

        <hr />
        <el-sub-menu index="3">
          <template #title>
            <el-icon size="30"><Setting /></el-icon>
            <el-text size="large">设置</el-text>
          </template>
          <el-menu-item-group>
            <!--浮层选择单题、考试限时-->
            <el-menu-item index="3-1">考试限时</el-menu-item>
            <el-menu-item index="3-2">乱序设置</el-menu-item>
            <el-menu-item index="3-3">其他设置</el-menu-item>
          </el-menu-item-group>
        </el-sub-menu>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<style scoped lang="scss">
.sidebar {
  backdrop-filter: blur(10px);
}
.el-menu-item {
  height: 40px;
}
.el-form {
  margin-left: 10px;
}
hr {
  border: black 0.1px solid;
  height: 0.1px;
  margin: 0 0;
}
.el-sub-menu {
  background-color: #cbf7fc;
}
</style>
