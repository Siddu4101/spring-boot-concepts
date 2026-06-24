# Spring IoC, BeanFactory & ApplicationContext


---

# 📌 Big Picture

```text
                 Inversion of Control (IoC)
                      (Design Principle)
                             │
                             ▼
                  Spring IoC Container
                             │
              ┌──────────────┴──────────────┐
              │                             │
              ▼                             ▼
        BeanFactory               ApplicationContext
        (Basic Container)        (Advanced Container)
```

---

# 🧠 What is IoC (Inversion of Control)?

IoC is a **design principle** where the responsibility of creating and managing objects is transferred from your application to the Spring Container.

### ❌ Traditional Java

```java
PaymentService service = new RazorpayPaymentService();
```

Your code creates the object.

---

### ✅ Spring IoC

```java
@Autowired
private PaymentService paymentService;
```

Spring creates and injects the dependency.

---

## 💡 In one line

> **You write what you need. Spring decides how and when to create it.**

---

# 📦 What is an IoC Container?

The IoC Container is responsible for:

* ✅ Reading configuration
* ✅ Creating beans
* ✅ Managing bean lifecycle
* ✅ Injecting dependencies

Spring provides two IoC container implementations:

```text
IoC Container
      │
      ├── BeanFactory
      │
      └── ApplicationContext
```

---

# 🏭 BeanFactory

The **basic implementation** of the IoC container.

## Responsibilities

* Create Beans
* Dependency Injection
* Bean Lifecycle Management

### Default Behavior

**Lazy Initialization**

```text
Application Starts
        │
Read Bean Definitions
        │
Store Metadata
        │
(No Objects Created)
        │
getBean()
        │
Create Bean
        │
Inject Dependencies
        │
Return Bean
```

### ✔ Pros

* Lightweight
* Less memory
* Suitable for small applications

### ❌ Cons

* No enterprise features
* Configuration errors detected late

---

# 🚀 ApplicationContext

ApplicationContext **extends BeanFactory**.

```text
BeanFactory
      ▲
      │
ApplicationContext
```

It provides everything BeanFactory offers **plus** many enterprise features.

---

## Default Behavior

### Eager Initialization

```text
Application Starts
        │
Read Bean Definitions
        │
Create Singleton Beans
        │
Inject Dependencies
        │
Run @PostConstruct
        │
Application Ready
```

Later,

```java
context.getBean(OrderService.class);
```

returns the already-created bean.

---

# 📖 BeanFactory vs ApplicationContext

| Feature                             | BeanFactory | ApplicationContext |
| ----------------------------------- | ----------- | ------------------ |
| IoC Container                       | ✅           | ✅                  |
| Dependency Injection                | ✅           | ✅                  |
| Bean Lifecycle                      | ✅           | ✅                  |
| Lazy Initialization                 | ✅ Default   | Optional           |
| Eager Initialization                | ❌           | ✅ Default          |
| Event Publishing                    | ❌           | ✅                  |
| Internationalization (i18n)         | ❌           | ✅                  |
| Environment & Properties            | ❌           | ✅                  |
| BeanPostProcessor Auto Registration | Limited     | ✅                  |
| AOP Integration                     | Basic       | Better             |
| Used in Spring Boot                 | Rare        | ✅                  |

---

# ⚙️ Internal Working of ApplicationContext

```text
@SpringBootApplication
          │
          ▼
Create ApplicationContext
          │
          ▼
Component Scan
(@Component, @Service, @Repository...)
          │
          ▼
Create Bean Definitions
          │
          ▼
Instantiate Singleton Beans
          │
          ▼
Resolve Dependencies
          │
          ▼
@Autowired Injection
          │
          ▼
@PostConstruct
          │
          ▼
Application Ready
```

---

# 📄 What is a Bean Definition?

Spring stores metadata for every bean.

```text
Bean Definition
──────────────────────────────
Class       : PaymentService
Bean Name   : paymentService
Scope       : Singleton
Lazy        : false
Dependencies:
   ├── OrderRepository
   └── EmailService
Init Method : init()
Destroy     : destroy()
──────────────────────────────
```

Think of it as a **recipe** Spring uses whenever it needs to create or manage a bean.

---




# 🎯 Where does @Autowired fit?

`@Autowired` is **not** the IoC container.

It is simply an instruction saying:

> "While creating this bean, inject another bean of the required type here."

The IoC container performs the actual injection.

---

# 🏠 Real-Life Analogy

Imagine a restaurant 🍽️

## IoC

Customers don't cook their own food.

The restaurant takes responsibility.

---

## BeanFactory 👨‍🍳

The chef cooks **only when someone orders**.

(Lazy Initialization)

---

## ApplicationContext 👨‍🍳👨‍💼

The restaurant:

* prepares popular dishes beforehand
* checks ingredient availability
* manages staff
* announces events
* supports multiple languages
* serves customers immediately

(Eager Initialization + Enterprise Features)

---

# 🎯 Interview One-Liners

### What is IoC?

> A design principle where object creation and dependency management are delegated to the Spring Container.

---

### What is the IoC Container?

> A Spring container responsible for creating, configuring, injecting, and managing bean lifecycles.

---

### What is BeanFactory?

> The basic implementation of the Spring IoC container that creates beans lazily by default.

---

### What is ApplicationContext?

> An advanced IoC container that extends BeanFactory, eagerly creates singleton beans by default, and provides enterprise features like event publishing, i18n, AOP integration, and environment support.

---

### Which one does Spring Boot use?

> **ApplicationContext**

---

# 📝 Quick Revision

```text
IoC
│
├── Design Principle
│
▼

IoC Container
│
├── Creates Beans
├── Injects Dependencies
├── Manages Lifecycle
│
├── BeanFactory
│      └── Lazy Initialization
│
└── ApplicationContext
       ├── Extends BeanFactory
       ├── Eager Singleton Creation
       ├── Events
       ├── i18n
       ├── AOP
       ├── Environment Support
       └── Used by Spring Boot
```

---

# ⭐ Key Takeaway

* **IoC** → Design Principle
* **IoC Container** → Spring implementation of IoC
* **BeanFactory** → Basic IoC container (Lazy)
* **ApplicationContext** → Advanced IoC container (Eager + Enterprise Features)
* **Spring Boot always uses ApplicationContext**


# Different bean scopes

| Scope       | Description                                                             |
| ----------- | ----------------------------------------------------------------------- |
| Singleton   | One bean instance per Spring IoC container (default scope).            |
| Prototype   | A new bean instance is created every time it is requested.             |
| Request     | One bean instance per HTTP request.                                    |
| Session     | One bean instance per HTTP session.                                    |
| Application | One bean instance shared across the entire web application (`ServletContext`). |
| WebSocket   | One bean instance per WebSocket session.                               |


# ⚠️ Handling Multiple Beans of the Same Type

When you have more than one implementation of the same type, use one of the following approaches:

| Approach | Description |
| -------- | ----------- |
| `@Qualifier` | Explicitly specify which bean to inject by name. |
| `@Primary` | Mark one bean as the default choice when multiple implementations exist. |
| `List<BeanType>` | Inject all implementations as a list. |
| `Map<String, BeanType>` | Inject all implementations as a map with bean names as keys. |
