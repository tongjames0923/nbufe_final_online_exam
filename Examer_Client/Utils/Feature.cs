﻿using Examer_Client.Objects;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace Examer_Client.Utils
{
    public class Feature
    {
        public static string BASEURL = "http://127.0.0.1:8080/";


        private void handle<T>(NetResp<T> resp,Action<T> success)
        {
            if (resp.code == 40000)
            {
                success.Invoke(resp.data);
            }
            else
            {
                MessageBox.Show($"错误信息：{resp.message}", $"请求失败{resp.code}");
            }
        }
        public void GetExams(string name,string number,string id,Action<List<ExamInfo>> success)
        {
            HttpHelper helper = new HttpHelper();
            handle<List<ExamInfo>>(
                helper.GET<List<ExamInfo>>(BASEURL + $"exam/listExamForStudent?name={name}&number={number}&id={id}"),success);
        }
    }
}