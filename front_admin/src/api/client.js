import req from '@/utils/request'
export function api_getMyExams(name,num,id)
{
    return req({
        url:'exam/listExamForStudent',
        method:'get',
        params:{
            'name':name,
            'number':num,
            'id':id
        }
    })
}
export function api_getExam(uid,exam)
{
    return req({
        url:'exam/studentLogin',
        method:'get',
        params:{
            'uid':uid,
            'examID':exam
        }
    })
}
export function api_submitExam(examid,uid,datas)
{
    return req({
        url:'reply/upload',
        method:'post',
        data:{
                'examid':examid,
                'uid':uid,
                'datas':datas
        },
    })
}