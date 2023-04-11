using Examer_Client.Objects;
using Examer_Client.Utils;
using Markdig;
using System;
using System.Windows;
using System.Windows.Controls;
using static Examer_Client.Utils.SystemManager;

namespace Examer_Client.Pages
{
    /// <summary>
    /// QuestionPage.xaml 的交互逻辑
    /// </summary>
    public partial class QuestionPage : Page
    {
        ExamQuestion q;
        int index = 0;


        public int Index
        {
            get => index;
            set
            {
                if (value < 0)
                    index = 0;
                else if (value >= SystemManager.Manager.Questions.Count)
                {
                    index = SystemManager.Manager.Questions.Count - 1;
                }
                else
                    index = value;
                Question = SystemManager.Manager.Questions[index];
                Page willgo=null;
                switch(Question.detail.que_type)
                {
                    case 0:willgo = new SelectAnswerArea(index);
                        break;
                    case 1:willgo = new FillBlankAnswerArea(index);
                        break;
                    case 2:
                        willgo = new SimpleAnswerArea(index);
                        break;
                }
                answer.Content = willgo;
                if(answer.CanGoBack)
                answer.RemoveBackEntry();
            }
        }

        public ExamQuestion Question
        {
            get => q;
            set
            {
                q = value;
                var result = Markdown.ToHtml(q.detail.que_file);
                //Console.WriteLine(result);   // prints: <p>This is a text with some <em>emphasis</em></p>
                questionContent.NavigateToString(result);
                String type = "";
                switch (q.detail.que_type)
                {
                    case 0:
                        type = "选择题";
                        break;
                    case 1:
                        type = "填空题";
                        break;
                    case 2:
                        type = "简答题";
                        break;
                }
                title.Content = $"第{Index}题 {type} 分值:{q.score} 标题:{q.detail.title}";
            }
        }
        public QuestionPage()
        {
            InitializeComponent();
            Index = 0;
        }

        private void lastQuestion_Click(object sender, RoutedEventArgs e)
        {
            Index--;
        }

        private void nextQuestion_Click(object sender, RoutedEventArgs e)
        {
            Index++;
        }

        private void submit_Click(object sender, RoutedEventArgs e)
        {
            SystemManager.Manager.Feature.uploadCheck(Manager.ExamInfo.exam_id, Manager.ExamUser.uid
                , Manager.AllChecks, (Object o) =>
                 {
                     MessageBox.Show("提交成功");
                 });
        }
    }
}
