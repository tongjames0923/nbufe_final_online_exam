using Examer_Client.Pages;
using Examer_Client.Utils;
using System.Diagnostics;
using System.Windows;

namespace Examer_Client
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
    {

        public MainWindow()
        {
            InitializeComponent();
            area.Content = new LoginPage();
            SystemManager.Manager.WindowArea = area;
            bool flag=false;
            string[] badapp = { "msedge", "DingTalk", "chrome", "QQ","WeChat" };
            string[] badname = { "Edge浏览器", "钉钉", "Chrome 浏览器", "QQ","微信" };
            int i = 0;
            foreach(var b in badapp)
            {
                if(Process.GetProcessesByName(b).Length>0)
                {
                    MessageBox.Show($"发现{badname[i]}","请关闭聊天软件和浏览器");
                    flag = true;

                }
                i++;
            }
            if(flag)
            {
                MessageBox.Show("检测到您有非法软件打开中。程序自动关闭");
                Application.Current.Shutdown();
            }
        }


    }
}
