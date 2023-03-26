import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'
import { title } from "@/settings";

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/register/index'),
    hidden: true
  },
  {
    path: '/findPassword',
    component: () => import('@/views/PasswordChange/UnLogined.vue'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: '个人主页',
    meta: { title: '个人主页', icon: 'el-icon-user' },
    children: [{
      path: 'dashboard',
      name: '个人信息',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '个人信息', icon: 'el-icon-edit' }
    },
    {
      path: 'password',
      name: '修改密码',
      component: () => import('@/views/PasswordChange/Logined.vue'),
      meta: { title: "修改密码", icon: 'el-icon-s-check' }
    }

    ]
  },

  {
    path: '/resource',
    component: Layout,
    redirect: '/resource/list',
    name: '资源',
    meta: { title: '资源管理', icon: 'el-icon-picture' },
    children: [
      {
        path: 'list',
        name: '资源列表',
        component: () => import('@/views/resource/list.vue'),
        meta: { title: '资源列表', icon: 'el-icon-folder' }
      },
      {
        path: 'upload',
        name: '资源上传',
        component: () => import('@/views/resource/upload.vue'),
        meta: { title: '资源上传', icon: 'el-icon-upload' }
      }
    ]
  },

  {
    path: '/tag',
    name: '标签管理',
    meta: { title: '标签管理', icon: 'el-icon-price-tag' },
    component: Layout,
    children: [
      {
        path: 'list',
        name: '标签列表',
        component: () => import('@/views/tag/list.vue'),
        meta: { title: '标签列表', icon: 'el-icon-s-order' }
      }
    ]
  },

  {
    path: '/question',
    component: Layout,
    name: 'Question',
    meta: {
      title: '题库',
      icon: 'nested'
    },
    children: [
      {
        path: 'edit',
        component: () => import('@/views/question/edit.vue'), // Parent router-view
        name: '新增题目',
        meta: { title: '新增题目', icon: 'el-icon-plus' }
      },
      {
        path: 'list',
        component: () => import('@/views/question/list.vue'),
        name: '题库列表',
        meta: { title: '题库列表', icon: 'el-icon-document' }
      }
    ]
  },
  {
    path:'/exam',
    component:Layout,
    meta:{
      title:"考试管理",
      icon:'el-icon-document'
    },
    children:[
      {
        path: 'list',
        component:() => import('@/views/exam/list.vue'),
        meta:{ title:'考试列表',}
      },
      {
        path:'upload',
        component:()=>import('@/views/exam/upload.vue'),
        meta:{
          title:"考试上传",
        }
      }
    ]
  },
  {
    path: '/userlist',
    component: Layout,
    meta: {
      title: '用户管理',
      icon: 'el-icon-user-solid'
    },
    children: [
      {
        path: 'mananger',
        component: () => import('@/views/userList/index.vue'),
        meta: {
          title: '用户管理',
          icon: 'el-icon-user-solid'
        },
      }
    ]
  },
  {
    path: '/logs',
    name: '系统日志',
    meta: { title: '系统日志', icon: 'el-icon-set-up' },
    component: Layout,
    children: [
      {
        path: 'list',
        name: '系统日志',
        component: () => import('@/views/log/index.vue'),
        meta: { title: '系统日志', icon: 'el-icon-s-order' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
