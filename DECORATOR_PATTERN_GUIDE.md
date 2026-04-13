# Decorator Pattern - Complete Implementation

## Problem: The Subclass Explosion Problem

### 1. How Many Subclasses Would You Need?

Without the Decorator Pattern, you'd need to create a subclass for **every possible combination** of features.

#### With 3 Features (Encryption, Compression, Timestamp):
- Plain (no decorators): 1
- Encryption only: 1
- Compression only: 1
- Timestamp only: 1
- Encryption + Compression: 1
- Encryption + Timestamp: 1
- Compression + Timestamp: 1
- All three (E + C + T): 1

**Total: 8 classes (2³ = 8) for just 3 features!**

```
EncryptedSender.java
CompressedSender.java
TimestampedSender.java
EncryptedCompressedSender.java
EncryptedTimestampedSender.java
CompressedTimestampedSender.java
EncryptedCompressedTimestampedSender.java
MessageSender.java (base)
```

#### With 5 Features:
You'd need: **2⁵ - 1 = 31 classes**

```
- Encryption
- Compression
- Timestamp
- Translation
- Profanity Filter
```

Every combination would need its own class! Adding one more feature means creating 32 new classes.

#### The Formula:
```
Number of classes needed = 2^n - 1
(where n = number of features)

3 features   → 8 classes (2³)
4 features   → 16 classes (2⁴)
5 features   → 32 classes (2⁵)
10 features  → 1024 classes (2¹⁰) ⚠️
```

### 2. SOLID Principle Violated: Open/Closed Principle (OCP)

The subclass explosion violates the **Open/Closed Principle**:

> "Software entities should be **open for extension** but **closed for modification**."

**Problem with Subclass Explosion:**
- ❌ NOT open for extension: Adding a new feature (e.g., TranslationDecorator) requires creating 2ⁿ new classes
- ❌ NOT closed for modification: Every new feature combination requires NEW classes and potentially modifying existing ones
- ❌ Tight coupling: Each subclass couples multiple features together
- ❌ Code duplication: Logic is copied across many classes
- ❌ Maintainability nightmare: Changes to encryption affect all classes with encryption

**Example:**
If we add a Profanity Filter feature:
- Old classes don't have it
- New classes must duplicate all old combinations PLUS new ones
- Total classes jump from 8 to 16

---

## Solution: The Decorator Pattern

### How Decorators Solve This

Instead of creating 2ⁿ subclasses, create **n decorator classes** (one per feature).

```
Features: Encryption, Compression, Timestamp, Translation
Decorators needed: 4 classes
Combinations possible: INFINITE (stack them in any order)
```

**With Decorator Pattern:**
```
MessageProcessor (Interface)
├── PlainSender (Base implementation)
├── EncryptionDecorator
├── CompressionDecorator
├── TimestampDecorator
└── TranslationDecorator
```

**Total: 5 classes for 4 features** (instead of 32 classes!)

### Core Principle

```
MessageProcessor processor = 
    new CompressionDecorator(           // Outer
        new TimestampDecorator(         // Middle
            new EncryptionDecorator(    // Inner
                new PlainSender()       // Base
            )
        )
    );
```

Each decorator:
1. Wraps another `MessageProcessor`
2. Applies its transformation
3. Delegates to the wrapped processor
4. **Can be combined in any order**
5. **Can be added/removed without modifying existing code**

---

## Implementation

### 1. MessageProcessor Interface

The contract that all processors must implement:

```java
public interface MessageProcessor {
    String process(String message);
}
```

This is crucial - both decorators and the base class implement the same interface, allowing them to be used interchangeably.

### 2. PlainSender (Base Implementation)

The simplest processor - just sends the message:

```java
public class PlainSender implements MessageProcessor {
    @Override
    public String process(String message) {
        System.out.println("[SENT] " + message);
        return message;
    }
}
```

### 3. EncryptionDecorator

Example decorator applying Caesar cipher encryption:

```java
public class EncryptionDecorator implements MessageProcessor {
    private final MessageProcessor processor;

    public EncryptionDecorator(MessageProcessor processor) {
        this.processor = processor;  // Wrap another processor
    }

    @Override
    public String process(String message) {
        // Apply transformation
        StringBuilder encrypted = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                encrypted.append((char) ((c - base + 3) % 26 + base));
            } else {
                encrypted.append(c);
            }
        }
        String result = encrypted.toString();
        
        // Delegate to wrapped processor
        return processor.process(result);
    }
}
```

**Key Points:**
- Accepts another `MessageProcessor` in constructor
- Applies its own transformation
- Calls `processor.process()` to delegate
- Returns the result from the wrapped processor

### 4. CompressionDecorator

Run-length encoding compression:

```java
public class CompressionDecorator implements MessageProcessor {
    private final MessageProcessor processor;

    public CompressionDecorator(MessageProcessor processor) {
        this.processor = processor;
    }

    @Override
    public String process(String message) {
        // Apply compression
        StringBuilder compressed = new StringBuilder();
        int i = 0;
        while (i < message.length()) {
            char c = message.charAt(i);
            int count = 1;
            while (i + count < message.length() && message.charAt(i + count) == c) {
                count++;
            }
            compressed.append(c);
            if (count > 1) compressed.append(count);
            i += count;
        }
        String result = compressed.toString();
        
        // Delegate to wrapped processor
        return processor.process(result);
    }
}
```

### 5. TimestampDecorator

Adds timestamp to messages:

```java
public class TimestampDecorator implements MessageProcessor {
    private final MessageProcessor processor;

    public TimestampDecorator(MessageProcessor processor) {
        this.processor = processor;
    }

    @Override
    public String process(String message) {
        // Add timestamp
        String result = "[" + java.time.LocalTime.now().withNano(0) + "] " + message;
        
        // Delegate to wrapped processor
        return processor.process(result);
    }
}
```

### 6. TranslationDecorator (Added Later - NO CHANGES TO EXISTING CODE!)

Simple word replacement:

```java
public class TranslationDecorator implements MessageProcessor {
    private final MessageProcessor processor;

    public TranslationDecorator(MessageProcessor processor) {
        this.processor = processor;
    }

    @Override
    public String process(String message) {
        // Apply translation
        String translated = message
            .replace("Hello", "Namaste")
            .replace("World", "Duniya");
        
        // Delegate to wrapped processor
        return processor.process(translated);
    }
}
```

**Important:** This new decorator was added WITHOUT modifying ANY existing class!

---

## Combining Decorators

### Single Decorators
```java
// Just encrypted
MessageProcessor processor = 
    new EncryptionDecorator(new PlainSender());

// Just compressed
MessageProcessor processor = 
    new CompressionDecorator(new PlainSender());

// Just timestamped
MessageProcessor processor = 
    new TimestampDecorator(new PlainSender());
```

### Stacking Decorators

Decorators can be wrapped around each other in ANY combination:

```java
// Encrypted THEN Compressed
MessageProcessor processor = 
    new CompressionDecorator(
        new EncryptionDecorator(
            new PlainSender()
        )
    );

// Encrypted THEN Compressed THEN Timestamped
MessageProcessor processor = 
    new TimestampDecorator(
        new CompressionDecorator(
            new EncryptionDecorator(
                new PlainSender()
            )
        )
    );

// With new TranslationDecorator (still no changes to existing code!)
MessageProcessor processor = 
    new CompressionDecorator(
        new EncryptionDecorator(
            new TranslationDecorator(
                new PlainSender()
            )
        )
    );
```

---

## BONUS: Why Order Matters

The ORDER of decorators affects the result because each transformation changes the data characteristics.

### Scenario A: Encrypt → Compress

```
"Hello World!" 
  ↓ (Encrypt - Caesar cipher +3)
"Khoor Zruog!"
  ↓ (Compress - run-length encoding)
"Kho2r Zruog!" (shows compression working on encrypted chars)
```

**Result:** "Kho2r Zruog! Wklv lv d vhfuhw phv2djh."

### Scenario B: Compress → Encrypt

```
"Hello World!"
  ↓ (Compress - run-length encoding)
"Hel2o Wor1ld!" (removes redundant consecutive chars)
  ↓ (Encrypt - Caesar cipher +3)
"KhO2r Zru1og!" (encrypts the compressed data)
```

**Result:** Different! Because encryption operates on different input

### Why This Happens

1. **Encryption → Compression:**
   - Encrypted data may have repeated patterns
   - Run-length encoding can compress these patterns
   - Example: "oo" in "Khoor" becomes "o2"

2. **Compression → Encryption:**
   - Compression removes some redundancy first
   - Less to encrypt afterward
   - Different patterns result in different encryption

3. **Both are valid!**
   - Choose order based on requirements:
   - **Encrypt first** if compression effectiveness matters
   - **Compress first** if reducing transmission size before encryption matters
   - **Timestamp last** to preserve order of transformations

---

## Key Benefits Summary

| Aspect | Without Pattern (Subclasses) | With Decorator Pattern |
|--------|---------------------------|----------------------|
| **Classes for N features** | 2^N - 1 classes | N + 1 classes |
| **Flexibility** | Limited to predefined combos | Infinite combinations |
| **Adding new feature** | Create 2^(N-1) new classes | Create 1 new decorator |
| **Code duplication** | High (logic copied in many classes) | None (each decorator focused) |
| **Maintaining code** | Difficult (changes propagate) | Easy (change one decorator) |
| **SOLID Compliance** | Violates OCP | Follows OCP |
| **Extensibility** | Poor (explosive growth) | Excellent (linear growth) |

---

## Real-World Applications

1. **Java I/O Streams**
   ```java
   InputStream is = new BufferedInputStream(
       new FileInputStream("file.txt")
   );
   ```
   - `FileInputStream` - base
   - `BufferedInputStream` - decorator
   - `DataInputStream` - another decorator
   - Stack as needed!

2. **Spring Framework**
   - Decorating beans with proxies for transaction management, security, caching

3. **GUI Frameworks**
   - Window decorators: borders, scrollbars, shadow effects

4. **Coffee Shop POS System**
   ```java
   Coffee beverage = new Espresso();
   beverage = new MilkDecorator(beverage);
   beverage = new ChocolateDecorator(beverage);
   beverage = new WhippedCreamDecorator(beverage);
   System.out.println(beverage.getCost());  // Automatically calculates cost!
   ```

5. **Message Processing (This example!)**
   - Add/remove encryption, compression, logging, etc. dynamically

---

## Comparison: Old vs New

### OLD WAY (Subclass Explosion)
```
MessageSender (base)
├── EncryptedSender
├── CompressedSender
├── TimestampedSender
├── TranslatedSender
├── EncryptedCompressedSender (duplicate logic!)
├── EncryptedTimestampedSender (duplicate logic!)
├── EncryptedTranslatedSender (duplicate logic!)
├── CompressedTimestampedSender (duplicate logic!)
├── CompressedTranslatedSender (duplicate logic!)
├── TimestampedTranslatedSender (duplicate logic!)
├── EncryptedCompressedTimestampedSender (duplicate logic!)
├── EncryptedCompressedTranslatedSender (duplicate logic!)
├── EncryptedTimestampedTranslatedSender (duplicate logic!)
├── CompressedTimestampedTranslatedSender (duplicate logic!)
└── AllDecoratorsOn (duplicate logic!)
```

**Total: 15 classes for 4 features!**
**Problem: Adding feature #5 means creating 32 new classes!**

### NEW WAY (Decorator Pattern)
```
MessageProcessor (interface)
├── PlainSender
├── EncryptionDecorator
├── CompressionDecorator
├── TimestampDecorator
└── TranslationDecorator
```

**Total: 5 classes for 4 features!**
**Adding feature #5? Just create 1 new decorator!**

---

## Summary

The **Decorator Pattern** solves the subclass explosion problem by:

1. ✅ Creating a single decorator per feature
2. ✅ Allowing infinite combinations through wrapping
3. ✅ Enabling new decorators without modifying existing code
4. ✅ Maintaining single responsibility (each decorator does one thing)
5. ✅ Following the Open/Closed Principle
6. ✅ Reducing code duplication dramatically

**Lesson:** When facing exponential complexity (2^N), consider composition over inheritance!

---

