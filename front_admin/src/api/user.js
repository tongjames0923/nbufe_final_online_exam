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
export function getUser(i) {
  return request({
    url:"user/getUser",
    method:'get',
    params:{id:i}
  })
}

export function logout() {
  return request({
    url: '/vue-admin-template/user/logout',
    method: 'post'
  })
}
