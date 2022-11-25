import axios from 'axios'
import { Message } from 'element-ui'


// create an axios instance
const get = axios.create({
    baseURL: "http://localhost:8080/", // url = base url + request url
    // withCredentials: true, // send cookies when cross-domain requests
    timeout: 5000, // request timeout
    method:"get"
})
const post=axios.create({
    baseURL: "http://localhost:8080/", // url = base url + request url
    // withCredentials: true, // send cookies when cross-domain requests
    timeout: 5000, // request timeout
    method:"post"
})
const resinter=response=>{
    const res = response.data
    if (res.code !== 40000) {
        Message({
            message: res.message || 'Error',
            type: 'error',
            duration: 5 * 1000
        })
        return Promise.reject(new Error(res.message || 'Error'))
    } else {
        return res
    }
};
const errinter=error => {
    console.log('err:' + error) // for debug
    Message({
        message: error.message,
        type: 'error',
        duration: 5 * 1000
    })
    return Promise.reject(error)
}
post.interceptors.response.use(resinter,errinter);
// response interceptor
get.interceptors.response.use(resinter,errinter
)

export default 
{
    get,post
}