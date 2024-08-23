package com.example.admin.controller;

import com.example.admin.service.crypto.AES256CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/crypto")
public class CryptoController {
    private final AES256CryptoService aes256CryptoService;
    private static final String CRYPTO_KEY = "DCB";

    @GetMapping("/encrypt")
    public String encrypt(@RequestParam("text") String text) {
        return aes256CryptoService.encryptSingleData(text, CRYPTO_KEY);
    }

    @GetMapping("/decrypt")
    public String decrypt(@RequestParam("text") String text) {
        return aes256CryptoService.decryptSingleData(text, CRYPTO_KEY);
    }
}
