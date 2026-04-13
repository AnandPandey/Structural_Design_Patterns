package Decorator;

public class Main {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║           DECORATOR PATTERN - MESSAGE PROCESSING                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        String msg = "Hello World! This is a secret message.";
        System.out.println("Original Message: \"" + msg + "\"\n");

        // ========== SINGLE DECORATORS ==========
        System.out.println("═══ SINGLE DECORATORS ═══\n");

        // Plain sender (no decorators)
        System.out.println("1) PLAIN (No decorators):");
        MessageProcessor plain = new PlainSender();
        plain.process(msg);
        System.out.println();

        // Encrypted only
        System.out.println("2) ENCRYPTED ONLY:");
        MessageProcessor encrypted = new EncryptionDecorator(new PlainSender());
        encrypted.process(msg);
        System.out.println();

        // Compressed only
        System.out.println("3) COMPRESSED ONLY:");
        MessageProcessor compressed = new CompressionDecorator(new PlainSender());
        compressed.process(msg);
        System.out.println();

        // Timestamped only
        System.out.println("4) TIMESTAMPED ONLY:");
        MessageProcessor timestamped = new TimestampDecorator(new PlainSender());
        timestamped.process(msg);
        System.out.println();

        // Translated only
        System.out.println("5) TRANSLATED ONLY:");
        MessageProcessor translated = new TranslationDecorator(new PlainSender());
        translated.process(msg);
        System.out.println();

        // ========== STACKING DECORATORS (Different Combinations) ==========
        System.out.println("═══ STACKING DECORATORS - Multiple Combinations ═══\n");

        // Combination 1: Encrypted + Compressed
        System.out.println("6) COMBINATION 1: ENCRYPTED → COMPRESSED:");
        System.out.println("   (Encryption applied first, then compression)");
        MessageProcessor combo1 = new CompressionDecorator(
            new EncryptionDecorator(new PlainSender())
        );
        combo1.process(msg);
        System.out.println();

        // Combination 2: Compressed + Timestamped
        System.out.println("7) COMBINATION 2: COMPRESSED → TIMESTAMPED:");
        System.out.println("   (Compression applied first, then timestamp)");
        MessageProcessor combo2 = new TimestampDecorator(
            new CompressionDecorator(new PlainSender())
        );
        combo2.process(msg);
        System.out.println();

        // Combination 3: Encrypted + Timestamped
        System.out.println("8) COMBINATION 3: ENCRYPTED → TIMESTAMPED:");
        System.out.println("   (Encryption applied first, then timestamp)");
        MessageProcessor combo3 = new TimestampDecorator(
            new EncryptionDecorator(new PlainSender())
        );
        combo3.process(msg);
        System.out.println();

        // ========== ALL DECORATORS STACKED ==========
        System.out.println("═══ ALL DECORATORS STACKED ═══\n");

        // All three: Timestamped + Encrypted + Compressed
        System.out.println("9) ALL THREE: ENCRYPTED → COMPRESSED → TIMESTAMPED:");
        System.out.println("   (Order: Encryption → Compression → Timestamp)");
        MessageProcessor all = new TimestampDecorator(
            new CompressionDecorator(
                new EncryptionDecorator(new PlainSender())
            )
        );
        all.process(msg);
        System.out.println();

        // ========== ADDING NEW DECORATOR WITHOUT CHANGING EXISTING CODE ==========
        System.out.println("═══ NEW DECORATOR (TranslationDecorator) - Added WITHOUT changing existing code ═══\n");

        // Translation with timestamp
        System.out.println("10) TRANSLATION + ENCRYPTED + COMPRESSED:");
        System.out.println("   (Shows TranslationDecorator works seamlessly with existing decorators)");
        MessageProcessor withTranslation = new CompressionDecorator(
            new EncryptionDecorator(
                new TranslationDecorator(new PlainSender())
            )
        );
        withTranslation.process(msg);
        System.out.println();

        // ========== BONUS: ORDER MATTERS! ==========
        System.out.println("═══ BONUS: ORDER OF DECORATORS MATTERS! ═══\n");
        System.out.println("IMPORTANT: The order in which decorators are applied affects the result!\n");

        System.out.println("SCENARIO A: ENCRYPT FIRST, THEN COMPRESS");
        System.out.println("Message flow: Plaintext → [Encrypt] → [Compress] → Send");
        System.out.println("Result: Each encrypted character may appear multiple times,");
        System.out.println("        so compression can be effective on encrypted data.\n");

        MessageProcessor encryptThenCompress = new CompressionDecorator(
            new EncryptionDecorator(new PlainSender())
        );
        System.out.print("Output: ");
        String encThenCompResult = encryptThenCompress.process(msg);
        System.out.println();

        System.out.println("SCENARIO B: COMPRESS FIRST, THEN ENCRYPT");
        System.out.println("Message flow: Plaintext → [Compress] → [Encrypt] → Send");
        System.out.println("Result: Compression reduces size first, then encryption");
        System.out.println("        scrambles the compressed data.\n");

        MessageProcessor compressThenEncrypt = new EncryptionDecorator(
            new CompressionDecorator(new PlainSender())
        );
        System.out.print("Output: ");
        String compThenEncResult = compressThenEncrypt.process(msg);
        System.out.println();

        System.out.println("ANALYSIS:");
        System.out.println("─────────");
        System.out.println("1. Different order = Different output (both valid, just different)");
        System.out.println("2. Encryption + Compression: Can achieve better compression on");
        System.out.println("   encrypted data if it has patterns");
        System.out.println("3. Compression + Encryption: Reduces size before encryption,");
        System.out.println("   faster encryption");
        System.out.println("4. WHY? Each transformation changes the data characteristics:");
        System.out.println("   - Encryption: Scrambles patterns (may reduce compressibility)");
        System.out.println("   - Compression: Removes redundancy");
        System.out.println();

        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║ KEY BENEFIT OF DECORATOR PATTERN:                              ║");
        System.out.println("║ Add new decorators (like TranslationDecorator) without         ║");
        System.out.println("║ modifying any existing class! Just wrap it around others.     ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }

    /**
     * Helper method to demonstrate decorator behavior
     */
    private static void demonstrateDecorator(String title, MessageProcessor processor, String message) {
        System.out.println(title);
        processor.process(message);
    }
}
