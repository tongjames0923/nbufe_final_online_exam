using Examer_Client.Objects;
using Examer_Client.Utils;
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

namespace Examer_Client.Pages
{
    /// <summary>
    /// SimpleAnswerArea.xaml 的交互逻辑
    /// </summary>
    public partial class SimpleAnswerArea : Page
    {
        int ques;

        public int Q
        {
            get => ques;
            private set
            {
                this.ques = value;
            }
        }
        public CheckData Check
        {
            get => SystemManager.Manager.getByQuestionId(Q);

        }
        bool init = true;
        public SimpleAnswerArea(int index)
        {
            InitializeComponent();
            this.Q = SystemManager.Manager.Questions[index].ques_id;
            if(Check.text.Count==0)
            {
                Check.text.Add("");
            }
            else
            {
                rich.Selection.Text = Check.text[0];
            }
            init = false;

        }

        private void RichTextBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            if(!init)
            {
                TextRange text = new TextRange(rich.Document.ContentStart, rich.Document.ContentEnd);
                Check.text[0] = text.Text;
            }


        }
    }
}
