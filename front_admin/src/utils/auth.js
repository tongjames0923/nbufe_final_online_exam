import Cookies from 'js-cookie'

const TokenKey = 'vue_admin_template_token'
const AccessKey="tbs_access"

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}
export function getAccess()
{
  const obj= localStorage.getItem(AccessKey)
  return obj==='null'?null:obj;
}

export function setAccess(token) {
  return localStorage.setItem(AccessKey,token)
}

export function removeAccess() {
  return localStorage.removeItem(AccessKey)
}
export function getTokenObj()
{
  let u=undefined;
  try
  {
    u=JSON.parse(getToken());
  }catch(e)
  {

  }
  return u;
}