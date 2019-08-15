public static void testCipher() throws Exception
{
    byte[] KEY =
            new byte[]{
                    (byte) 0x0C, (byte) 0x09, (byte) 0x03, (byte) 0x0E,
                    (byte) 0x05, (byte) 0x0A, (byte) 0x0D, (byte) 0x02,
                    (byte) 0x03, (byte) 0x0A, (byte) 0x09, (byte) 0x0B,
                    (byte) 0x06, (byte) 0x10, (byte) 0x04, (byte) 0x10
            };

    byte[] DATA =
            new byte[]{
                    (byte) 0x29, (byte) 0xDA, (byte) 0xC0, (byte) 0xC4,
                    (byte) 0xB8, (byte) 0x47, (byte) 0x13, (byte) 0xA2};

    byte[] newByte8 = new byte[8]; //Zeroes

    android.util.Log.d("TEST", "KEY : " + bin2hex(KEY));
    android.util.Log.d("TEST", "DATA: " + bin2hex(DATA));
    android.util.Log.d("TEST", "IVPS: " + bin2hex(newByte8));
    android.util.Log.d("TEST", "----");

    javax.crypto.Cipher cipher =
            javax.crypto.Cipher.getInstance("DESede/CBC/NoPadding");

    cipher.init(
            Cipher.DECRYPT_MODE,
            new javax.crypto.spec.SecretKeySpec(KEY, "DESede"),
            new javax.crypto.spec.IvParameterSpec(newByte8));

    byte[] result = cipher.doFinal(DATA);

    android.util.Log.d("TEST", "RSLT: " + bin2hex(result));
}

public static String bin2hex(byte[] data) {
    return String.format("%0" + (data.length * 2) + "X", new java.math.BigInteger(1, data));
}
