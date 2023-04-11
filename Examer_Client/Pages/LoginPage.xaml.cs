using Examer_Client.Objects;
using Examer_Client.Utils;
using System.Collections.Generic;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using static Examer_Client.Utils.SystemManager;

namespace Examer_Client.Pages
{


    /// <summary>
    /// LoginPage.xaml 的交互逻辑
    /// </summary>
    public partial class LoginPage : Page
    {


        bool m_ischecked = false;
        bool ischecked
        {
            get
            {
                return m_ischecked;
            }
            set
            {
                if (!value)
                {
                    btn.Content = "检查考试";
                }
                else
                {
                    btn.Content = "开始考试";
                }
                m_ischecked = value;
            }
        }
        private void Button_Click(object sender, RoutedEventArgs e)
        {
            bool flag = false;
            string[] badapp = { "msedge", "DingTalk", "chrome", "QQ", "WeChat" };
            string[] badname = { "Edge浏览器", "钉钉", "Chrome 浏览器", "QQ", "微信" };
            int i = 0;
            foreach (var b in badapp)
            {
                Process[] processes = Process.GetProcessesByName(b);
                if (processes.Length > 0)
                {
                    MessageBox.Show($"发现{badname[i]}", "请关闭聊天软件和浏览器");
                    flag = true;
                    foreach (var proc in processes)
                    {
                        proc.Kill();
                    }

                }
                i++;
            }
            if (flag)
            {
                MessageBox.Show("检测到您有非法软件打开中。已强制关闭");
            }
            if (!ischecked)
            {
                SystemManager.Manager.Feature.GetExams(name.Text, number.Text, id.Text, (List<ExamInfo> list) =>
                {
                    foreach (ExamInfo info in list)
                    {

                        Label label = new Label();
                        label.Content = info.exam_name;
                        label.Tag = info;
                        exams.Items.Add(label);
                        ischecked = true;
                    }

                });
            }
            else
            {
                Label label = (Label)exams.SelectedItem;
                ExamInfo info = (ExamInfo)label.Tag;
                SystemManager.Manager.Feature.startExam(info.uid, info.exam_id.ToString(), (ExamPost ei) =>
                {
                    SystemManager.Manager.Exam = ei;
                    SystemManager.Manager.ExamInfo = info;
                    SystemManager.Manager.ExamUser = ei.students[0];
                    SystemManager.Manager.WindowArea.Content = new QuestionPage();
                    if (SystemManager.Manager.WindowArea.CanGoBack)
                    {
                        SystemManager.Manager.WindowArea.RemoveBackEntry();
                    }
                });
            }

        }

        private void name_TextChanged(object sender, TextChangedEventArgs e)
        {
            ischecked = false;
        }



        public LoginPage()
        {
            InitializeComponent();
        }
    }

}
