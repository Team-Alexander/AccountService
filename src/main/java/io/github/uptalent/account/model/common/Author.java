package io.github.uptalent.account.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Author {
    private Long id;
    private String name;
    private String avatar;
}
