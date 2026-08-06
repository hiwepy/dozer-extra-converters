# dozer-extra-converters

[English](./README.md) | [简体中文](./README.zh-CN.md)

为 Dozer 对象映射框架（`dozer-core` 7.x）提供的附加自定义转换器，覆盖 Boolean、fastjson2 JSON、`BigDecimal`、`BigInteger` 与 `String` 之间的转换。

## 目录

- [1. 项目概览](#1-项目概览)
- [2. 功能与状态](#2-features--status)
- [3. 环境要求与兼容性](#3-requirements--compatibility)
- [4. 架构与模块](#4-architecture--modules)
- [5. 安装](#5-installation)
- [6. 快速开始](#6-quick-start)
- [7. 配置](#7-configuration)
- [8. 核心用法 / API](#8-core-usage--api)
- [9. 测试与构建](#9-testing--build)
- [10. 版本线与分支](#10-versioning--branches)
- [11. 参与贡献与许可协议](#11-contributing--license)

## 1. 项目概览

`dozer-extra-converters`（项目描述：*Dozer Extra*）是 [Dozer](https://github.com/DozerMapper/dozer) 映射框架的配套小库，内置了开箱即用的 `DozerConverter` 实现，覆盖 Dozer 默认不提供的常见 `String` 相关类型转换，无需重复编写转换器样板代码即可注册进 Dozer 映射。

| 是什么 | 不是什么 |
|:---|:---|
| 面向 `dozer-core` 7.x 的一组自定义转换器 | 独立的映射框架（它构建在 Dozer 之上） |
| 轻量、低依赖的工具代码 | Spring Boot Starter（不含自动配置） |

典型使用场景：

| 场景 | 收益 |
|:---|:---|
| `Boolean` 字段与 `String` 互转 | `BooleanStringConverter` 委托 Apache Commons BeanUtils 的 `BooleanConverter` |
| fastjson2 `JSONObject` / `JSONArray` 与 `String` 互转 | `JSONObjectStringConverter` / `JSONArrayStringConverter`（fastjson2 2.x） |
| `BigDecimal` / `BigInteger` 与 `String` 互转 | `BigDecimalStringConverter` / `BigIntegerStringConverter` 保留字符串原始形态 |
| 使用 Dozer 的 Spring 环境 | `dozer-spring4` 集成构件以 compile 依赖声明 |

**项目状态：** 活跃开发。

**说明：** 当前 `feature/1.0.x` 快照的仓库树中保留的是构建配置（`pom.xml`、`LICENSE`、Maven Wrapper）；转换器源码保存在本仓库的 git 历史与 `master` 分支中，下文将其作为构件公开 API 描述。

<a id="2-features--status"></a>
## 2. 功能与状态

| 能力 | 状态 | 说明 |
|:---|:---|:---|
| `BooleanStringConverter` | 可用 | `Boolean` <-> `String`，基于 `org.apache.commons.beanutils.converters.BooleanConverter` |
| `JSONObjectStringConverter` | 可用 | fastjson2 `JSONObject` <-> `String` |
| `JSONArrayStringConverter` | 可用 | fastjson2 `JSONArray` <-> `String` |
| `BigDecimalStringConverter` | 可用 | `BigDecimal` <-> `String`（位于 `converters.number`） |
| `BigIntegerStringConverter` | 可用 | `BigInteger` <-> `String`（位于 `converters.number`） |
| `dozer-spring4` 集成 | 可用 | `dozer-spring4` 7.0.0 以 compile 依赖声明 |
| 单元测试 | 较少 | 仓库历史中存在测试源码（`CodecAndCryptoTest`） |
| CI 流水线 | 未配置 | 仓库中无 CI 工作流文件 |

<a id="3-requirements--compatibility"></a>
## 3. 环境要求与兼容性

| 依赖项 | 版本 |
|:---|:---|
| JDK | 8 |
| Maven | 3.0+ |
| `dozer-core` | 7.0.0（`com.github.dozermapper`） |
| `dozer-spring4` | 7.0.0 |
| fastjson2 | 2.0.53 |
| slf4j-api | 2.0.18 |
| Lombok | provided 作用域（构建期注解处理） |

### 版本线矩阵

| 分支 | JDK | 版本号模式 |
|:---|:---|:---|
| `feature/1.0.x` | JDK 8 | `1.0.x.*` |
| `feature/2.0.x` | JDK 17 | `2.0.x.*` |
| `feature/3.0.x` | JDK 21 | `3.0.x.*` |

<a id="4-architecture--modules"></a>
## 4. 架构与模块

```text
  源值                 自定义转换器                  目标值
 (Boolean / JSON /  ->  继承                        (String / JSON /
  BigDecimal /           DozerConverter<A,B>          BigDecimal /
  BigInteger)            convertFrom / convertTo       BigInteger)
        |                        |                         |
        +----------> Dozer Mapper (dozer-core 7.0) <-------+
                     通过映射定义注册
                    （XML custom-converters
                       或 mapper API）
```

单一模块，jar 打包：

| 模块 | 职责 |
|:---|:---|
| `dozer-extra-converters` | 包含全部附加转换器的单个 jar（基础包 `com.github.dozermapper.extra.converters`，数字转换器位于 `.number`） |

<a id="5-installation"></a>
## 5. 安装

### Maven

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>dozer-extra-converters</artifactId>
    <version>1.0.x.20260630-SNAPSHOT</version>
</dependency>
```

### Gradle

```groovy
implementation 'io.github.easy4j:dozer-extra-converters:1.0.x.20260630-SNAPSHOT'
```

**可用性：** 构件发布至阿里云私有 Maven 仓库，并通过 GitHub Releases 分发；尚未发布到 Maven Central。

<a id="6-quick-start"></a>
## 6. 快速开始

转换器是普通类，可直接使用，也可注册进 Dozer 映射。

```java
import com.alibaba.fastjson2.JSONObject;
import com.github.dozermapper.extra.converters.BooleanStringConverter;
import com.github.dozermapper.extra.converters.JSONObjectStringConverter;

BooleanStringConverter boolConv = new BooleanStringConverter();
String flag = boolConv.convertTo(Boolean.TRUE, null);            // "true"
Boolean restored = boolConv.convertFrom("true", null);           // true

JSONObjectStringConverter jsonConv = new JSONObjectStringConverter();
String json = jsonConv.convertTo(JSONObject.parseObject("{\"k\":1}"), null); // {"k":1}
JSONObject obj = jsonConv.convertFrom("{\"k\":1}", null);        // JSONObject{k:1}
```

预期结果依次为 `"true"`、`true`、`{"k":1}` 以及解析后的 `JSONObject`。

<a id="7-configuration"></a>
## 7. 配置

本库无运行时配置：转换器是纯 `DozerConverter` 实现，通过 Dozer 的自定义转换器注册机制（映射 XML 的 `custom-converters` 或 mapper API）接入。库本身不读取任何配置文件或属性。

**假设：** 具体注册语法以 `dozermapper` 7.x 文档为准；schema 地址与元素名请对照所使用 Dozer 版本核实。

<a id="8-core-usage--api"></a>
## 8. 核心用法 / API

所有转换器继承 `com.github.dozermapper.core.DozerConverter<A, B>` 并实现两个抽象方法：

- `A convertFrom(B source, A destination)` — 从源侧转换为 `A`
- `B convertTo(A source, B destination)` — 从 `A` 转换为目标侧

| 转换器类 | 泛型对 |
|:---|:---|
| `com.github.dozermapper.extra.converters.BooleanStringConverter` | `Boolean` <-> `String` |
| `com.github.dozermapper.extra.converters.JSONObjectStringConverter` | `JSONObject` <-> `String` |
| `com.github.dozermapper.extra.converters.JSONArrayStringConverter` | `JSONArray` <-> `String` |
| `com.github.dozermapper.extra.converters.number.BigDecimalStringConverter` | `BigDecimal` <-> `String` |
| `com.github.dozermapper.extra.converters.number.BigIntegerStringConverter` | `BigInteger` <-> `String` |

数字转换器示例：

```java
import com.github.dozermapper.extra.converters.number.BigDecimalStringConverter;

BigDecimalStringConverter conv = new BigDecimalStringConverter();
String s = conv.convertTo(new BigDecimal("12345.6700"), null); // "12345.6700"（toPlainString）
BigDecimal d = conv.convertFrom("12345.67", null);             // BigDecimal("12345.67")
```

<a id="9-testing--build"></a>
## 9. 测试与构建

```bash
./mvnw clean verify        # 编译、运行测试、生成覆盖率报告
./mvnw clean install       # 安装到本地仓库
```

- 覆盖率由 JaCoCo Maven 插件度量（`prepare-agent` / `report` / `check` 绑定到 `verify`）；配置目标为 90% 行覆盖率，当前 `haltOnFailure=false`。
- pom 保留了发布工作流中的 Surefire 插件配置（含 `skip` 标志）——需要显式执行测试时请加 `-DskipTests=false`。**假设：** 该行为可能在未来提交中调整。
- `release` profile 组装 GPG 签名 + 源码 + Javadoc + 部署（`./mvnw -Prelease clean deploy`）；`central` profile 面向 Maven Central Portal 发布。

<a id="10-versioning--branches"></a>
## 10. 版本线与分支

仓库维护三条并行版本线：

| 分支 | JDK | 版本号模式 | 说明 |
|:---|:---|:---|:---|
| `feature/1.0.x` | JDK 8 | `1.0.x.*` | 当前版本线；面向存量应用基线 |
| `feature/2.0.x` | JDK 17 | `2.0.x.*` | 现代化版本线 |
| `feature/3.0.x` | JDK 21 | `3.0.x.*` | 最新版本线 |

维护策略：在 JDK 8 仍作为受支持基线期间，1.0.x 版本线接收缺陷修复与依赖更新；新功能开发主要面向 2.0.x / 3.0.x 版本线。

<a id="11-contributing--license"></a>
## 11. 参与贡献与许可协议

欢迎参与贡献——请通过 Issue 反馈问题，或向对应版本线分支提交 Pull Request（JDK 8 相关改动提交到 `feature/1.0.x`）。

本项目基于 [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0) 许可发布。详见仓库根目录的 `LICENSE` 文件。
