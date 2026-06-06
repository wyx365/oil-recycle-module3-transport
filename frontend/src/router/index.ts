/**
 * 模块1：用户认证与权限菜单。
 */
import {createRouter, createWebHistory, type RouteRecordRaw} from 'vue-router'
import {useUserStore} from '@/stores/user'

// 路由说明：路由 meta.title 已统一改为中文，通用 CRUD 页面直接展示中文标题。
const routes: RouteRecordRaw[] = [
    // ==================== 模块1：用户认证与权限菜单 ====================
    {path: '/login', component: () => import('@/views/auth/Login.vue')},
    {path: '/register', component: () => import('@/views/auth/Register.vue')},
    {
        path: '/', component: () => import('@/layouts/MainLayout.vue'), redirect: '/dashboard', children: [
            // ==================== 模块5：系统管理与基础支撑 ====================
            {path: 'dashboard', component: () => import('@/views/dashboard/Dashboard.vue')},
            // ==================== 模块1：用户认证与权限菜单 ====================
            {path: 'profile', component: () => import('@/views/system/Profile.vue')},
            // ==================== 模块4：积分商城与内容互动 ====================
            {path: 'articles', component: () => import('@/views/article/ArticleList.vue')},
            {path: 'articles/:id', component: () => import('@/views/article/ArticleDetail.vue')},
            // ==================== 模块2：回收预约与站点库存 ====================
            {path: 'user/appointment', component: () => import('@/views/user/Appointment.vue')},
            {path: 'user/orders', component: () => import('@/views/user/MyOrders.vue')},
            // ==================== 模块4：积分商城与内容互动 ====================
            {
                path: 'user/points',
                component: () => import('@/views/common/CrudPage.vue'),
                meta: {title: '积分明细', api: '/api/points'}
            },
            {path: 'user/mall', component: () => import('@/views/user/Mall.vue')},
            {path: 'user/evaluation', component: () => import('@/views/user/Evaluation.vue')},
            {path: 'user/feedback', component: () => import('@/views/user/Feedback.vue')},
            {path: 'user/report', component: () => import('@/views/user/Report.vue')},
            // ==================== 模块2：回收预约与站点库存 ====================
            {path: 'station/pending-orders', component: () => import('@/views/station/PendingOrders.vue')},
            {path: 'station/storage', component: () => import('@/views/station/Storage.vue')},
            // ==================== 模块3：无人机转运与接口监控 ====================
            {path: 'station/transport-create', component: () => import('@/views/station/TransportCreate.vue')},
            // ==================== 模块2：回收预约与站点库存 ====================
            {
                path: 'station/ledger',
                component: () => import('@/views/common/CrudPage.vue'),
                meta: {title: '库存出入库台账', api: '/api/storage/records'}
            },
            {path: 'admin/stations', component: () => import('@/views/admin/Stations.vue')},
            // ==================== 模块4：积分商城与内容互动 ====================
            {path: 'admin/articles', component: () => import('@/views/admin/Articles.vue')},
            // ==================== 模块3：无人机转运与接口监控 ====================
            {path: 'admin/transport', component: () => import('@/views/admin/TransportTasks.vue')},
            // ==================== 模块4：积分商城与内容互动 ====================
            {
                path: 'admin/reports',
                component: () => import('@/views/common/CrudPage.vue'),
                meta: {title: '违规举报处理', api: '/api/reports'}
            },
            {
                path: 'admin/activities',
                component: () => import('@/views/common/CrudPage.vue'),
                meta: {title: '环保活动管理', api: '/api/activities'}
            },
            {
                path: 'admin/point-rules',
                component: () => import('@/views/common/CrudPage.vue'),
                meta: {title: '积分规则配置', api: '/api/point-rules'}
            },
            {
                path: 'admin/goods',
                component: () => import('@/views/common/CrudPage.vue'),
                meta: {title: '积分商城商品管理', api: '/api/mall'}
            },
            // ==================== 模块1：用户认证与权限菜单 ====================
            {
                path: 'super/users',
                component: () => import('@/views/common/CrudPage.vue'),
                meta: {title: '用户账号管理', api: '/api/super/users'}
            },
            {
                path: 'super/roles',
                component: () => import('@/views/common/CrudPage.vue'),
                meta: {title: '角色权限管理', api: '/api/super/roles'}
            },
            {
                path: 'super/menus',
                component: () => import('@/views/common/CrudPage.vue'),
                meta: {title: '菜单权限管理', api: '/api/super/menus'}
            },
            // ==================== 模块5：系统管理与基础支撑 ====================
            {
                path: 'super/logs',
                component: () => import('@/views/common/CrudPage.vue'),
                meta: {title: '系统操作日志', api: '/api/super/logs'}
            },
            {
                path: 'super/interface-status',
                component: () => import('@/views/common/CrudPage.vue'),
                meta: {title: '接口对接状态监控', api: '/api/super/interface-status'}
            },
            {path: '403', component: () => import('@/views/system/Forbidden.vue')}
        ]
    },
    {path: '/:pathMatch(.*)*', component: () => import('@/views/system/NotFound.vue')}
]

const router = createRouter({history: createWebHistory(), routes})

// 路由守卫，未登录跳转登录页，已登录时自动加载用户信息和菜单。
router.beforeEach(async (to) => {
    const store = useUserStore()
    if (!store.token && to.path !== '/login' && to.path !== '/register') return '/login'
    if (store.token && !store.user && to.path !== '/login') {
        try {
            await store.loadInfo()
        } catch {
            store.logout();
            return '/login'
        }
    }
    return true
})
export default router
