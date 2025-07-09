package jdev.kovalev.service.impl;

import jdev.kovalev.service.EmailSendingService;
import org.springframework.stereotype.Service;

@Service
public class EmailSendingServiceImpl implements EmailSendingService {
    private static final String EMAIL_MESSAGE = """
            Адрес: %s
            Тема: Ваш код подтверждения для AuthService.
            Тело письма: %s
            """;

    @Override
    public void sendEmail(String email, String confirmationCode) {
        System.out.printf(EMAIL_MESSAGE, email, confirmationCode);
    }
}
