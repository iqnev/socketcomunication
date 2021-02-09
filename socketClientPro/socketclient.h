#ifndef SOCKETCLIENT_H
#define SOCKETCLIENT_H

#include <QtWidgets>
#include <QTcpSocket>
#include <QAbstractSocket>
#include <QDebug>

QT_BEGIN_NAMESPACE
class Ui_Form;
QT_END_NAMESPACE

class SocketClient : public QWidget
{
    Q_OBJECT
public:
   explicit SocketClient(QWidget *parent = nullptr);
   ~SocketClient();

private slots:
    void updateEnabledState();
    void socketConnect();
    void socketStateChanged(QAbstractSocket::SocketState state);
    void readyRead();
    void disconnected();
  //  void socketError(QAbstractSocket::SocketError error);
private:
    void setupUi();
    void setupSocket();
    void appendString(const QString &line);

    QTcpSocket *socket = nullptr;
    Ui_Form *form = nullptr;
    bool handlingSocketError = false;
    bool executingDialog = false;
};

#endif // SOCKETCLIENT_H
