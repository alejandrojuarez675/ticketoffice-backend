package com.ticketoffice.backend.infra.adapters.in.controller.checkout;

import com.ticketoffice.backend.infra.adapters.in.dto.request.ContactToUsRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/v1/question-and-answer")
public class QuestionAndAnswerController {

//    @PostMapping
//    @Operation(
//            summary = "Contact to us endpoint",
//            description = "This endpoint is used to send a question to the support team.\n" +
//                    "This endpoint should be called when the user wants to ask a question.",
//            tags = {"public-endpoints"}
//    )
    public ResponseEntity<Void> contactToUs(
            @RequestBody ContactToUsRequest question
    ) {
        return ResponseEntity.ok().build();
    }
}
