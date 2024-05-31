import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

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
 * constantRoutes   不需要权限  谁都能访问
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
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/indexOld'),
      meta: { title: 'home page', icon: 'dashboard', affix: true }
    }]
  },


  // {
  //   path: '/form',
  //   component: Layout,
  //   children: [
  //     {
  //       path: 'index',
  //       name: 'Form',
  //       component: () => import('@/views/form/index'),
  //       meta: { title: 'Form', icon: 'form' }
  //     }
  //   ]
  // }
]

/**
 * asyncRoutes   需要权限才能访问
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [
  // {
  //   path: '/readerDashboard',
  //   component: Layout,
  //   alwaysShow: true,
  //   // redirect: '/example/table',
  //   name: 'readerDashboard',
  //   meta: { title: 'home', icon: 'dashboard' },
  //   children: [
  //     {
  //       path: '/index',
  //       name: 'index',
  //       component: () => import('@/views/readerDashboard/index'),
  //       meta: { title: 'reader home', icon: 'dashboard' }
  //     }
  //   ]
  // },
  // {
  //   path: '/',
  //   component: Layout,
  //   redirect: '/dashboard/index',
  //   children: [
  //     {
  //       path: '/index',
  //       name: 'index',
  //       component: () => import('@/views/dashboard/index'),
  //       meta: { title: 'home page', icon: 'dashboard', affix: true }
  //     },
  //     {
  //       path: '/readerIndex',
  //       name: 'readerIndex',
  //       component: () => import('@/views/dashboard/readerIndex'),
  //       meta: { title: 'home page', icon: 'dashboard', affix: true }
  //     }]
  // },
  {
    path: '/system',
    component: Layout,
    alwaysShow: true,
    // redirect: '/example/table',
    name: 'system',
    meta: { title: 'system control', icon: 'el-icon-s-help' },
    children: [
      {
        path: '/sysUserList',
        name: 'sysUserList',
        component: () => import('@/views/system/sysUserList'),
        meta: { title: 'user control', icon: 'table' }
      },
      {
        path: '/sysRoleList',
        name: 'sysRoleList',
        component: () => import('@/views/system/sysRoleList'),
        meta: { title: 'role control', icon: 'tree' }
      },
      {
        path: '/sysMenuList',
        name: 'sysMenuList',
        component: () => import('@/views/system/sysMenuList'),
        meta: { title: 'menu control', icon: 'tree' }
      }
    ]
  },
  {
    path: '/member',
    component: Layout,
    alwaysShow: true,
    // redirect: '/example/table',
    name: 'member',
    meta: { title: 'member control', icon: 'el-icon-s-help' },
    children: [
      {
        path: '/memberList',
        name: 'memberList',
        component: () => import('@/views/member/memberList'),
        meta: { title: 'member list', icon: 'table' }
      }
    ]
  },
  {
    path: '/book',
    component: Layout,
    alwaysShow: true,
    // redirect: '/example/table',
    name: 'book',
    meta: { title: 'book control', icon: 'el-icon-s-help' },
    children: [
      {
        path: '/bookCategory',
        name: 'bookCategory',
        component: () => import('@/views/book/bookCategory'),
        meta: { title: 'book category', icon: 'table' }
      },
      {
        path: '/bookList',
        name: 'bookList',
        component: () => import('@/views/book/bookList'),
        meta: { title: 'book list', icon: 'tree' }
      }
    ]
  },
  {
    path: '/borrow',
    component: Layout,
    alwaysShow: true,
    name: 'borrow',
    meta: { title: 'BR control', icon: 'el-icon-s-help' },
    children: [
      {
        path: '/bookBorrow',
        name: 'bookBorrow',
        component: () => import('@/views/borrow/bookBorrow'),
        meta: { title: 'borrow control', icon: 'table' }
      },
      {
        path: '/bookReturn',
        name: 'bookReturn',
        component: () => import('@/views/borrow/bookReturn'),
        meta: { title: 'return control', icon: 'table' }
      },
      {
        path: '/borrowLook',
        name: 'borrowLook',
        component: () => import('@/views/borrow/borrowLook'),
        meta: { title: 'look borrow', icon: 'el-icon-s-cooperation' }
      },
      {
        path: '/borrowLookMember',
        name: 'borrowLookMember',
        component: () => import('@/views/borrow/borrowLookMember'),
        meta: { title: 'borrow look', icon: 'el-icon-s-cooperation' }
      }
    ]
  },
  {
    path: '/fine',
    component: Layout,
    alwaysShow: true,
    // redirect: '/example/table',
    name: 'fine',
    meta: { title: 'fine control', icon: 'el-icon-s-help' },
    children: [
      {
        path: '/payFine',
        name: 'payFine',
        component: () => import('@/views/fine/payFine'),
        meta: { title: 'pay fine', icon: 'table' }
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
