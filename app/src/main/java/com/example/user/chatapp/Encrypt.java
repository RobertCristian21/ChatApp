package com.example.user.chatapp;

import android.util.Base64;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt {

    public static String decrypts(String text, String password,String algorithm) throws Exception {
        SecretKeySpec key= generateKey(password,algorithm);
        Cipher c=Cipher.getInstance(algorithm);
        c.init(Cipher.DECRYPT_MODE,key);

        byte[] decryptedVal= Base64.decode(text,Base64.DEFAULT);

        byte [] devVal=c.doFinal(decryptedVal);

        return new String(devVal);
    }

    public static String encrypts(String text, String password,String algorithm) throws Exception {
        SecretKeySpec key= generateKey(password,algorithm);
        Cipher c=Cipher.getInstance(algorithm);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal=c.doFinal(text.getBytes());
        return Base64.encodeToString(encVal,Base64.DEFAULT);
    }

    private static SecretKeySpec generateKey(String password,String algorithm) throws Exception {
        final MessageDigest digest= MessageDigest.getInstance("SHA-256");
        byte[] bytes=password.getBytes("UTF-8");

        digest.update(bytes,0,bytes.length);
        byte[] key=digest.digest();
        return new SecretKeySpec(key,algorithm);
    }
}
