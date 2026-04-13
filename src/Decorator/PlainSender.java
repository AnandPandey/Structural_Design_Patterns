package Decorator;

public class PlainSender implements MessageProcessor {
    @Override
    public String process(String message) {
        System.out.println("[SENT] " + message);
        return message;
    }
}
