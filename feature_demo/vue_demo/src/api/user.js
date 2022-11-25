/* eslint-disable */
import {get,post} from "@/util/request"

export default{

        async getUser(i)
        {
          let data=  await get({
                url:"user/getUser",
                params:{id:i}
            });
            return data;
        }
}