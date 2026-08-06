# dozer-extra-converters

[English](./README.md) | [简体中文](./README.zh-CN.md)

Additional custom converters for the Dozer object mapping framework (`dozer-core` 7.x). The converters cover Boolean, fastjson2 JSON, `BigDecimal` and `BigInteger` <-> `String` conversions.

## Table of Contents

- [1. Project Overview](#1-project-overview)
- [2. Features & Status](#2-features--status)
- [3. Requirements & Compatibility](#3-requirements--compatibility)
- [4. Architecture & Modules](#4-architecture--modules)
- [5. Installation](#5-installation)
- [6. Quick Start](#6-quick-start)
- [7. Configuration](#7-configuration)
- [8. Core Usage / API](#8-core-usage--api)
- [9. Testing & Build](#9-testing--build)
- [10. Versioning & Branches](#10-versioning--branches)
- [11. Contributing & License](#11-contributing--license)

## 1. Project Overview

`dozer-extra-converters` (project description: *Dozer Extra*) is a small companion library for the [Dozer](https://github.com/DozerMapper/dozer) mapping framework. It ships ready-made `DozerConverter` implementations for common `String`-related type conversions that are not covered out of the box, so they can be registered into a Dozer mapping without writing converter boilerplate.

| What it is | What it is not |
|:---|:---|
| A set of custom converters for `dozer-core` 7.x | A standalone mapping framework (it builds on Dozer) |
| Thin, dependency-light utility code | A Spring Boot starter (no auto-configuration) |

Typical use cases:

| Use case | Why it helps |
|:---|:---|
| Mapping `Boolean` fields to/from `String` values | `BooleanStringConverter` delegates to Apache Commons BeanUtils `BooleanConverter` |
| Mapping fastjson2 `JSONObject` / `JSONArray` to/from `String` | `JSONObjectStringConverter` / `JSONArrayStringConverter` (fastjson2 2.x) |
| Mapping `BigDecimal` / `BigInteger` to/from `String` | `BigDecimalStringConverter` / `BigIntegerStringConverter` preserve plain string forms |
| Spring environments using Dozer | `dozer-spring4` integration artifact is declared as a compile dependency |

**Project status:** active development.

**Note:** in the current `feature/2.0.x` snapshot the repository tree carries the build configuration (`pom.xml`, `LICENSE`, Maven wrapper); the converter sources are versioned in this repository's git history and on the `master` branch, and are described below as the artifact's public API.

## 2. Features & Status

| Feature | Status | Notes |
|:---|:---|:---|
| `BooleanStringConverter` | Available | `Boolean` <-> `String`, backed by `org.apache.commons.beanutils.converters.BooleanConverter` |
| `JSONObjectStringConverter` | Available | fastjson2 `JSONObject` <-> `String` |
| `JSONArrayStringConverter` | Available | fastjson2 `JSONArray` <-> `String` |
| `BigDecimalStringConverter` | Available | `BigDecimal` <-> `String` (in `converters.number`) |
| `BigIntegerStringConverter` | Available | `BigInteger` <-> `String` (in `converters.number`) |
| `dozer-spring4` integration | Available | `dozer-spring4` 7.0.0 declared as compile dependency |
| Unit tests | Minimal | Test sources exist in the repository lineage (`CodecAndCryptoTest`) |
| CI pipeline | Not configured | No CI workflow files in the repository |

## 3. Requirements & Compatibility

| Requirement | Version |
|:---|:---|
| JDK | 8 |
| Maven | 3.0+ |
| `dozer-core` | 7.0.0 (`com.github.dozermapper`) |
| `dozer-spring4` | 7.0.0 |
| fastjson2 | 2.0.53 |
| slf4j-api | 2.0.18 |
| Lombok | provided scope (annotation processing at build time) |

### Version lines

| Branch | JDK | Version pattern |
|:---|:---|:---|
| `feature/1.0.x` | JDK 8 | `1.0.x.*` |
| `feature/2.0.x` | JDK 17 | `2.0.x.*` |
| `feature/3.0.x` | JDK 21 | `3.0.x.*` |

## 4. Architecture & Modules

```text
  Source value          custom converter               target value
 (Boolean / JSON /   ->  extends                       (String / JSON /
  BigDecimal /            DozerConverter<A,B>            BigDecimal /
  BigInteger)             convertFrom / convertTo        BigInteger)
        |                        |                          |
        +----------> Dozer Mapper (dozer-core 7.0) <--------+
                     registered via mapping definition
                          (XML custom-converters
                            or mapper API)
```

Single module, jar packaging:

| Module | Responsibility |
|:---|:---|
| `dozer-extra-converters` | One jar containing all extra converters (base package `com.github.dozermapper.extra.converters`, numeric converters under `.number`) |

## 5. Installation

### Maven

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>dozer-extra-converters</artifactId>
    <version>2.0.x.x.20260630-SNAPSHOT</version>
</dependency>
```

### Gradle

```groovy
implementation 'io.github.easy4j:dozer-extra-converters:2.0.x.x.20260630-SNAPSHOT'
```

**Availability:** the artifact is published to the Aliyun private Maven repository and distributed through GitHub Releases; it has not yet been published to Maven Central.

## 6. Quick Start

Converters are plain classes; they can be used directly or registered into a Dozer mapping.

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

Expected results: `"true"`, `true`, `{"k":1}`, and a parsed `JSONObject` respectively.

## 7. Configuration

There is no runtime configuration: the converters are plain `DozerConverter` implementations and are wired into Dozer through its custom-converter registration mechanism (mapping XML `custom-converters` or the mapper API). No configuration files or properties are read by this library.

**Assumption:** the exact registration syntax follows the `dozermapper` 7.x documentation; verify the schema URL and element names against the Dozer version you use.

## 8. Core Usage / API

All converters extend `com.github.dozermapper.core.DozerConverter<A, B>` and implement the two abstract methods:

- `A convertFrom(B source, A destination)` — converts from the source side to `A`
- `B convertTo(A source, B destination)` — converts from `A` to the target side

| Converter class | Generic pair |
|:---|:---|
| `com.github.dozermapper.extra.converters.BooleanStringConverter` | `Boolean` <-> `String` |
| `com.github.dozermapper.extra.converters.JSONObjectStringConverter` | `JSONObject` <-> `String` |
| `com.github.dozermapper.extra.converters.JSONArrayStringConverter` | `JSONArray` <-> `String` |
| `com.github.dozermapper.extra.converters.number.BigDecimalStringConverter` | `BigDecimal` <-> `String` |
| `com.github.dozermapper.extra.converters.number.BigIntegerStringConverter` | `BigInteger` <-> `String` |

Example with the numeric converters:

```java
import com.github.dozermapper.extra.converters.number.BigDecimalStringConverter;

BigDecimalStringConverter conv = new BigDecimalStringConverter();
String s = conv.convertTo(new BigDecimal("12345.6700"), null); // "12345.6700" (toPlainString)
BigDecimal d = conv.convertFrom("12345.67", null);             // BigDecimal("12345.67")
```

## 9. Testing & Build

```bash
./mvnw clean verify        # compile, run tests, generate coverage report
./mvnw clean install       # install into the local repository
```

- Coverage is measured with the JaCoCo Maven plugin (`prepare-agent` / `report` / `check` bound to `verify`); the configured target is 90% line coverage, currently with `haltOnFailure=false`.
- The pom keeps the Surefire plugin configuration (including the `skip` flag) from the release workflow — pass `-DskipTests=false` when you explicitly want to execute tests. **Assumption:** behavior may be adjusted in future commits.
- The `release` profile assembles GPG signing + sources + Javadoc + deployment (`./mvnw -Prelease clean deploy`); the `central` profile targets the Maven Central Portal publisher.

## 10. Versioning & Branches

Three parallel version lines are maintained in this repository:

| Branch | JDK | Version pattern | Notes |
|:---|:---|:---|:---|
| `feature/1.0.x` | JDK 8 | `1.0.x.*` | Current line; baseline for legacy applications |
| `feature/2.0.x` | JDK 17 | `2.0.x.*` | Modern line |
| `feature/3.0.x` | JDK 21 | `3.0.x.*` | Latest line |

Maintenance strategy: the 1.0.x line receives bug fixes and dependency updates while JDK 8 remains a supported baseline; feature development primarily targets the 2.0.x / 3.0.x lines.

## 11. Contributing & License

Contributions are welcome — open an issue or submit a pull request against the matching version-line branch (`feature/2.0.x` for JDK 17 changes).

This project is licensed under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0). See the `LICENSE` file in the repository root for details.
