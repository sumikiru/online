<template>
  <div style="width: 40%; margin: 30px auto">
    <div style="font-size: 20px; font-weight: bold; text-align: center">{{ data.testPaperData.name }}</div>
    <div style="margin-top: 15px; color: #666666; text-align: center">
      <span>课程名称：{{ data.testPaperData.courseName }}</span>
      <span style="margin: 0 30px">授课教师：{{ data.testPaperData.teacherName }}</span>
      <span>考试时间：{{ data.testPaperData.time }} 分钟</span>
    </div>
    <div style="margin-top: 10px;text-align: center;color:red">
    考试倒计时：{{ data.hour }}时：{{data.minutes}}分：{{data.seconds}秒
    </div>
    <div style="margin-top: 50px">
      <div v-for="item in data.testPaperData.questions" style="margin-bottom: 20px">
        <div style="font-weight: bold; font-size: 16px; background-color: #ddf1ec; line-height: 30px; padding: 5px; margin-bottom: 10px">{{ item.name }}（{{ item.typeScore }} 分）</div>
        <!--   题目下面要根据具体的题型来设计具体的样式了     -->
        <!--   typeId === 1：单选题     -->
        <div v-if="item.typeId === 1">
          <el-radio-group v-model="item.newAnswer">
            <el-radio value="A">A. {{ item.optionA }}</el-radio>
            <el-radio value="B">B. {{ item.optionB }}</el-radio>
            <el-radio value="C">C. {{ item.optionC }}</el-radio>
            <el-radio value="D">D. {{ item.optionD }}</el-radio>
          </el-radio-group>
        </div>
        <!--   typeId === 2：多选题     -->
        <div v-if="item.typeId === 2">
          <el-checkbox-group v-model="item.checkList">
            <el-checkbox :label="'A. ' + item.optionA" value="A" />
            <el-checkbox :label="'B. ' + item.optionB" value="B" />
            <el-checkbox :label="'C. ' + item.optionC" value="C" />
            <el-checkbox :label="'D. ' + item.optionD" value="D" />
          </el-checkbox-group>
        </div>
        <!--   typeId === 3：判断题     -->
        <div v-if="item.typeId === 3">
          <el-radio-group v-model="item.newAnswer">
            <el-radio value="正确">A. 正确</el-radio>
            <el-radio value="错误">B. 错误</el-radio>
          </el-radio-group>
        </div>
        <!--   typeId === 4：填空题     -->
        <div v-if="item.typeId === 4">
          <el-input style="width: 50%" v-model="item.newAnswer" placeholder="请输入您的答案"></el-input>
        </div>
        <!--   typeId === 5：简答题     -->
        <div v-if="item.typeId === 5">
          <el-input type="textarea" :rows="8" v-model="item.newAnswer" placeholder="请输入您的答案"></el-input>
        </div>
      </div>
      <div style="text-align: center; margin: 50px">
        <el-button style="padding: 20px 40px" type="primary" @click="submitPaper">提交试卷</el-button>
      </div>
    </div>
  </div>
</template>
<script setup>
import { reactive, onMounted } from 'vue';
import request from '@/utils/request.js';
import router from '@/router/index.js';
import { ElMessage } from 'element-plus';
import date from "async-validator/dist-types/validator/date.js";

const data = reactive({
  testPaperId: router.currentRoute.value.query.id,
  testPaperData: {},
  hour:0,
  minutes:0,
  seconds:0,
});
let time

const loadTestPaper = () => {
  data.testPaperId = router.currentRoute.value.query.id;
  request.get('/testPaper/selectById/' + data.testPaperId).then((res) => {
    if (res.code === '200') {
      data.testPaperData = res.data;
      time=setInterval(() =>{
        let maxTime=data.testPaperData.maxTime
        if (maxTime >= 0){
          let remain = Math.floor(maxTime % 3600)
          data.hour = Math.floor(maxTime / 3600)
          data.minutes = Math.floor(remain / 60)
          data.seconds = Math.floor(remain % 60)
          maxTime--
        }else {
          ElMessage.error('时间到')
          submitPaper()
        }


      },1000)

    } else {
      ElMessage.error(res.msg);
    }
  });
};
const  submitPaper = () => {
  clearInterval(time)
//提交试卷
}

loadTestPaper();
</script>
<style scoped>
.el-radio-group {
  display: block;
}
.el-radio {
  display: block;
}
.el-checkbox {
  display: block;
}
</style>
