using Examer_Client.Pages;
using Examer_Client.Utils;
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
        }


    }
}
