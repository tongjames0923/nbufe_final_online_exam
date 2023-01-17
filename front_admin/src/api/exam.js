
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