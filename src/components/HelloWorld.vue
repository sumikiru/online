<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <el-button type="primary">点击我</el-button>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  data() {
    return {
      msg: '欢迎使用 Element Plus!'
    }
  }
}
</script>
