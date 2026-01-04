# User Feature Module

此模块演示了符合 Modern Android Development (MAD) 标准的 Clean Architecture 架构实现。
模块采用了 strictly separated layers (Strict Layer Separation) 原则。

## 架构分层说明

### 1. Domain Layer (`domain/`)
**核心业务层，不依赖任何 Android 框架细节，纯 Kotlin 代码。**
- **model/**: 业务实体对象（Entity）。
- **repository/**: 仓库接口定义，定义了数据获取的规约，但不关心具体实现（网络或数据库）。
- **usecase/**: 用例层，封装单一的业务逻辑（例如：GetUserUseCase），供 ViewModel 调用。

### 2. Data Layer (`data/`)
**数据实现层，负责数据的获取、存储和转换。**
- **model/**: 数据实体，通常包含 API 响应结构或数据库表结构注解。
    - 在本示例中，为简化代码，`User` 类同时充当了 Domain Model, Room Entity 和 Network DTO。
- **local/**: 本地数据源实现 (Room Database, DAO)。
- **remote/**: 远程数据源实现 (Retrofit API)。
- **repository/**: Domain 层仓库接口的具体实现 (`UserRepositoryImpl`)。负责协调 Local 和 Remote 数据源，实现 "Single Source of Truth" (SSOT)。

### 3. Presentation Layer (`presentation/`)
**UI 展示层，负责处理 UI 状态和用户交互。**
- **UserViewModel**: 使用 MVVM 模式，持有 `StateFlow<UiState>`，通过 UseCase 与业务层交互。
- **UserScreen**: 使用 Jetpack Compose 构建的声明式 UI。
    - `UserRoute`: 负责与 ViewModel 连接，State Hoisting。
    - `UserScreen`: 无状态的纯 UI 组件。

### 4. Dependency Injection (`di/`)
**依赖注入层，使用 Hilt 将各层组件装配在一起。**
- **UserModule**: 提供 Database, API, Repository, UseCase 等对象的创建逻辑。

## 技术栈
- **UI**: Jetpack Compose (Material3)
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Network**: Retrofit + Kotlin Serialization
- **Local Storage**: Room
- **Async**: Coroutines + Flow
