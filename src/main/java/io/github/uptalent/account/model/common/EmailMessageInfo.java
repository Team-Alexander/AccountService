package io.github.uptalent.account.model.common;

import io.github.uptalent.account.model.constant.EmailMessageLinkType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EmailMessageInfo {
    private String uuid;
    private String username;
    private String email;
    private LocalDateTime expiredDateTime;
    private EmailMessageLinkType messageLinkType;
}
