package io.github.uptalent.account.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthorUpdate {
    private String name;
    private String avatar;
}
