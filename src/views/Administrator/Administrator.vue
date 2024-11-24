<template>
  <div class="sidebar">
    <el-form label-width="0" label-position="left">
      <el-form-item id="userAvatar">
        <el-avatar size="large" shape="circle" fit="cover">
          <img
            src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
            alt="用户头像占位符,点击跳转到设置"
          />
        </el-avatar>
        <el-text size="large"> 欢迎回来</el-text>
      </el-form-item>
      <br />

      <el-form-item id="adminManager" style="align-content: center">
        <el-collapse style="width: 250px">
          <el-collapse-item title="学生管理" name="1">
            <el-button flex="1">添加学生</el-button>
            <el-button>通知提醒</el-button>
          </el-collapse-item>
          <el-collapse-item title="成绩管理" name="2">
            <el-button>查看成绩</el-button>
            <el-button>导出成绩</el-button>
          </el-collapse-item>
          <el-collapse-item title="成绩分析" name="3">
            <el-button>统计分析</el-button>
            <el-button>题库分析</el-button>
          </el-collapse-item>
        </el-collapse>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'Administrator',
  data() {
    return {
      // 可以在这里定义组件的数据
    };
  },
  methods: {
    // 可以在这里定义组件的方法
  },
};
</script>

<style scoped lang="scss">
.el-collapse__wrap {
  display: flex;
}
.el-collapse-item .el-button {
  flex: 1;
  margin: 0;
  width: 100%;
  height: fit-content;
}
</style>
