import req from '@/utils/request'
export function api_getAnswer(que)
{
    return req({
        url:'answer/get',
        method:'get',
        params:{
            'ques':que
        }
    })
}