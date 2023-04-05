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
    /// FillBlankAnswerArea.xaml 的交互逻辑
    /// </summary>
    public partial class FillBlankAnswerArea : Page
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
        public FillBlankAnswerArea(int index)
        {
            InitializeComponent();
            this.Q = SystemManager.Manager.Questions[index].ques_id;
            fill.Items.Clear();
            List<FillBlank> selects =
            System.Text.Json.JsonSerializer.Deserialize<List<FillBlank>>(SystemManager.Manager.Questions[index].detail.answer_data);
            int i = 0;
            foreach (var item in selects)
            {
                TextBox box = new TextBox();
                box.Width = 150;
                Check.text.Add("");
                box.TextChanged += Box_TextChanged;
                box.Tag =i;
                //box.Text = item.text;
                i++;
                fill.Items.Add(box);
            }
            init = false;
        }

        private void Box_TextChanged(object sender, TextChangedEventArgs e)
        {
            if(!init)
            {
                TextBox item = (TextBox)sender;
                int index = (int)(item).Tag;
                Check.text[index] = item.Text;
            }

        }
    }
}
