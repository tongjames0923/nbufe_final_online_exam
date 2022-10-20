
#include "mainwindow.h"
#include "ui_MainWindow.h"
#include <QMessageBox>

MainWindow::MainWindow(QWidget *parent) :
		QMainWindow(parent), ui(new Ui::MainWindow)
{
	ui->setupUi(this);
	connect(ui->pushButton,&QAbstractButton::clicked, this,&MainWindow::button_clicked);
	ui->menuBar->setNativeMenuBar(false);
	for (QAction *ac:ui->menuFile->actions())
	{
		connect(ac,&QAction::triggered, this,&MainWindow::button_clicked);
	}
}

MainWindow::~MainWindow()
{
	delete ui;
}

void MainWindow::button_clicked()
{
	ui->label->setText("clicked in my heart");
}

bool MainWindow::eventFilter(QObject *watched, QEvent *event)
{
	QEvent::Type tp= event->type();
	if (tp==QEvent::MouseButtonPress)
	{
		QMessageBox::information(this,"clicked me!","you click !");
	}

	return QObject::eventFilter(watched, event);
}

