import request from '@/utils/request'
import { getToken } from '@/utils/auth'

export function create_ques(quesData)
{
    return request({url:'question/create',
method:'post',data:quesData});
}
export function CountQues()
{
    return request({  url: "question/questionCount",method:'get' });
}
export function list(from,num)
{
    return request({url:'question/list',method:'get',params:{
        'from':from,
        'num':num
    }})
}
export function changePublic(id,pb)
{
    return request({url:'question/updatePublic',params:{
        ques:id,publicable:pb
    }})
}
export function questionBody(id)
{
    return request({url:'file/question',method:'get',params:{'id':id}});
}