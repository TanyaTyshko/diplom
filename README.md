# Дипломный проект по профессии «Тестировщик»

## Документация
[План автоматизации](docs/Plan.md)

[Отчёт по итогам тестирования](docs/Report.md)

[Отчёт по итогам автоматизации](docs/Summary.md)

## Инструкция по запуску

1.	Клонировать репозиторий

*Выполнить команду:* <code>git clone https://github.com/TanyaTyshko/diplom.git</code>

2.	Перейти в склонированный репозиторий

3.	Запустить контейнеры Docker

*Выполнить команду:* <code>docker-compose up –build</code>

4.	Запустить приложение

*для mysql:*
<code>java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar</code>

*для postgresql:*
<code>java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar</code>

5. Запустить тесты

*для mysql:*
 <code>./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"</code>

*для postgresql:*
<code>./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"</code>

## Формирование отчёта Allure
*Генерация отчёта после прохождения тестов:*
<code>gradlew allureReport</code>

или

*Генерация отчёта и автоматическое открытие в браузере по умолчанию:*
<code>gradlew allureServe</code>

## Остановка контейнеров
*Чтобы остановить контейнер выполнить команду:*
<code>docker-compose down</code>
