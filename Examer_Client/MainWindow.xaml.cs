using Examer_Client.Utils;
using Examer_Client;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Examer_Client.Objects;

namespace Examer_Client
{
    using ExamList = NetResp<List<ExamInfo>>;
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
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
                if(!value)
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
        Feature feature = new Feature();
        public MainWindow()
        {
            InitializeComponent();
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            if(!ischecked)
            {
               feature.GetExams(name.Text, number.Text, id.Text,(List<ExamInfo> list)=> {
                   foreach (ExamInfo info in list)
                   {
                       exams.Items.Add(info.exam_name);
                   }
                   ischecked = true;
               });
            }

        }

        private void name_TextChanged(object sender, TextChangedEventArgs e)
        {
            ischecked = false;
        }
    }
}
