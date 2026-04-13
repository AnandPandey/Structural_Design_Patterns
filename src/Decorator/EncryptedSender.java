package Decorator;

public class EncryptedSender implements MessageProcessor{
    private final MessageProcessor processor;

    public EncryptedSender(MessageProcessor p) {
        this.processor = p;
    }

    @Override
    public String process(String msq) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : msq.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                encrypted.append(
                    (char)((c - base + 3) % 26 + base));
            } else {
                encrypted.append(c);
            }
        }
        String result = encrypted.toString();
        return processor.process(result);
    }
}
