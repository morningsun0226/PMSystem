import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: () => import('../views/Manage.vue'),
    redirect: '/home',
    children: [
      {path: 'home', name: '首页', component: () => import('../views/Home.vue')},
      {path: 'person', name: '个人信息', component: () => import('../views/Person.vue')},
      {path: 'user', name: '用户管理', component: () => import('../views/User.vue')},
      {path: 'employee', name: '员工管理', component: () => import('../views/Employee.vue')},
      {path: 'department', name: '公司管理', component: () => import('../views/Department.vue')},
      {path: 'salary', name: '工资管理', component: () => import('../views/Salary.vue')},
    ]
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('../views/About.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from,next) => {
  localStorage.setItem("currentPathName", to.name);
  store.commit("setPath");
  next();
})

export default router
