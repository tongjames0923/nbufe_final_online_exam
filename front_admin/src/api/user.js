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
  if(i)
  {
    return request({
      url:"user/getUser",
      method:'get',
      params:{
        id:i
      }
    })
  }
  else
  {
    return request({
      url:"user/getUser",
      method:'get',
      params:{
      }
    })
  }

}
export function pullUserList(id,from,num)
{
  return request({
    url:'user/pullusers',
    method:'get',
    params:{
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
export function setDetail(info)
{
  return request({
    url:'user/updatedetails',
    method:'get',
    params:{
      'id': info.id,
      'email': info.email,
      'phone': info.phone,
      'address': info.address,
      'note': info.note
    }
  })
}
export function changePasswordByOld(id,old,newone)
{
  return request({
    url:'user/updatePassword',
    method:'get',
    params:{
      'id': id,
      'password': newone,
      'oldpassword': old
    }})
}
export function getQues(name)
{
  return request({
    url:'user/secQues',
    method:'get',
    params:{
      'name': name
    }})
}
export function replyAns(name,ans)
{
  return request({
    url:'user/answerSec',
    method:'get',
    params:{
      'name': name,
      'answer':ans
    }});
}
export function changePasswordBySec(name,password,ans)
{
  return request({
    url:'user/findpdbyques',
    method:'get',
    params:{
      'name': name,
      'password': password,
      'answer':ans
    }
  })
}
export function api_log_out(token)
{
  return request({
    url:'user/logout',
    method:'get',
    params:{
      access:token
    }
  })
}
export function api_renew_access()
{
  return request({
    url:"user/renew",
    method:'get'
  })
}
export function api_findUserByName(name)
{
  return request({
    url:"user/findUser",
    method:'get',
    params:{
      'name':name
    }
  })
}
export function api_changeSec(sec,ans)
{
  return request({
    url:'user/updateSecQues',
    method:'get',
    params:{
      'ques':sec,
      'ans':ans
    }
  })
}