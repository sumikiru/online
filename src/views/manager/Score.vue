<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.name" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入试卷名称查询"></el-input>
      <el-input v-model="data.courseName" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入课程名称查询"></el-input>
      <el-input v-model="data.status" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入阅卷状态查询"></el-input>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData">
        <el-table-column prop="name" label="试卷名称" show-overflow-tooltip/>
        <el-table-column prop="courseName" label="课程名称" show-overflow-tooltip/>
        <el-table-column prop="teacherName" label="授课教师" show-overflow-tooltip v-if="data.user.role === 'ADMIN'"/>
        <el-table-column prop="studentName" label="学生姓名" show-overflow-tooltip/>
        <el-table-column prop="status" label="试卷状态" show-overflow-tooltip>
          <template v-slot="scope">
            <el-tag v-if="scope.row.status === '已阅卷'" type="success">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '待阅卷'" type="danger">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="分数" show-overflow-tooltip/>
        <el-table-column label="操作" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" @click="handleEdit(scope.row)">阅卷</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <el-dialog title="阅卷打分" v-model="data.formVisible" width="40%" destroy-on-close>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="update">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";


const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  name: null,
  courseName: null,
  status: null
})

const load = () => {
  request.get('/score/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      courseName: data.courseName,
      status: data.status
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const update = () => {
  request.put('/score/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    }
  })
}

const reset = () => {
  data.name = null
  data.courseName = null
  data.status = null
  load()
}

load()
</script>