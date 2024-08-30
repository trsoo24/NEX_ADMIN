package com.example.admin.service.crypto;

import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class AES256CryptoService {

    private static final byte[] FIXED_IV = "nexgrid18nexgrid".getBytes(StandardCharsets.UTF_8); // 16-byte IV

    public String encryptSingleData(String data, String cryptoKey) {
        SecretKeySpec secretKey = encryptKey(cryptoKey);
        Cipher cipher;
        byte[] encryptedBytes;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(FIXED_IV);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException |
                 IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }


    public String decryptSingleData(String encryptedData, String cryptoKey) {
        SecretKeySpec secretKey = encryptKey(cryptoKey);
        Cipher cipher;
        byte[] decryptedBytes;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(FIXED_IV);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException |
                 IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }


    // 입력된 문자열을 SHA-256로 변환하고 결과물인 32바이트를 AES 키로 암호화한다.
    private SecretKeySpec encryptKey(String rawKey) {
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] key = sha.digest(rawKey.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(key, "AES");
    }
}