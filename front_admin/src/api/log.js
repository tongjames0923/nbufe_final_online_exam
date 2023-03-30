import req from '@/utils/request'

export function api_log(f,v,p)
{
    return req({
        url:'log/get',
        method:'get',
        params:{
            field:f,
            val:v,
            maxpage:p
        }
    })
}
export function api_log_top(n)
{
    return req({
        url:'log/top',
        method:'get',
        params:{
            'num':n
        }
    })
}