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
- **UserActivity**: 使用传统的 **XML 布局** 和 **ViewBinding** 构建 UI。Activity 负责观察 ViewModel 中的 `UiState`，并根据状态更新界面（如显示加载、成功或错误视图）。

### 4. Dependency Injection (`di/`)
**依赖注入层，使用 Hilt 将各层组件装配在一起。**
- **UserModule**: 提供 Database, API, Repository, UseCase 等对象的创建逻辑。

## 技术栈
- **UI**: **XML Layouts & ViewBinding**
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Network**: Retrofit + Kotlin Serialization
- **Local Storage**: Room
- **Async**: Coroutines + Flow

## Hilt 依赖注入详解

本模块使用 Hilt 来管理对象的创建和依赖关系，实现了代码的松耦合。对于初学者，以下是本模块中关键注解的解析：

### 1. 注入入口 (@AndroidEntryPoint)
- **位置**: `UserActivity`
- **作用**: 告诉 Hilt 这个 Activity 需要注入依赖。只有添加了此注解，Activity 才能获取由 Hilt 提供的 ViewModel。

### 2. 定义依赖来源 (@Module, @Provides)
- **位置**: `di/UserModule.kt`
- **背景**: Hilt 不知道如何创建接口（如 `UserRepository`）或第三方库类（如 `Retrofit`, `RoomDatabase`）。我们需要在一个模块里手动教它。
- **@Module**: 表明这是一个 Hilt 配置模块。
- **@InstallIn(SingletonComponent::class)**: 指定这些依赖的作用域是全 App 范围（单例）。
- **@Provides**: 修饰方法。告诉 Hilt："当有地方需要这个类型的对象时，请运行这个方法来创建它"。

### 3. 构造函数注入 (@Inject)
- **位置**: `UserRepositoryImpl`, `GetUserUseCase`
- **作用**: 告诉 Hilt 如何创建这个自己写的类的实例。Hilt 会自动查看构造函数需要的参数，并自动去 DI 系统里找到这些对象填进去。

### 4. ViewModel 注入 (@HiltViewModel & by viewModels())
- **位置**: `UserViewModel` / `UserActivity`
- **@HiltViewModel**: 专门用于 ViewModel 的注解。它允许 Hilt 生成 ViewModel 的工厂类。
- **`by viewModels()`**: 在 `UserActivity` 中，我们使用 `private val viewModel: UserViewModel by viewModels()` 来获取 ViewModel 实例。这是 `activity-ktx` 库提供的属性委托，它会自动处理 ViewModel 的生命周期，并利用 Hilt 创建注入了依赖的 ViewModel 实例。

### 依赖链是如何打通的？
1. **UI**: `UserActivity` 通过 `by viewModels()` 请求获取 `UserViewModel`。
2. **ViewModel**: Hilt 发现 `UserViewModel` 需要 `GetUserUseCase`。
3. **Domain**: Hilt 发现 `GetUserUseCase` 需要 `UserRepository` 接口。
4. **Module**: Hilt 查阅 `UserModule`，发现 `UserRepository` 接口应该由 `UserRepositoryImpl` 实现。
5. **Data**: Hilt 发现 `UserRepositoryImpl` 需要 `UserApi` 和 `UserDao`。
6. **Root**: Hilt 再次查阅 `UserModule`，调用对应的 `@Provides` 方法创建 Retrofit 和 Room 实例，最终层层组装完成。
