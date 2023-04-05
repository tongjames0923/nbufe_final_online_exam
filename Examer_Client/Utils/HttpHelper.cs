using Examer_Client.Objects;
using RestSharp;
using System;
using System.Collections.Generic;

namespace Examer_Client.Utils
{
    public class HttpHelper
    {
        public static string BASEURL = "http://127.0.0.1:8090/API_Server/";
        public NetResp<T> Post<T, D>(string url, Dictionary<string, string> pms, D body) where D : class
        {
            try
            {
                RestSharp.RestClient client = new RestSharp.RestClient(BASEURL);
                RestSharp.RestRequest request = new RestRequest(url, Method.Post);
                request.AddHeader("Content-Type", "application/json;charset=UTF-8");
                foreach (var i in pms)
                {
                    request.AddParameter(i.Key, i.Value);
                }
                request.AddBody(body);
                var task = client.PostAsync<NetResp<T>>(request);
                task.Wait();
                return task.Result;
            }
            catch (Exception e)
            {
                NetResp<T> resp = new NetResp<T>() { code = 404, message = e.Message };
                return resp;
            }

        }
        public NetResp<T> GET<T>(string url, Dictionary<string, string> pms)
        {
            try
            {
                RestSharp.RestClient client = new RestSharp.RestClient(BASEURL);
                RestSharp.RestRequest request = new RestRequest(url, Method.Get);
                foreach (var i in pms)
                {
                    request.AddParameter(i.Key, i.Value);
                }

                var task = client.GetAsync<NetResp<T>>(request);
                task.Wait();
                return task.Result;
            }
            catch (Exception e)
            {
                NetResp<T> resp = new NetResp<T>() { code = 404, message = e.Message };
                return resp;
            }

        }
    }
}
