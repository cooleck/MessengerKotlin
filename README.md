MessengerKotlin - клиент/серверное приложения (мессенджер), предназначенное для коммуникации между пользователями (клиентами) посредством передачи текстовых сообщений в чатах. В возможности клиентской части приложения входит поддержка авторизаиции и регистрации пользователей, создание и ведение чатов с другими пользователями, просмотр истории сообщений в чатах. Пользовательские чаты реализованы на основе протокола WebSockets. В серверную часть приложения входит поддержка возможностей клиентской части, хранение и получение авторизационных данных пользователей, списка чатов пользователей, историй сообщений пользователей.

Проект предназначен для ознакомления с фреймворком Ktor языка программирования Kotlin. Проект написан в учебных целях.

Сборка проекта осуществляется отдельно для клиентской и серверной части. Обе части собираются с помощью системы автоматической сборки Gradle. Все файлы настройки проекта для сборки обеих частей находятся в корневых папках каждой из частей проекта. 
Для сборки каждой из частей достаточно войти в корневую папку подпроекта и запустить задачу build для Gradle:

`cd ./MessegnerClient && ./gradlew build`

`cd ./MessengerServer && ./gradlew build`

Для запуска соответвующих подпроектов требуется выполнить схожие действия и запустить задачу run для Gradle:

`cd ./MessegnerClient && ./gradlew run`

`cd ./MessengerServer && ./gradlew run`

Для корректной работы серверной части приложения вышеперечисленных действий достаточно.

Далее будет описаны действия для работы с консольным UI клиентской части приложения:
При запуске клиентской части пользователю следует ввести логин и пароль. Если пользователь с таким логином уже зарегистрирован в мессенджере, то будет произведена проверка паролей и в случае корректного ввода пользователь получит приветственное сообщение,
в противном случае система попросит ввести корректные авторизационные данные. В случае если пользователь с заданным логином не будет найден в системе, то в системе будет зарегистрирован новый пользователь с
заданными авторизационными данными и будет произведен вход в систему под его логином.

После авторизации клиенту будет предложено ввести имя пользователя чат с которым, он хочет открыть или создать. Если введенное имя
не будет найдено в системе, то у клиента будет запрошен повторный ввод. В случае если чат не существовал в системе, он будет создан.
В случае если чат был создан ранее, пользователь получит всю историю сообщений чата.

Для выхода из чата в меню выбора чатов пользователю следует ввести команду `/exit`.
Для выхода из приложения клиента пользователю следует ввести команду `/exit` в меню выбора чатов.

Для завершения работы серверной части приложения следует нажать `Ctrl + C` в терминале.