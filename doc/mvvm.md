# MVVM架构

MVVM 是 Model-View-ViewModel 的缩写，是一种基于数据驱动的设计模式，旨在分离应用的 UI 逻辑与业务逻辑。以下是对安卓 MVVM 架构原理的图解与说明：

### 核心组件

- **Model（模型）**：负责存储和处理应用的业务数据，包含数据结构、业务逻辑、数据获取（如网络请求、数据库操作）等。它与 UI 层完全解耦，不依赖 View 或 ViewModel。
- **View（视图）**：对应 Android 中的 UI 组件（如 Activity、Fragment、XML 布局），负责展示数据并接收用户交互。理想情况下，View 不包含业务逻辑，仅通过数据绑定与 ViewModel 关联。
- **ViewModel（视图模型）**：作为 View 与 Model 之间的桥梁，负责处理 UI 相关的业务逻辑，管理数据并将其暴露给 View。ViewModel 不持有 View 的引用，生命周期与 View 无关，可在配置变化（如屏幕旋转）时保留数据。

![示意图](D:\MyProject\kotlinDemo\doc\images\mvvm.png)

方法调用链：view-->viewMode-->mode

数据流动链：mode-->viewMode--->view

数据流动的载体：LiveData

viewMode生命感知通过viewModelScope