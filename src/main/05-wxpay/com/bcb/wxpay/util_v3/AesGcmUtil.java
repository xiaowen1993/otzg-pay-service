package com.bcb.wxpay.util_v3;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AesGcmUtil {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int NONCE_LENGTH_BYTE = 12;
    private static final String AES_KEY = ""; // APIv3密钥
    private static final String TRANSFORMATION_PKCS1Padding = "RSA/ECB/PKCS1Padding";

    public static String aesgcmDecrypt(String aad, String iv, String cipherText) throws Exception {
        final Cipher cipher = Cipher.getInstance(ALGORITHM, "SunJCE");
        SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        cipher.updateAAD(aad.getBytes());
        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
    }

    public static void main(String[] args) {
        final String associatedData = ""; // encrypt_certificate.associated_data
        final String nonce = ""; // encrypt_certificate.nonce
        final String cipherText = ""; // encrypt_certificate.ciphertext
        try {
            String wechatpayCert = aesgcmDecrypt(associatedData, nonce, cipherText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}