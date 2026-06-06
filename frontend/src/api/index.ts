/**
 * 模块5：系统管理与基础支撑。
 */
import request from './request'

// ==================== 模块1：用户认证与权限菜单 ====================
// 认证相关接口，包含登录、注册、当前用户信息和动态菜单。
export const authApi = {
    login: (data: any) => request.post('/api/auth/login', data),
    register: (data: any) => request.post('/api/auth/register', data),
    info: () => request.get('/api/auth/info'),
    menu: () => request.get('/api/auth/menu')
}

// ==================== 模块5：系统管理与基础支撑 ====================
// 数据看板接口。
export const dashboardApi = {overview: () => request.get('/api/admin/dashboard')}

// ==================== 模块2：回收预约与站点库存 ====================
// 回收预约订单接口，覆盖创建、接单、上门、完成和取消状态流转。
export const orderApi = {
    page: (params?: any) => request.get('/api/orders', {params}),
    create: (data: any) => request.post('/api/orders', data),
    accept: (id: number) => request.post(`/api/orders/${id}/accept`),
    picking: (id: number) => request.post(`/api/orders/${id}/picking`),
    complete: (id: number, data: any) => request.post(`/api/orders/${id}/complete`, data),
    cancel: (id: number) => request.post(`/api/orders/${id}/cancel`)
}

// ==================== 模块2：回收预约与站点库存 ====================
// 回收站点管理接口。
export const stationApi = {
    page: (params?: any) => request.get('/api/stations', {params}),
    save: (data: any) => request.post('/api/stations', data),
    update: (id: number, data: any) => request.put(`/api/stations/${id}`, data),
    remove: (id: number) => request.delete(`/api/stations/${id}`)
}

// ==================== 模块2：回收预约与站点库存 ====================
// 库存和库存台账接口。
export const storageApi = {
    page: (params?: any) => request.get('/api/storage', {params}),
    records: (params?: any) => request.get('/api/storage/records', {params})
}

// ==================== 模块4：积分商城与内容互动 ====================
// 积分商城兑换接口。
export const exchangeApi = {
    redeem: (goodsId: number) => request.post(`/api/exchanges/redeem/${goodsId}`)
}

// ==================== 模块5：系统管理与基础支撑 ====================
// 本地文件上传接口，返回可直接访问的 URL。
export const uploadApi = {
    image: (file: File) => {
        const form = new FormData()
        form.append('file', file)
        return request.post('/api/files/upload', form, {headers: {'Content-Type': 'multipart/form-data'}})
    }
}

// ==================== 模块3：无人机转运与接口监控 ====================
// 无人机转运任务接口，包含创建、推送和模拟回调。
export const transportApi = {
    page: (params?: any) => request.get('/api/transport-tasks', {params}),
    create: (data: any) => request.post('/api/transport-tasks', data),
    push: (id: number) => request.post(`/api/transport-tasks/${id}/push`),
    callback: (data: any) => request.post('/api/drone/callback', data)
}

// ==================== 模块5：系统管理与基础支撑 ====================
// 通用 CRUD 接口工厂，用于文章、活动、举报、系统管理等页面快速复用。
export const crudApi = (base: string) => ({
    page: (params?: any) => request.get(base, {params}),
    get: (id: number) => request.get(`${base}/${id}`),
    save: (data: any) => request.post(base, data),
    update: (id: number, data: any) => request.put(`${base}/${id}`, data),
    remove: (id: number) => request.delete(`${base}/${id}`)
})
