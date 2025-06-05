package com.ticketoffice.backend.infra.adapters.out.emails;

import com.ticketoffice.backend.domain.ports.EmailService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Override
    public void sendEmail(String content, List<String> to, String from, String subject) {
        System.out.printf("Sending email to %s \n from: %s \n subject: %s \n body:\n\n %s \n",
                to.stream().reduce("", (a, b) -> a + ", " + b),
                from,
                subject,
                content
        );
    }
}
