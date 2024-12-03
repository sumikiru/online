import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    //{ path: '/', redirect: '/manager/home' },错误，会导致退出登录时直接进入管理页
    { path: '/', redirect: '/login' }, //默认进入登录界面，退出后也会回到登录页
    {
      path: '/manager',
      component: () => import('@/views/Manager.vue'),
      children: [
        { path: 'home', meta: { name: '系统首页' }, component: () => import('@/views/manager/Home.vue') },
        { path: 'admin', meta: { name: '管理员信息' }, component: () => import('@/views/manager/Admin.vue') },
        { path: 'notice', meta: { name: '系统公告' }, component: () => import('@/views/manager/Notice.vue') },
        { path: 'person', meta: { name: '个人资料' }, component: () => import('@/views/manager/Person.vue') },
        { path: 'password', meta: { name: '修改密码' }, component: () => import('@/views/manager/Password.vue') },
        { path: 'teacher', meta: { name: '教师信息' }, component: () => import('@/views/manager/Teacher.vue') },
        { path: 'student', meta: { name: '学生信息' }, component: () => import('@/views/manager/Student.vue') },
        { path: 'examPlan', meta: { name: '考试安排' }, component: () => import('@/views/manager/ExamPlan.vue') },
        { path: 'questionType', meta: { name: '题型信息' }, component: () => import('@/views/manager/QuestionType.vue') },
        { path: 'course', meta: { name: '课程信息' }, component: () => import('@/views/manager/Course.vue') },
        { path: 'question', meta: { name: '题库信息' }, component: () => import('@/views/manager/Question.vue') },
        { path: 'testPaper', meta: { name: '试卷信息' }, component: () => import('@/views/manager/TestPaper.vue') },
        { path: 'score', meta: { name: '成绩管理' }, component: () => import('@/views/manager/Score.vue') },
      ],
    },
    {
      path: '/front',
      component: () => import('@/views/Front.vue'),
      children: [
        { path: 'home', redirect: '/front/exam' }, // 默认为在线考试
        { path: 'person', component: () => import('@/views/front/Person.vue') },
        { path: 'exam', component: () => import('@/views/front/Exam.vue') },
        { path: 'testPaper', component: () => import('@/views/front/TestPaper.vue') },
        { path: 'testPaperView', component: () => import('@/views/front/TestPaperView.vue') },
        { path: 'score', component: () => import('@/views/front/Score.vue') },
      ],
    },
    { path: '/login', component: () => import('@/views/Login.vue') },
    { path: '/register', component: () => import('@/views/Register.vue') },
    { path: '/404', component: () => import('@/views/404.vue') },
    { path: '/:pathMatch(.*)', redirect: '/404' },
  ],
});

export default router;
