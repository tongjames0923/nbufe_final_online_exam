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
  return Cookies.get(AccessKey)
}

export function setAccess(token) {
  return Cookies.set(AccessKey, token)
}

export function removeAccess() {
  return Cookies.remove(AccessKey)
}