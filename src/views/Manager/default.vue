<script setup>
//导入子组件
import ManagerSidebar from '@/components/ManagerSidebar.vue';
</script>

<template>
  <el-container>
    <el-aside>
      <ManagerSidebar />
    </el-aside>
  </el-container>
</template>

<style scoped lang="scss">
.el-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
}
.el-aside {
  width: 250px;
  height: 100vh;
  border: black 1px solid;
}
</style>
