import req from '@/utils/request'

export function api_log(f,v)
{
    return req({
        url:'log/get',
        method:'get',
        params:{
            field:f,
            val:v
        }
    })
}