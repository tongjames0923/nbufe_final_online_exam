import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getAccess, getToken } from '@/utils/auth'
import { Server } from '@/settings'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
NProgress.configure({ showSpinner: false }) // NProgress Configuration
const se = Server;
// create an axios instance
const service = axios.create({
  baseURL: se, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 1000 * 60 * 2 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    if(!NProgress.isStarted())
    NProgress.start();
    const access = getAccess();
    if (access) {
      config.headers['X-TOKEN'] = access
    }
    return config
  },
  error => {
    if(NProgress.isStarted())
    NProgress.done();
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data
    if (res.code !== 40000) {
      Message({
        message: res.message || 'Error',
        type: res.code > 40000 ? 'error' : 'warning',
        duration: res.code == 39999 ? 500 : 5 * 1000,
        showClose:true,
        onClose: () => {
          if (res.code == 39999) {
            store.dispatch('user/resetToken').then(() => {
              location.reload()
            })
          }
        }
      })
      // // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      // if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
      //   // to re-login
      //   MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
      //     confirmButtonText: 'Re-Login',
      //     cancelButtonText: 'Cancel',
      //     type: 'warning'
      //   }).then(() => {
      //     store.dispatch('user/resetToken').then(() => {
      //       location.reload()
      //     })
      //   })
      // }
      if(NProgress.isStarted())
      NProgress.done();
      return Promise.reject(new Error(res.message || '网络错误'))
    } else {
      if(NProgress.isStarted())
      NProgress.done();
      return res.data
    }
  },
  error => {
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000,
      showClose:true
    })
    if(NProgress.isStarted())
    NProgress.done();
    return Promise.reject(error)
  }
)

export default service
