import req from '@/utils/request'

export function api_ask(text)
{
    return req({
        url:"ai/ask",
        method:'post',
        data:
        {
            'text':text
        }
    })
}
export function api_makeSelectQ(text)
{
    return req({
        url:"ai/makeSelectQ",
        method:'post',
        data:text
    })
}
export function api_makeFillBlankQ(text)
{
    return req({
        url:"ai/makeFillBlankQ",
        method:'post',
        data:text
    })
}