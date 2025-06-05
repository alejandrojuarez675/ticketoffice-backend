package com.ticketoffice.backend.domain.ports;

import java.util.List;

public interface EmailService {
    void sendEmail(String content, List<String> to, String from, String subject);
}
