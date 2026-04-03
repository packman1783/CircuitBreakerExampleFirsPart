Circuit Breaker Example

Учебный проект на Java Spring Boot, демонстрирующий реализацию паттерна Circuit Breaker с использованием библиотеки Resilience4j.
Цель проекта
Предотвращение каскадных сбоев в системе при недоступности внешнего сервиса.

Использование
Откройте браузер и перейдите по адресам:
Тест 1: Проверка работы (внешний сервис работает)
http://localhost:8080/api/test

Ожидаемый ответ: 
json
{"message":"Real external data"}


Тест 2: Проверка fallback (внешний сервис выключен)
    1. Остановите CircuitBreakerOuterExample (Ctrl+C в терминале 1)
    2. Подождите 3-5 секунд
    3. Обновите страницу в браузере
http://localhost:8080/api/test


Ожидаемый ответ (fallback):
json
{"message":"Fallback response: внешний сервис недоступен"}

Тест 3: Проверка endpoint'а /api/fail
http://localhost:8081/api/fail

Этот endpoint намеренно выбрасывает RuntimeException для имитации упавшего сервиса.

properties:
# === Circuit Breaker Configuration ===
resilience4j.circuitbreaker.instances.externalService.slidingWindowSize=5 - Анализируем последние 5 вызовов
resilience4j.circuitbreaker.instances.externalService.failureRateThreshold=50 - 50%	При 3 из 5 ошибок → OPEN
resilience4j.circuitbreaker.instances.externalService.waitDurationInOpenState=10s - Время в состоянии OPEN
resilience4j.circuitbreaker.instances.externalService.permittedNumberOfCallsInHalfOpenState=3 - Тестовые вызовы для восстановления

Endpoints

CircuitBreakerExample (порт 8080)

CircuitBreakerOuterExample (порт 8081)
Метод	URL	Описание
GET	/api/data	Возвращает реальные данные
GET	/api/fail	Возвращает 500 ошибку

Actuator Endpoints (CircuitBreakerExample)
URL	Описание
/actuator/health	Статус приложения и Circuit Breaker
/actuator/circuitbreakers	Информация о состоянии circuit breaker

Проверьте состояние Circuit Breaker:
http://localhost:8080/actuator/health
