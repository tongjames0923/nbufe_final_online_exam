using Examer_Client.Objects;
using System;
using System.Collections.Generic;
using System.Windows;

namespace Examer_Client.Utils
{
    public class Feature
    {

        HttpHelper helper = new HttpHelper();

        private void handle<T>(NetResp<T> resp, Action<T> success)
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
        public void GetExams(string name, string number, string id, Action<List<ExamInfo>> success)
        {
            Dictionary<string, string> dic = new Dictionary<string, string>();
            dic.Add("name", name);
            dic.Add("number", number);
            dic.Add("id", id);

            handle<List<ExamInfo>>(
                helper.GET<List<ExamInfo>>($"exam/listExamForStudent", dic), success);
        }

        public void startExam(string name, string number, string id, string examID, Action<ExamPost> suceess)
        {
            Dictionary<string, string> dic = new Dictionary<string, string>();
            dic.Add("name", name);
            dic.Add("number", number);
            dic.Add("id", id);
            dic.Add("examID", examID);
            handle<ExamPost>(helper.GET<ExamPost>($"exam/studentLogin", dic), suceess);
        }

        public void uploadCheck(int examid, String number, String personid, String personname, List<CheckData> datas, Action<Object> suceess)
        {
            UploadData d = new UploadData() {examid=examid,datas=datas,number=number,personid=personid,personname=personname };
            handle<Object>(helper.Post<Object, UploadData>("reply/upload",new Dictionary<string, string>(), d), suceess);
        }

    }
}
