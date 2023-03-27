import defaultSettings from '@/settings'
import { getToken } from './auth'

const title = defaultSettings.title || '在线考试'

export default function getPageTitle(pageTitle) {
  let u=getToken()
  if(u)
  {
    try{
      u=JSON.parse(u);
    }catch(e)
    {
      u=undefined
    }
  }
  if (pageTitle) {
    return `${u?u.name:''}|${pageTitle} - ${title}`
  }
  return `${title}`
}
