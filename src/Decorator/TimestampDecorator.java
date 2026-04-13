package Decorator;

public class TimestampDecorator implements MessageProcessor {
    private final MessageProcessor processor;

    public TimestampDecorator(MessageProcessor processor) {
        this.processor = processor;
    }

    @Override
    public String process(String message) {
        String result = "[" + java.time.LocalTime.now().withNano(0) + "] " + message;
        return processor.process(result);
    }
}
