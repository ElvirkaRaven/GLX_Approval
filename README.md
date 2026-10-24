# GLX_Approval

Мобильное Android-приложение для согласования документов из учётной системы (1С Web-сервер). Приложение позволяет ответственному лицу просматривать список документов, которые ждут его согласования, открывать карточку любого документа и принимать по нему решение («Согласовать» или «Отклонить»). Для работы с сервером пользователь должен задать в настройках адрес сервера, имя базы данных, имя пользователя и пароль.

## Возможности приложения

- **Список документов на согласование.** Главный экран отображает все документы, ожидающие решения пользователя, в виде карточек. По каждой карточке видны: тип документа, номер, дата, сумма с валютой и текущий статус.
- **Просмотр деталей документа.** По тапу на карточку открывается экран деталей с полным набором полей (тип, номер, дата, сумма, статус, проект, комментарий) и двумя кнопками: «Согласовать» и «Отклонить».
- **Проверка подключения к серверу.** На экране настроек можно ввести параметры подключения и проверить, что сервер доступен, нажатием кнопки «Проверить подключение». Проверка выполняется через специальный endpoint `/hs/approval/v1/ping/`, который должен вернуть строку `pong`.
- **Сохранение настроек.** Параметры подключения сохраняются локально через Jetpack DataStore и переиспользуются при следующих запусках приложения.
- **Безопасное подключение к self-signed HTTPS.** Используется `UnsafeOkHttpClient`, который обходит проверение сертификата сервера — это удобно для тестовых и внутренних корпоративных серверов с self-signed сертификатами.

## Технологии

| Категория | Библиотека | Версия |
|---|---|---|
| Язык | Kotlin | 2.2.21 |
| Сборка | Android Gradle Plugin | 9.1.1 |
| Минимальный SDK | Android 26 (Android 8.0 Oreo) | — |
| Target SDK | Android 36 | — |
| UI | Jetpack Compose + Material 3 | BOM 2026.08.00 |
| DI | Hilt (Dagger) | 2.57.2 |
| Навигация | Navigation Compose | 2.10.2 |
| Сетевой слой | Retrofit 3.0.0 + OkHttp 5.1.0 + Gson 2.13.2 | — |
| Локальные настройки | DataStore Preferences | 1.2.1 |
| Корутины | kotlinx-coroutines | 1.10.2 |
| Статический анализ | Detekt | 1.23.8 |
| Тестирование | JUnit4 + Compose UI Test | — |

## Архитектура

Приложение построено по принципам **Clean Architecture** в слое данных и **MVVM** в слое представления. Проект разбит на модули по принципу «feature + core», что позволяет переиспользовать доменную модель и репозитории во всех фичах и независимо тестировать каждый слой.

### Модули

| Модуль | Назначение |
|---|---|
| `app` | Точка входа в приложение. Содержит `Application`, `MainActivity`, тему, навигацию между экранами. |
| `core:domain` | Чистый Kotlin-модуль (без зависимостей от Android). Содержит доменные модели (`Document`, `ServerSettings`, `ObjectsRequest`), интерфейсы репозиториев (`DocumentsRepository`, `SettingsRepository`) и константы предметной области. |
| `core:data` | Реализация репозиториев поверх Retrofit и DataStore. Содержит `DocumentsRepositoryImpl`, `LocalDocumentsDataSource` (читает JSON из assets), `PingChecker` (проверка сервера), `SettingsDataStore` (хранилище настроек), Hilt-модули `NetworkModule` и `RepositoryModule`. |
| `core:ui` | Общие UI-компоненты (в текущей версии почти пустой, зарезервирован для будущих переиспользуемых виджетов). |
| `core:navigation` | Граф навигации между экранами, определение маршрутов, нижняя навигация. |
| `feature:documents` | Экраны «Список документов» и «Детали документа» + соответствующие ViewModel. |
| `feature:settings` | Экран «Настройки сервера» + `SettingsViewModel`. |

### Схема архитектуры

                    Single Activity
                          |
                  Navigation Compose
                     /          \
                    v            v
             Экран документов   Настройки
                    |             |
                ViewModel      ViewModel
                     \           /
                      v         v
                         Domain
                  Модели и контракты
                          ^
                          |
                         Data
                     Репозитории
                    /         \
                   v           v
              JSON / Assets   Retrofit
                                  |
                                  v
                               REST API

### Поток данных

1. **Открытие экрана списка документов.** `DocumentsViewModel` через `init {}` вызывает `loadDocuments()` → `DocumentsRepository.getDocuments()` → `DocumentsRepositoryImpl` → `LocalDocumentsDataSource.loadDocuments()` → чтение `documents.json` из `assets/`, парсинг через Gson, маппинг в `Document`.
2. **Просмотр деталей.** `DocumentDetailViewModel` достаёт `documentId` из `SavedStateHandle`, ещё раз запрашивает список документов у репозитория и находит нужный по `id`.
3. **Сохранение настроек.** `SettingsViewModel.saveSettings()` → `SettingsDataStore.save()` → `DataStore.edit { ... }` (асинхронно пишется в файл `server_settings.preferences_pb`).
4. **Проверка подключения.** `SettingsViewModel.checkConnection()` → `PingChecker.check()` → собирается URL вида `{server}/{database}/hs/approval/v1/ping/` → вызывается `PingApi.ping()` → если `response.code() == 200` и тело равно `"pong"`, проверка считается успешной.

## Сборка и запуск

### Требования

- JDK 17+
- Android SDK с `compileSdk = 36` (для модуля `app` — 37)
- Android Studio Ladybug или новее (либо Gradle 9.x из командной строки)

### Команды

```bash
# Собрать debug-APK
./gradlew :app:assembleDebug

# Установить на подключённое устройство
./gradlew :app:installDebug

# Запустить все unit-тесты
./gradlew test

# Запустить все instrumented/UI-тесты
./gradlew connectedAndroidTest

# Запустить Detekt
./gradlew detekt

# Сгенерировать отчёты Detekt (HTML, XML, SARIF)
# Они появятся в build/reports/detekt/ каждого модуля
```

## Тестирование

Проект покрыт тремя типами тестов:

1. **Unit-тесты (JUnit4).**
2. **UI-тесты (Compose UI Test).** 
3. **Статический анализ (Detekt).** 

## Скриншоты

### Список документов

<img width="387" height="775" alt="image" src="https://github.com/user-attachments/assets/f7ebb38e-ff85-42d0-848c-432068467323" />

### Состояние ошибки

<img width="394" height="779" alt="image" src="https://github.com/user-attachments/assets/d2d2a0fa-1935-43b6-b059-6b1fd63bd1dc" />

### Детали документа

<img width="385" height="773" alt="image" src="https://github.com/user-attachments/assets/321de75f-0e56-4444-961a-b80d3b1ef849" />

### Настройки

<img width="390" height="775" alt="image" src="https://github.com/user-attachments/assets/ef3681eb-6f2a-4a30-a21d-2b409bb4b3d7" />


## Структура проекта

```
GLX_Approval/
├── app/                                   # Главный модуль
│   └── src/main/java/com/example/glx_approval/
│       ├── ApprovalApplication.kt          # @HiltAndroidApp
│       ├── MainActivity.kt                 # @AndroidEntryPoint, setContent
│       └── ui/theme/                       # Material3 тема
├── core/
│   ├── domain/                            # Чистый Kotlin
│   │   └── src/main/java/com/example/domain/
│   │       ├── model/{Document,ServerSettings,ObjectsRequest}.kt
│   │       ├── repository/DocumentsRepository.kt
│   │       ├── SettingsRepository.kt
│   │       └── constants/ApprovalConstants.kt
│   ├── data/                              # Реализация репозиториев
│   │   └── src/main/java/com/example/data/
│   │       ├── ApprovalApi.kt
│   │       ├── repository/DocumentsRepositoryImpl.kt
│   │       ├── connection/PingChecker.kt
│   │       ├── settings/SettingsDataStore.kt
│   │       ├── remote/{LocalDocumentsDataSource,PingApi,UnsafeOkHttpClient}.kt
│   │       └── di/{NetworkModule,RepositoryModule}.kt
│   ├── ui/                                # Общие UI-компоненты 
│   └── navigation/
│       └── src/main/java/com/example/navigation/
│           ├── AppDestination.kt
│           ├── AppNavigation.kt
│           └── BottomNavigationBar.kt
├── feature/
│   ├── documents/
│   │   └── src/main/java/com/example/documents/presentation/
│   │       ├── DocumentsScreen.kt
│   │       ├── DocumentsViewModel.kt
│   │       ├── DocumentsRoute.kt
│   │       ├── DocumentDetailsScreen.kt
│   │       └── DocumentDetailViewModel.kt
│   └── settings/
│       └── src/main/java/com/example/settings/
│           ├── SettingsScreen.kt
│           ├── SettingsViewModel.kt
│           ├── SettingsRoute.kt
│           └── SettingsUiState.kt
├── config/detekt/detekt.yml               # Конфиг Detekt
├── gradle/libs.versions.toml              # Версии зависимостей
└── build.gradle.kts                       # Корневой build-файл
```

## Detekt

В проект подключён статический анализатор **Detekt** версии 1.23.8. 

Запуск:

```bash
./gradlew detekt
```

Конфигурация лежит в `config/detekt/detekt.yml`. Отчёты генерируются в `build/reports/detekt/` каждого модуля:

- `detekt.html` — человекочитаемый отчёт
- `detekt.xml` — для интеграции с CI
- `detekt.sarif` — для SonarQube / GitHub Code Scanning
