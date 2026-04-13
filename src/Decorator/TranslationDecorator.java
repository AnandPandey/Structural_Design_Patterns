package Decorator;

public class TranslationDecorator implements MessageProcessor {
    private final MessageProcessor processor;

    public TranslationDecorator(MessageProcessor processor) {
        this.processor = processor;
    }

    @Override
    public String process(String message) {
        String translated = message.replace("Hello", "Namaste").replace("World", "Duniya");
        return processor.process(translated);
    }
}
