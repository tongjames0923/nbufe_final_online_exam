
import req from '@/utils/request'
export function api_uploadExam(u, c, s, q) {
    return req({
        url: 'exam/upload',
        method: 'post',
        params: { user: u },
        data: {
            exam_name: c.exam_name,
            exam_begin: c.exam_begin,
            exam_len: c.exam_len,
            exam_note: c.exam_note,
            students: s,
            questions: q
        }
    })
}
export function api_count(user) {
    return req({ url: 'exam/count', method: 'get', params: { 'user': user } })
}
export function api_list(from, num) {
    return req({ url: 'exam/list', method: 'get', params: { 'from': from, 'num': num } })
}
export function api_updateNote(v, u, e) {
    return req({ url: 'exam/updateNote', method: 'get', params: { 'note': v, 'user': u, 'examid': e } });
}
export function api_updateName(v, u, e) {
    return req({
        url: 'exam/updateName',
        method: 'get',
        params: {
            'name': v,
            'user': u,
            'examid': e
        }
    })
}
export function api_updateLen(v, u, e) {
    return req({
        url: 'exam/updateLen',
        method: 'get',
        params: {
            'len': v,
            'user': u,
            'examid': e
        }
    })
}
export function api_updateBegin(v, u, e) {
    return req({
        url: 'exam/updateBegin',
        method: 'get',
        params: {
            'time': v,
            'user': u,
            'examid': e
        }
    })
}
export function api_fullExam(id) {
    return req({
        url: 'exam/getFull',
        method: "get",
        params: {
            'id': id
        }
    })
}
export function api_delete(e, u) {
    return req({ url: 'exam/delete', method: 'get', params: { 'id': e, 'user': u } })
}
export function api_updateScore(qid, exam, score) {
    return req({
        url: "exam/updateScore",
        method: 'get',
        params: {
            'qid': qid,
            'exam': exam,
            'score': score
        }
    })
}
export function api_listExamAccess(examid)
{
    return req({
        url:'exampermissions/listExamAccess',
        method:'get',
        params:{
            'examid':examid
        }
    })
}

export function api_setAccessForExam(exam,u,w,r,c)
{
    return req({
        url:'exampermissions/set',
        method:'get',
        params:
        {
            examid:exam,
            targetid:u,
            read:r,
            write:w,
            check:c
        }
    })
}
export function api_precheck(exam)
{
    return req({
        url:"reply/precheck",
        method:'get',
        params:{
            'examid':exam
        }
    })
}
export function api_listCheck(examid)
{
    return req({
        url:'reply/list',
        method:'get',
        params:{
            'examid':examid
        }
    })
}
export function api_updateCheckScore(s,er)
{
    return req({
        url:'reply/updateScore',
        method:'get',
        params:{
            'rep_id':er,
            'score':s
        }
    })
}
export function api_confirmExam(e)
{
    return req({
        url:'reply/confirm',
        method:'get',
        params:{
            'examid':e
        }
    })
}