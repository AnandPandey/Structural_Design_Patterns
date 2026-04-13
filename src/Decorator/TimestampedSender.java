package Decorator;

public class TimestampedSender implements MessageProcessor {
    private final MessageProcessor processor;

    public TimestampedSender(MessageProcessor p) {
        this.processor = p;
    }

    @Override
    public String process(String msq) {
        String result = "[" +
            java.time.LocalTime.now().withNano(0) +
            "] " + msq;
        return processor.process(result);
    }
}
