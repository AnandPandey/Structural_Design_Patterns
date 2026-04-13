package Decorator;

public class CompressedSender implements MessageProcessor {
    private final MessageProcessor processor;

    public CompressedSender(MessageProcessor p) {
        this.processor = p;
    }

    @Override
    public String process(String msq) {
        StringBuilder compressed = new StringBuilder();
        int i = 0;
        while (i < msq.length()) {
            char c = msq.charAt(i);
            int count = 1;
            while (i + count < msq.length()
                && msq.charAt(i + count) == c) {
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
