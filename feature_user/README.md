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

## Hilt 依赖注入详解

本模块使用 Hilt 来管理对象的创建和依赖关系，实现了代码的松耦合。对于初学者，以下是本模块中关键注解的解析：

### 1. 注入入口 (@AndroidEntryPoint)
- **位置**: `UserActivity`
- **作用**: 告诉 Hilt 这个 Activity 需要注入依赖。
    - 只有添加了此注解，Activity 才能使用 `@Inject` 注入字段，或者获取 `@HiltViewModel`。
    - 如果忘记添加，App 运行时会崩溃，提示无法创建 ViewModel。

### 2. 定义依赖来源 (@Module, @Provides)
- **位置**: `di/UserModule.kt`
- **背景**: Hilt 不知道如何创建接口（如 `UserRepository`）或第三方库类（如 `Retrofit`, `RoomDatabase`）。我们需要在一个模块里手动教它。
- **@Module**: 表明这是一个 Hilt 配置模块。
- **@InstallIn(SingletonComponent::class)**: 指定这些依赖的作用域是全 App 范围（单例）。即在 App 运行期间，这些对象只会被创建一次。
- **@Provides**: 修饰方法。告诉 Hilt："当有地方需要这个类型的对象时，请运行这个方法来创建它"。
    - *示例*: `provideUserRepository` 方法告诉 Hilt，当 `UserViewModel` 需要 `UserRepository` 接口时，请返回 `UserRepositoryImpl` 的实例。

### 3. 构造函数注入 (@Inject)
- **位置**: `UserRepositoryImpl`, `GetUserUseCase`
- **作用**: 告诉 Hilt 如何创建这个自己写的类的实例。
    - Hilt 会自动查看构造函数需要的参数（如 `UserApi`, `UserDao`），并自动去 DI 系统里找到这些对象填进去。
    - 这是 Clean Architecture 中最常用的注入方式。

### 4. ViewModel 注入 (@HiltViewModel)
- **位置**: `UserViewModel`
- **作用**: 专门用于 ViewModel 的注解。
    - 它允许 Hilt 生成 ViewModel 的工厂类。
    - 配合 Compose 中的 `hiltViewModel()` 函数，系统会自动创建 ViewModel，并自动注入它所需要的 UseCase 和 Repository。

### 依赖链是如何打通的？
1. **UI**: `UserActivity` 请求获取 `UserViewModel`。
2. **ViewModel**: Hilt 发现 `UserViewModel` 需要 `GetUserUseCase`。
3. **Domain**: Hilt 发现 `GetUserUseCase` 需要 `UserRepository` 接口。
4. **Module**: Hilt 查阅 `UserModule`，发现 `UserRepository` 接口应该由 `UserRepositoryImpl` 实现。
5. **Data**: Hilt 发现 `UserRepositoryImpl` 需要 `UserApi` 和 `UserDao`。
6. **Root**: Hilt 再次查阅 `UserModule`，调用对应的 `@Provides` 方法创建 Retrofit 和 Room 实例，最终层层组装完成。
