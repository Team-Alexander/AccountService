package com.uptalent.account.converter;


import com.uptalent.account.model.response.PublicKeyDTO;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class PublicKeyConverter {
    public static PublicKey convertToPublicKey(PublicKeyDTO publicKeyDTO) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(publicKeyDTO.getAlgorithm());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyDTO.getEncoded());
        return keyFactory.generatePublic(keySpec);
    }
}
