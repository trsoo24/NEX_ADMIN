package com.example.admin.common.service.gdcb.util;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.Security;

@Slf4j
public class PGPFile {
    /*
     * PGP 파일을 복호화한다.
     * @param strCsvBpgFile (PGP 원문파일)
     * @param strCsvFile (복호화 시에 생성 파일)
     * @param strSecretKey (PGP 개인키)
     * @param strPublicKey (PGP 공개키)
     * @param keyPw (PGP 비밀번호)
     * @throws Exception
     */
    public static void decryptAndVerify(String strCsvBpgFile, String strCsvFile, String strSecretKey, String strPublicKey, String keyPw) throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        FileInputStream inFile = null;
        FileOutputStream outFile = null;
        FileInputStream privKeyIn = null;
        FileInputStream googleKeyStream = null;

        try {
            // Private Key
            inFile = new FileInputStream(strCsvBpgFile);
            outFile = new FileOutputStream(strCsvFile);
            privKeyIn = new FileInputStream(strSecretKey);

            googleKeyStream = new FileInputStream(strPublicKey);
            PGPPublicKey publicKey = PGPUtils.readPublicKey(googleKeyStream);
            PGPUtils.decryptAndVerify(inFile, outFile, privKeyIn, publicKey, keyPw.toCharArray());

        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);

//			ValidationUtil.validationCheck(SchedulerConstants.DECODE_FILE_NOT_FOUND_CD);

            throw e;
        } catch (IOException e) {
            log.error(e.getMessage(), e);

//			ValidationUtil.validationCheck(SchedulerConstants.DECODE_ERROR_CD);

            throw e;

        } catch (PGPException e) {
            log.error(e.getMessage(), e);

            if ("org.bouncycastle.openpgp.PGPPublicKeyRing found where PGPSecretKeyRing expected".equals(e.getMessage())) {
//				ValidationUtil.validationCheck(SchedulerConstants.DECODE_KEY_NOT_FOUND_CD);
            } else if ("unknown PGP public key algorithm encountered".equals(e.getMessage())) {
//				ValidationUtil.validationCheck(SchedulerConstants.DECODE_UNKNOWN_SIGN_KEY_CD);
            } else {
//				ValidationUtil.validationCheck(SchedulerConstants.DECODE_ERROR_CD);
            }

            throw e;
        } catch (NoSuchProviderException e) {
            log.error(e.getMessage(), e);

//			ValidationUtil.validationCheck(SchedulerConstants.DECODE_ERROR_CD);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);

//			ValidationUtil.validationCheck(SchedulerConstants.DECODE_KEY_NOT_FOUND_CD);
            throw e;
        } finally {
            if (privKeyIn != null) {
                privKeyIn.close();
            }
            if (inFile != null) {
                inFile.close();
            }
            if (outFile != null) {
                outFile.close();
            }
            if (googleKeyStream != null) {
                googleKeyStream.close();
            }
        }
    }

    /**
     * 평문을 PGP 암호화한다.
     * @param strSecretKey (PGP 개인키)
     * @param strPublicKey (PGP 공개키)
     * @param password (PGP 비밀번호)
     * @param strCsvPpgFile (암호화 생성 파일)
     * @param strCsvFile (평문 파일)
     * @throws Exception
     */
    public static void signEncryptFile(String strSecretKey, String strPublicKey, String password, String strCsvPpgFile, String strCsvFile) throws Exception {

        FileOutputStream out = null;
        FileInputStream googleKeyStream = null;
        FileInputStream secretKeyStream = null;

        try {

            out = new FileOutputStream(strCsvPpgFile);

            googleKeyStream = new FileInputStream(strPublicKey);
            PGPPublicKey publicKey = PGPUtils.readPublicKey(googleKeyStream);

            secretKeyStream = new FileInputStream(strSecretKey);
            PGPSecretKey secretKey = PGPUtils.readSecretKey(secretKeyStream);

            boolean armor = true;
            boolean withIntegrityCheck = true;

            PGPUtils.signEncryptFile(out, strCsvFile, publicKey, secretKey, password, armor, withIntegrityCheck);

        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);

//			ValidationUtil.validationCheck(SchedulerConstants.ENCODE_FILE_NOT_FOUND_CD);
            throw e;
        } catch (IOException e) {
            log.error(e.getMessage(), e);

//			ValidationUtil.validationCheck(SchedulerConstants.ENCODE_ERROR_CD);
            throw e;
        } catch (PGPException e) {
            log.error(e.getMessage(), e);

            if ("org.bouncycastle.openpgp.PGPSecretKeyRing found where PGPPublicKeyRing expected".equals(e.getMessage())) {
//				ValidationUtil.validationCheck(SchedulerConstants.ENCODE_KEY_NOT_FOUND_CD);
            } else {
//				ValidationUtil.validationCheck(SchedulerConstants.ENCODE_ERROR_CD);
            }

            throw e;
        } catch (NoSuchProviderException e) {
            log.error(e.getMessage(), e);

//			ValidationUtil.validationCheck(SchedulerConstants.ENCODE_ERROR_CD);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);

//			ValidationUtil.validationCheck(SchedulerConstants.ENCODE_KEY_NOT_FOUND_CD);
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
            if (googleKeyStream != null) {
                googleKeyStream.close();
            }
            if (secretKeyStream != null) {
                secretKeyStream.close();
            }
        }
    }
}
