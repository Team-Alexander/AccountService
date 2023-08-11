package io.github.uptalent.account.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailMessageLinkType {
    VERIFY("/verify"),
    RESTORE("/restore"),
    CHANGE("/password/change");

    private final String url;
}
