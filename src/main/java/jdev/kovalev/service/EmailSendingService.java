package jdev.kovalev.service;

public interface EmailSendingService {
    void sendEmail(String email, String message);
}
