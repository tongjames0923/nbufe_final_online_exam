import request from '@/utils/request'
import { getToken } from '@/utils/auth';

export function getAllTags()
{
    return request({url:'tag/list',method:'get'});
}
export function getTagByQues(ques_id)
{
    return request({url:'tag/getQuesTag',method:'get',params:{ques:ques_id}});
}
export function remove(name)
{
    return request({url:'tag/remove',method:'get',
params:{'tag':name}});
}
export function add(val)
{
    return request({url:'tag/add',method:'get',
params:{tag:val}
})
}
export function api_getUnselect(ques)
{
    return request({
        url:'tag/unselected_tags',
        method:'get',
        params:{
            'ques':ques
        }
    })
}