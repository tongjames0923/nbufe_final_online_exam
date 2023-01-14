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
export function api_changePublic(id,pb)
{
    return request({url:'question/updatePublic',params:{
        ques:id,publicable:pb
    }})
}
export function questionBody(id)
{
    return request({url:'file/question',method:'get',params:{'id':id}});
}
export function typeOf(val)
{
    if(val===0)
    return '选择题'
    else if(val===1)
    return '填空题'
    else if(val===2)
    return '简答题'
    else 
    '错误的题型'
}
export function searchByTitle(title,from,num)
{
    return request({url:'question/search',method:'get',params:{title:title,from:from,num:num}});
}
export function searchByID(id)
{
    return request({url:'question/findByid',method:'get',params:{id:id}});
}
export function searchByTag(tag)
{
    return request({url:'question/findByTag',method:'get',params:{tag:tag}});
}
export function deleteQues(que,user)
{
    return request({url:'question/delete',method:'get',params:{ques:que,user:user}});
}