package com.wrx.quickeats.util;

import android.util.Log;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by mobulous55 on 23/1/18.
 */

public class Generate256Hash
{

    private static final String HASH_ALGORITHM = "HmacSHA256";
    public static String hashMac(String MySecret, String MyDataString)
        throws SignatureException {

    try {
        Key sk = new SecretKeySpec(MySecret.getBytes(), HASH_ALGORITHM);
        Mac mac = Mac.getInstance(sk.getAlgorithm());
        mac.init(sk);
        final byte[] hmac = mac.doFinal(MyDataString.getBytes());
        String generatedHash=toHexString(hmac);
        Log.i("Generate256Hash", generatedHash);
        return generatedHash;
    } catch (NoSuchAlgorithmException e1) {
        // throw an exception or pick a different encryption method
        throw new SignatureException(
                "error building signature, no such algorithm in device "
                        + HASH_ALGORITHM);
    } catch (InvalidKeyException e) {
        throw new SignatureException(
                "error building signature, invalid key " + HASH_ALGORITHM);
    }
}

    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return sb.toString();
    }
}
