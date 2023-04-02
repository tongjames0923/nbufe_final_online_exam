using Examer_Client.Objects;
using System;
using System.Net.Http;

namespace Examer_Client.Utils
{
    public class HttpHelper
    {
        HttpClient client = new HttpClient();
        public NetResp<T> GET<T>(string url)
        {
            try
            {
                var task = client.GetAsync(url);
                task.Wait();
                var read = task.Result.Content.ReadAsStringAsync();
                read.Wait();
                String result = read.Result;
                return System.Text.Json.JsonSerializer.Deserialize<NetResp<T>>(result);
            }
            catch (Exception e)
            {
                NetResp<T> resp = new NetResp<T>() { code = 404, message = e.Message };
                return resp;
            }

        }
    }
}
