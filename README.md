Приложение состоит из 3 модулей: server, client и network.
Для запуска приложения нужно сначала запустить server/chatserver, а затем запустить client/ClientWindow. 
(Можно запустить несколько клиентов одновременно, но для этого в настройках Edit Run Configuration у ClientWindow нужно поставить флаг "Allow multiple instances" в "Modify options")
Кнопки и поля в верхней панели panelTop в классе ClientWindow пока неактивны. IP  можно изменить на 9 строке кода, в переменной IP_ADDR, порт - на 10 строке, в переменной PORT.
Логи сохранаяются в файле log.txt в модуле server.
