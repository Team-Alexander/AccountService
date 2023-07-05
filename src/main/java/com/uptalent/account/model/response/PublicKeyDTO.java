package com.uptalent.account.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicKeyDTO {
    private String algorithm;
    private String format;
    private byte[] encoded;
}