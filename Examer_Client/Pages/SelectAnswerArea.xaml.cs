using Examer_Client.Objects;
using Examer_Client.Utils;
using System.Collections.Generic;
using System.Windows.Controls;

namespace Examer_Client.Pages
{
    /// <summary>
    /// SelectAnswerArea.xaml 的交互逻辑
    /// </summary>
    public partial class SelectAnswerArea : Page
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
        public SelectAnswerArea(int index)
        {
            InitializeComponent();
            this.Q = SystemManager.Manager.Questions[index].ques_id;
            select.Items.Clear();
            List<SingleSelect> selects =
            System.Text.Json.JsonSerializer.Deserialize<List<SingleSelect>>(SystemManager.Manager.Questions[index].detail.answer_data);
            foreach (var item in selects)
            {
                ListBoxItem listBox = new ListBoxItem();
                listBox.Content = item.text;
                listBox.Tag = item;

                foreach (var str in Check.text)
                {
                    if (str.Equals(item.text))
                    {
                        listBox.IsSelected = true;
                        break;
                    }
                }
                select.Items.Add(listBox);
            }
            init = false;
        }

        private void select_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (!init)
            {
                Check.text.Clear();
                foreach (var i in select.SelectedItems)
                {
                    ListBoxItem item = (ListBoxItem)i;
                    Check.text.Add(item.Content.ToString());
                }
            }

        }
    }
}
