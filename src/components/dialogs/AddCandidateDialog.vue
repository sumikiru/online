<script setup>
import { reactive, ref, inject } from 'vue';
import { provideIsAddCandidateDialogOpen } from '@/components/keys.js';

const DialogVisible = inject(provideIsAddCandidateDialogOpen, false);
const form = reactive({
  stuNumber: '',
  testNumber: '',
});
</script>

<template>
  <el-dialog v-model="DialogVisible" title="添加考生到考试" width="25%">
    <el-form :model="form" label-width="auto">
      <el-form-item label="考生学号">
        <el-input v-model="form.stuNumber" autocomplete="off" />
      </el-form-item>
      <el-form-item label="考试编号">
        <el-input v-model="form.testNumber" autocomplete="off" />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="AddCandidateDialog-footer">
        <el-button @click="DialogVisible = false">取消</el-button>
        <el-button type="primary" @click="DialogVisible = false">添加</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped lang="scss"></style>
