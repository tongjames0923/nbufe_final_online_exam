//
// Created by abstergo on 2022/10/17.
//

#ifndef QT_DEMO_MAINWINDOW_H
#define QT_DEMO_MAINWINDOW_H

#include <QMainWindow>


QT_BEGIN_NAMESPACE
namespace Ui
{
	class MainWindow;
}
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
Q_OBJECT

public:
	explicit MainWindow(QWidget *parent = nullptr);

	~MainWindow() override;

	bool eventFilter(QObject *watched, QEvent *event) override;

public slots:
	void button_clicked();

private:
	Ui::MainWindow *ui;
};


#endif //QT_DEMO_MAINWINDOW_H
