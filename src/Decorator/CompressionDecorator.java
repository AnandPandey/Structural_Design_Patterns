package Decorator;

public class CompressionDecorator implements MessageProcessor {
    private final MessageProcessor processor;

    public CompressionDecorator(MessageProcessor processor) {
        this.processor = processor;
    }

    @Override
    public String process(String message) {
        // Simple run-length encoding
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
        return processor.process(result);
    }
}
