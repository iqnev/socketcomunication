#include "socketclient.h"
#include "ui_socketclient.h"

#include <QtCore>

SocketClient::SocketClient(QWidget *parent)
 : QWidget(parent)
{
    setupUi();
    setupSocket();
}

SocketClient::~SocketClient()
{
    delete form;
}

void SocketClient::setupUi()
{

    if (form)
          return;

    form = new Ui_Form;
    form->setupUi(this);
    form->hostNameEdit->setSelection(0, form->hostNameEdit->text().size());
    form->sessionOutput->setHtml(tr("&lt;not connected&gt;"));

    connect(form->hostNameEdit, SIGNAL(textChanged(QString)),
               this, SLOT(updateEnabledState()));
    connect(form->connectButton, SIGNAL(clicked()),
               this, SLOT(socketConnect()));

}


void SocketClient::setupSocket()
{
    if (socket)
        return;

    socket = new QTcpSocket(this);

    connect(socket, SIGNAL(connected()),this, SLOT(updateEnabledState()));
    connect(socket, SIGNAL(disconnected()),this, SLOT(disconnected()));
    connect(socket, SIGNAL(readyRead()),this, SLOT(readyRead()));
    connect(socket, SIGNAL(stateChanged(QAbstractSocket::SocketState)),
                this, SLOT(socketStateChanged(QAbstractSocket::SocketState)));
}

void SocketClient::updateEnabledState()
{
    const bool unconnected = socket->state() == QAbstractSocket::UnconnectedState;
    form->hostNameEdit->setReadOnly(!unconnected);
    form->hostNameEdit->setFocusPolicy(unconnected ? Qt::StrongFocus : Qt::NoFocus);
    form->hostNameLabel->setEnabled(unconnected);
    form->portBox->setEnabled(unconnected);
    form->portLabel->setEnabled(unconnected);
    form->connectButton->setEnabled(unconnected && !form->hostNameEdit->text().isEmpty());

    const bool connected = socket->state() == QAbstractSocket::ConnectedState;
    form->sessionInputLabel->setEnabled(connected);
    form->sessionInputLabel->setEnabled(connected);
    form->sessionOutput->clear();
}

void SocketClient::socketConnect() {

    socket->connectToHost(form->hostNameEdit->text(), form->portBox->value());
    updateEnabledState();
}

void SocketClient::appendString(const QString &line)
{
    QTextCursor cursor(form->sessionOutput->textCursor());
    cursor.movePosition(QTextCursor::End);
    cursor.insertText(line.trimmed());
    cursor.insertText("\n");
    form->sessionOutput->verticalScrollBar()->setValue(form->sessionOutput->verticalScrollBar()->maximum());
}

void SocketClient::readyRead()
{
    QByteArray buffer;
    buffer = socket->readAll();

    qDebug() <<"Buffer"<<buffer;
    if(!buffer.isEmpty()) {
       appendString(QString::fromUtf8(buffer.data()));
    }
}

void SocketClient::socketStateChanged(QAbstractSocket::SocketState state)
{
    qDebug() << "Change socket state" <<state;

    if (executingDialog)
           return;

       updateEnabledState();

       if (state == QAbstractSocket::UnconnectedState) {
           form->sessionInputLabel->clear();
           form->hostNameEdit->setPalette(QPalette());
           form->hostNameEdit->setFocus();
           form->cipherLabel->setText(tr("<none>"));
       }
}

void SocketClient::disconnected()
{
    form->sessionInputLabel->clear();
    form->sessionInputLabel->setText(tr("Disconnected"));

    qDebug() << "disconnected...";


}
