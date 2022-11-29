/* eslint-disable */
import req from "@/util/request"

export default{

        async getUser(i)
        {
          let data=  await req.get({
                url:"user/getUser",
                params:{id:i}
            });
            return data;
        }
}