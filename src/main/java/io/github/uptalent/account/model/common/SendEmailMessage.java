package io.github.uptalent.account.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendEmailMessage {
    private String to;
    private String subject;
    private String text;
}
