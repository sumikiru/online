<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.name" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入课程名称查询"></el-input>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button v-if="data.user.role === 'TEACHER'" type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="img" label="课程封面">
          <template v-slot="scope">
            <el-image style="width: 60px; height: 40px; border-radius: 5px; display: block" v-if="scope.row.img" :src="scope.row.img" :preview-src-list="[scope.row.img]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="课程名称" />
        <el-table-column prop="teacherName" label="授课教师" />
        <el-table-column prop="score" label="课程学分" />
        <el-table-column label="操作" width="100" fixed="right">
          <template v-slot="scope">
            <el-button v-if="data.user.role === 'TEACHER'" type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <el-dialog title="课程信息" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="70px" style="padding: 20px">
        <el-form-item prop="avatar" label="课程封面">
          <el-upload :action="baseUrl + '/files/upload'" :before-upload="beforeAvatarUpload" :on-success="handleFileUpload" list-type="picture">
            <el-button type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="name" label="课程名称">
          <el-input v-model="data.form.name" placeholder="请输入课程名称"></el-input>
        </el-form-item>
        <el-form-item prop="score" label="课程学分">
          <el-input v-model="data.form.score" placeholder="请输入课程学分"></el-input>
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
import { reactive } from 'vue';
import request from '@/utils/request.js';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Delete, Edit } from '@element-plus/icons-vue';
const baseUrl = import.meta.env.VITE_BASE_URL;

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  name: null,
  ids: [],
});

const load = () => {
  request
    .get('/course/selectPage', {
      params: {
        pageNum: data.pageNum,
        pageSize: data.pageSize,
        name: data.name,
      },
    })
    .then((res) => {
      if (res.code === '200') {
        data.tableData = res.data?.list || [];
        data.total = res.data?.total;
      } else {
        ElMessage.error(res.msg);
      }
    });
};
const handleAdd = () => {
  data.form = {};
  data.formVisible = true;
};
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row));
  data.formVisible = true;
};
const add = () => {
  request.post('/course/add', data.form).then((res) => {
    if (res.code === '200') {
      ElMessage.success('操作成功');
      data.formVisible = false;
      load();
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const update = () => {
  request.put('/course/update', data.form).then((res) => {
    if (res.code === '200') {
      ElMessage.success('操作成功');
      data.formVisible = false;
      load();
    }
  });
};

const save = () => {
  data.form.id ? update() : add();
};

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' })
    .then((res) => {
      request.delete('/course/delete/' + id).then((res) => {
        if (res.code === '200') {
          ElMessage.success('删除成功');
          load();
        } else {
          ElMessage.error(res.msg);
        }
      });
    })
    .catch((err) => {
      console.error(err);
    });
};
const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning('请选择数据');
    return;
  }
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' })
    .then((res) => {
      request.delete('/course/delete/batch', { data: data.ids }).then((res) => {
        if (res.code === '200') {
          ElMessage.success('操作成功');
          load();
        } else {
          ElMessage.error(res.msg);
        }
      });
    })
    .catch((err) => {
      console.error(err);
    });
};
const handleSelectionChange = (rows) => {
  data.ids = rows.map((v) => v.id);
};

const reset = () => {
  data.name = null;
  load();
};

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

        // 设置裁剪区域和输出大小：课程封面比例为 82:45，并保证图片为正中心部分
        const outputWidth = 800; // 输出宽度
        const outputHeight = Math.round((outputWidth * 45) / 82); // 按比例计算输出高度

        // 计算裁剪区域
        const imageAspectRatio = img.width / img.height; // 图片宽高比
        const targetAspectRatio = 82 / 45; // 目标宽高比

        let cropWidth, cropHeight, startX, startY;

        if (imageAspectRatio > targetAspectRatio) {
          // 图片宽度较大，以高度为基准裁剪
          cropHeight = img.height;
          cropWidth = Math.round(cropHeight * targetAspectRatio);
          startX = (img.width - cropWidth) / 2;
          startY = 0;
        } else {
          // 图片高度较大，以宽度为基准裁剪
          cropWidth = img.width;
          cropHeight = Math.round(cropWidth / targetAspectRatio);
          startX = 0;
          startY = (img.height - cropHeight) / 2;
        }

        // 设置 canvas 的输出尺寸
        canvas.width = outputWidth;
        canvas.height = outputHeight;

        // 裁剪并绘制到 canvas
        ctx.drawImage(
          img,
          startX,
          startY,
          cropWidth,
          cropHeight, // 裁剪区域
          0,
          0,
          outputWidth,
          outputHeight, // 绘制到 canvas 的区域
        );

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
  data.form.img = res.data;
  ElMessage.success('图片已成功上传，请点击保存');
};

load();
</script>
