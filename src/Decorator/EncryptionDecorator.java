package Decorator;

public class EncryptionDecorator implements MessageProcessor {
    private final MessageProcessor processor;

    public EncryptionDecorator(MessageProcessor processor) {
        this.processor = processor;
    }

    @Override
    public String process(String message) {
        // Simple Caesar cipher (shift by 3)
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
        return processor.process(result);
    }
}
