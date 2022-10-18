//
// Created by abstergo on 2022/10/17.
//

// You may need to build the project (run Qt uic code generator) to get "ui_MainWindow.h" resolved

#include <QMessageBox>
#include "mainwindow.h"
#include "ui_MainWindow.h"


MainWindow::MainWindow(QWidget *parent) :
		QMainWindow(parent), ui(new Ui::MainWindow)
{
	ui->setupUi(this);
	connect(ui->pushButton,&QAbstractButton::clicked, this,&MainWindow::button_clicked);
	installEventFilter(this);
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
	if(event->type()==QEvent::ActivationChange)
	{
		if (QApplication::activeWindow()!= this)
		{
			QMessageBox::information(this,"","you need back to mainWindow");
			removeEventFilter(this);
		}
	}


	return QObject::eventFilter(watched, event);
}

