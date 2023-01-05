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
export function pullUserList(id,from,num)
{
  return request({
    url:'user/pullusers',
    method:'get',
    params:{
      'id':id,
      'from':from,
      'num':num
    }
  })
}
export function allUserCount()
{
  return request({
    url:'user/count',
    method:'get'
  });
}
export function updateLevel(id,target,level)
{
  return request({
    url:'user/updatelevel',
    method:'get',
    params:{
      'id':id,'target':target,'lv':level
    }
  })
}

export function logout() {
  return request({
    url: '/vue-admin-template/user/logout',
    method: 'post'
  })
}
