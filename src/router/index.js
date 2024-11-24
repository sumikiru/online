import { createRouter } from 'vue-router';
import { createWebHistory } from 'vue-router';

const routes = [
  {
    path: '/',
    alias: ['/index', '/login'],
    component: () => import('@/views/Login.vue'),
  },
  {
    path: '/manager', //http://localhost:5173/manager
    component: () => import('@/views/manager.vue'),
    children: [
      {
        path: '', //http://localhost:5173/manager
        component: () => import('@/views/Manager/default.vue'),
      },
      {
        path: '/exam', //http://localhost:5173/manager/exam
        component: () => import('@/views/Manager/exam.vue'),
      },
    ],
  },
  {
    path: '/administrator',
    component: () => import('@/views/Administrator/Administrator.vue'), // 新增的路由
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
