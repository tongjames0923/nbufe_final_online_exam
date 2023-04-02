using Examer_Client.Objects;
using Examer_Client.Utils;
using System.Collections.Generic;
using System.Windows;
using System.Windows.Controls;

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
                    }
                    ischecked = true;
                });
            }
            else
            {
                Label label = (Label)exams.SelectedItem;
                ExamInfo info = (ExamInfo)label.Tag;
                SystemManager.Manager.Feature.startExam(name.Text, number.Text, id.Text, info.exam_id.ToString(), (ExamPost ei) =>
                {
                    SystemManager.Manager.Exam = ei;
                    SystemManager.Manager.WindowArea.Content = new QuestionPage();
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
