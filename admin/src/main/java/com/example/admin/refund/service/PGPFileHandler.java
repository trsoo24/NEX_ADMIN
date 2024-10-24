package com.example.admin.refund.service;

import com.example.admin.common.service.gdcb.util.PGPFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PGPFileHandler {
    @Value("${PGP.LG.PrivateKey}")
    private String lgPrivateKey;
    @Value("$PGP.Google.PublicKey}")
    private String googlePublicKey;
    @Value("${PGP.Password}")
    private String keyPw;

    public boolean decryptePGP(String pathPgpFile) {
        try {

            log.debug("PGP 복호화 : {}", pathPgpFile);

            // 복호화 대상 파일명 형식 확인 <- 파일명이 .csv.pgp 로 끝나는 파일만 복호화 대상으로 한다.
            if (".csv.pgp".equals(pathPgpFile.substring(pathPgpFile.length() - 8))) {
                String resultFileName = pathPgpFile.replace(".pgp", "");

                PGPFile.decryptAndVerify(pathPgpFile, resultFileName, lgPrivateKey, googlePublicKey, keyPw);

                return true;
            } else {
                log.warn("CSV & PGP 파일 형식이 아님: {}", pathPgpFile);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return false;
    }

    public boolean encryptePGP(String pathCsvFile) {
        //pgp파일

        try {
            log.debug("PGP 복호화 : {}", pathCsvFile);

            if (pathCsvFile.endsWith(".csv")) {

                String resultFileName = pathCsvFile + ".pgp";

                PGPFile.signEncryptFile(lgPrivateKey, googlePublicKey, keyPw, resultFileName, pathCsvFile);

                return true;
            } else {
                log.warn("CSV 파일 형식이 아님: {}", pathCsvFile);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return false;
    }
}