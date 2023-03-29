import request from '@/utils/request'
import { getToken } from '@/utils/auth';
export function deleteResource(resourceid)
{
    let token= getToken()
    if(token)
    {
        let obj=JSON.parse(token)
        return request(
            {
                url:"resource/delete",
                method:'get',
                params:{'userid':obj.id,
            'resource_id':resourceid
            }
            }
        )
    }
}
export function getInfoByType(type,from,num)
{
    return request({
        url:'resource/getByType',
        method:'get',
        params:{'type':type,'from':from,'num':num}
    });
}
export function searchByNote(note,from,num)
{
    return request({url:"resource/getByNote",
method:'get',
params:{'note':note,'from':from,'num':num}
})
}
export function upload_resource(formdata)
{
    return request({
        url:"/resource/upload",
        method:"post",
        data:formdata
    })
}
export function api_changeResourceNote(id,note)
{
    return request({
        url:'resource/setnote',
        method:'get',
        params:{
            'resource':id,
            'note':note
        }
    })
}