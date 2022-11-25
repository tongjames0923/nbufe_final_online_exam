import req from "@/util/request"
export default
    {
        async getTagByQues(ques_id) {
            let data = await req.get({
                url: "tag/getQuesTag",
                params: {
                    ques: ques_id
                }
            });
            return data;
        }
        ,
        async getAllTags()
        {
            let data=await req.get({url:"tag/list"});
            return data;
        }
    }