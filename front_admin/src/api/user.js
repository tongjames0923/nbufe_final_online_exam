import request from '@/utils/request'

export function login(data) {
  return request({
    url: 'user/login',
    method: 'get',
    params: data
  })
}
export function register(data) {
  return request({
    url: 'user/register',
    method: 'post',
    data: data
  })
}
export function getInfo(token) {
  return request({
    url: '/vue-admin-template/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/vue-admin-template/user/logout',
    method: 'post'
  })
}
