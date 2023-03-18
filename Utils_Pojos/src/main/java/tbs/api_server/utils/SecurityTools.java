package tbs.api_server.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class SecurityTools
{
    public static final String Encrypt_Method = "SHA-512";


    public static String bytesToHex(byte[] bytes) {
        return new BigInteger(1, bytes).toString(16);
    }
    public static byte[] Encrypt_byte(String text) throws Exception
    {
        MessageDigest mdg = MessageDigest.getInstance(Encrypt_Method);
        return mdg.digest(text.getBytes(StandardCharsets.UTF_8));
    }
    public static String Encrypt_str(String text) throws Exception
    {
        return bytesToHex(Encrypt_byte(text));
    }
}
