package com.commonsware.android.syssvc.alarm;

import java.security.SecureRandom;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class SimpleCrypto
{

    public SimpleCrypto()
    {
    }

    private static void appendHex(StringBuffer stringbuffer, byte byte0)
    {
        stringbuffer.append("0123456789ABCDEF".charAt(0xf & byte0 >> 4)).append("0123456789ABCDEF".charAt(byte0 & 0xf));
    }

    public static String decrypt(String s, String s1)
        throws Exception
    {
        return new String(decrypt(getRawKey(s.getBytes()), toByte(s1)));
    }

    private static byte[] decrypt(byte abyte0[], byte abyte1[])
        throws Exception
    {
        SecretKeySpec secretkeyspec = new SecretKeySpec(abyte0, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(2, secretkeyspec);
        return cipher.doFinal(abyte1);
    }

    public static String encrypt(String s, String s1)
        throws Exception
    {
        return toHex(encrypt(getRawKey(s.getBytes()), s1.getBytes()));
    }

    private static byte[] encrypt(byte abyte0[], byte abyte1[])
        throws Exception
    {
        SecretKeySpec secretkeyspec = new SecretKeySpec(abyte0, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(1, secretkeyspec);
        return cipher.doFinal(abyte1);
    }

    public static String fromHex(String s)
    {
        return new String(toByte(s));
    }

    private static byte[] getRawKey(byte abyte0[])
        throws Exception
    {
        KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
        SecureRandom securerandom = SecureRandom.getInstance("SHA1PRNG","Crypto");
        securerandom.setSeed(abyte0);
        keygenerator.init(128, securerandom);
        return keygenerator.generateKey().getEncoded();
    }

    public static byte[] toByte(String s)
    {
        int i = s.length() / 2;
        byte abyte0[] = new byte[i];
        int j = 0;
        do
        {
            if(j >= i)
                return abyte0;
            abyte0[j] = Integer.valueOf(s.substring(j * 2, 2 + j * 2), 16).byteValue();
            j++;
        } while(true);
    }

    public static String toHex(String s)
    {
        return toHex(s.getBytes());
    }

    public static String toHex(byte abyte0[])
    {
        if(abyte0 == null)
            return "";
        StringBuffer stringbuffer = new StringBuffer(2 * abyte0.length);
        int i = 0;
        do
        {
            if(i >= abyte0.length)
                return stringbuffer.toString();
            appendHex(stringbuffer, abyte0[i]);
            i++;
        } while(true);
    }

    private static final String HEX = "0123456789ABCDEF";
}
