
import req from '@/utils/request'
export function api_uploadExam(u,c,s,q)
{

    return req({
        url:'exam/upload',
        method:'post',
        params:{user:u},
        data:{
            exam_name:c.exam_name,
            exam_begin:c.exam_begin,
            exam_len:c.exam_len,
            exam_note:c.exam_note,
            students:s,
            questions:q
        }
    })
}
export function api_count(user)
{
    return req({url:'exam/count',method:'get',params:{'user':user}})
}
export function api_list(from,num)
{
    return req({url:'exam/list',method:'get',params:{'from':from,'num':num}})
}
export function api_updateNote(v,u,e)
{
return req({url:'exam/updateNote',method:'get',params:{'note':v,'user':u,'examid':e}});
}
export function api_updateName(v,u,e)
{
    return req({url:'exam/updateName',
    method:'get',
    params:{
        'name':v,
        'user':u,
        'examid':e
    }
})
}
export function api_updateLen(v,u,e)
{
    return req({url:'exam/updateLen',
    method:'get',
    params:{
        'len':v,
        'user':u,
        'examid':e
    }
})
}
export function api_updateBegin(v,u,e)
{
    return req({url:'exam/updateBegin',
    method:'get',
    params:{
        'time':v,
        'user':u,
        'examid':e
    }
})
}