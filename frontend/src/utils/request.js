import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建 axios 实例
const request = axios.create({
  baseURL: '/api',
  timeout: 15000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    
    // 如果响应状态码不是 200，说明有错误
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    
    return res
  },
  error => {
    console.error('请求错误:', error)
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default request
