# 🌱 Spring Boot - Dependency Injection (DI)

## 📌 What is Dependency Injection?

Dependency Injection (DI) is a design pattern where the **Spring IoC Container** creates and injects the required dependencies into a bean instead of the object creating them itself.

---

## 🛠️ Types of Dependency Injection

### 1. Constructor Injection ✅ (Recommended)

```java
@Service
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

**Pros**
- ✅ Mandatory dependencies
- ✅ Supports `final` fields
- ✅ Immutable
- ✅ Easy to unit test

---

### 2. Setter Injection

```java
@Service
public class OrderService {

    private PaymentService paymentService;

    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

**Pros**
- ✅ Suitable for optional dependencies

**Note**
- Spring automatically calls the setter using **Reflection**.
- You never call the setter yourself.

---

### 3. Field Injection ❌

```java
@Service
public class OrderService {

    @Autowired
    private PaymentService paymentService;
}
```

**Cons**
- ❌ Hidden dependencies
- ❌ Difficult to test
- ❌ Doesn't support `final`
- ❌ Not recommended

---

# ⚙️ How `@Autowired` Works Internally

```
Application Starts
        │
        ▼
Component Scan
        │
        ▼
Create Bean Definitions
        │
        ▼
Instantiate Beans
        │
        ▼
BeanPostProcessor
        │
        ▼
Detect @Autowired
        │
        ▼
Find Dependency in IoC Container
        │
        ▼
Inject Dependency (Reflection)
```

---

## 🔍 Field Injection Internally

Spring uses Reflection:

```java
field.set(orderService, paymentServiceBean);
```

---

## 🔍 Setter Injection Internally

Spring invokes the setter using Reflection:

```java
method.invoke(orderService, paymentServiceBean);
```

---

## 🔍 Constructor Injection Internally

Spring resolves dependencies first and then creates the object.

```java
new OrderService(paymentServiceBean);
```

No Reflection is required to set the field afterward.

---

# 🎯 Interview One-Liner

> Spring scans the application, creates beans inside the IoC container, and the `AutowiredAnnotationBeanPostProcessor` detects `@Autowired`, resolves the dependency by type, and injects it using Reflection (or constructor invocation for constructor injection).

---

⭐ **Best Practice:** Prefer **Constructor Injection** for mandatory dependencies.
---

# 🔗 Loose Coupling & Multiple Implementations

## 🎯 Why Loose Coupling?

Without DI:

```java
private RazorpayService paymentService = new RazorpayService();
```

- ❌ Tightly coupled to `RazorpayService`
- ❌ Code changes required to switch implementation

With DI:

```java
private final PaymentService paymentService;
```

- ✅ Depends on the **interface**, not the implementation.
- ✅ Spring decides which implementation to inject.

---

## 🤔 What does `@Qualifier` do?

```java
public OrderService(
    @Qualifier("stripeService")
    PaymentService paymentService) {
    this.paymentService = paymentService;
}
```

`@Qualifier` tells **Spring** exactly which bean to inject.

> **Note:** It is only used when **Spring creates the object**. If you instantiate the class using `new`, Spring (and `@Qualifier`) is not involved.

---

## 🔄 Can `@Qualifier` change at runtime?

❌ No.

Once Spring creates the bean, the dependency is fixed.

To choose implementations dynamically at runtime, use:

- `Map<String, PaymentService>`
- `List<PaymentService>`
- Factory Pattern
- Strategy Pattern
- Profiles / Conditional Beans

Example:

```java
public OrderService(Map<String, PaymentService> services) {
    this.services = services;
}

services.get("stripeService").pay();
services.get("razorpayService").pay();
```

---

## ⚠️ Multiple Implementations

```text
PaymentService
     │
     ├── RazorpayService
     └── StripeService
```

If Spring needs **one** `PaymentService`, it doesn't know which one to inject.

Result:

```text
NoUniqueBeanDefinitionException
```

---

## ✅ Ways to Resolve Ambiguity

### 1. `@Qualifier`

```java
@Qualifier("stripeService")
```

Injects the specified bean.

---

### 2. `@Primary`

```java
@Service
@Primary
class RazorpayService implements PaymentService {}
```

Makes it the default choice.

---

### 3. Inject All Beans

```java
List<PaymentService> services
```

or

```java
Map<String, PaymentService> services
```

Spring injects **all** matching implementations.

---

## 💡 Key Takeaways

- ✅ Spring resolves dependencies **by type**.
- ✅ If only one bean exists → injected automatically.
- ❌ If multiple beans exist → ambiguity must be resolved.
- ✅ Resolve using `@Qualifier`, `@Primary`, or inject all implementations.
- ✅ `@Qualifier` fixes the implementation **for that Spring-managed bean**.
- ✅ Loose coupling comes from depending on an **interface**, not from changing implementations at any moment.

---

# 🎤 Interview One-Liners

### Q: Why use interfaces for DI?

> Programming to an interface keeps business logic independent of concrete implementations, allowing implementations to change without modifying the dependent class.

### Q: When is `@Qualifier` required?

> When multiple beans of the same type exist and Spring needs to inject a single bean.

### Q: Does `@Qualifier` support runtime switching?

> No. It selects a bean during Spring bean creation. For runtime selection, use a Strategy/Factory pattern or inject all implementations.

### Q: Does Spring inject by name or by type?

> Spring first resolves dependencies **by type**. If multiple beans match, it uses `@Qualifier` or `@Primary` to resolve the ambiguity.