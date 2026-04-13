package Decorator;

public class MessageSender implements MessageProcessor{
    @Override
    public String process(String msq) {
        System.out.println("[SENT] " + msq);
        return msq;
    }
}
