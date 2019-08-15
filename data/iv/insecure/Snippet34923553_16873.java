public byte[] decryptData(byte input[], String password) throws Exception {
    byte[] result = null;
    //Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
    //byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0 };
    byte[] iv = { -128, -128, -128, -128, -128, -128, -128, -128 };
    IvParameterSpec ivspec = new IvParameterSpec(iv);
    cipher.init(Cipher.DECRYPT_MODE, generateSecretKey(passwordToKey(password)), ivspec);
    result = cipher.doFinal(input);
    return result;
}

protected SecretKey generateSecretKey(byte[] key) throws Exception {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
    KeySpec keySpec = new DESKeySpec(key);
    SecretKey secretKey = factory.generateSecret(keySpec);
    return secretKey;
}

public byte[] passwordToKey(String password) throws Exception
{
    if (password == null)
        throw new IllegalArgumentException("password");
    if (password == "")
        throw new IllegalArgumentException("password");

    byte[] key = new byte[8];

    for (int i = 0; i < password.length(); i++)
    {
        int c = (int)password.charAt(i);
        if ((i % 16) < 8)
        {
            key[i % 8] ^= (byte)(c << 1);
        }
        else
        {
            // reverse bits e.g. 11010010 -> 01001011
            c = (((c << 4) & 0xf0) | ((c >> 4) & 0x0f));
            c = (((c << 2) & 0xcc) | ((c >> 2) & 0x33));
            c = (((c << 1) & 0xaa) | ((c >> 1) & 0x55));
            key[7 - (i % 8)] ^= (byte)c;
        }
    }

    addOddParity(key);

    byte[] target = new byte[8];
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
    outputStream.write(password.getBytes("US-ASCII"));
    outputStream.write(new byte[8]);
    byte[] temp = outputStream.toByteArray();
    outputStream = new ByteArrayOutputStream( );
    for (int i = 0; i < (password.length() + (8 - (password.length() % 8)) % 8); ++i) {
        outputStream.write(temp[i]);
    }
    byte[] passwordBuffer = outputStream.toByteArray(); 

    Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
    //byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0 };
    byte[] iv = { -128, -128, -128, -128, -128, -128, -128, -128 };
    IvParameterSpec ivspec = new IvParameterSpec(iv);
    cipher.init(Cipher.ENCRYPT_MODE, generateSecretKey(key), ivspec);
    for (int x = 0; x < passwordBuffer.length / 8; ++x)
    {
        cipher.update(passwordBuffer, 8 * x, 8, target, 0);
    }

    addOddParity(target);

    return target;
}

private void addOddParity(byte[] buffer)
{
    for (int i = 0; i < buffer.length; ++i)
    {
        buffer[i] = _oddParityTable[buffer[i] & 0xFF];
    }
}

private static byte[] _oddParityTable = {
   -127,-127,-126,-126,-124,-124,-121,-121,-120,-120,-117,-117,-115,-115,-114,-114,
   -112,-112,-109,-109,-107,-107,-106,-106,-103,-103,-102,-102,-100,-100, -97, -97,
    -96, -96, -93, -93, -91, -91, -90, -90, -87, -87, -86, -86, -84, -84, -81, -81,
    -79, -79, -78, -78, -76, -76, -73, -73, -72, -72, -69, -69, -67, -67, -66, -66,
    -64, -64, -61, -61, -59, -59, -58, -58, -55, -55, -54, -54, -52, -52, -49, -49,
    -47, -47, -46, -46, -44, -44, -41, -41, -40, -40, -37, -37, -35, -35, -34, -34,
    -31, -31, -30, -30, -28, -28, -25, -25, -24, -24, -21, -21, -19, -19, -18, -18,
    -16, -16, -13, -13, -11, -11, -10, -10,  -7,  -7,  -6,  -6,  -4,  -4,  -1,  -1,
      0,   0,   3,   3,   5,   5,   6,   6,   9,   9,  10,  10,  12,  12,  15,  15,
     17,  17,  18,  18,  20,  20,  23,  23,  24,  24,  27,  27,  29,  29,  30,  30,
     33,  33,  34,  34,  36,  36,  39,  39,  40,  40,  43,  43,  45,  45,  46,  46,
     48,  48,  51,  51,  53,  53,  54,  54,  57,  57,  58,  58,  60,  60,  63,  63,
     65,  65,  66,  66,  68,  68,  71,  71,  72,  72,  75,  75,  77,  77,  78,  78,
     80,  80,  83,  83,  85,  85,  86,  86,  89,  89,  90,  90,  92,  92,  95,  95,
     96,  96,  99,  99, 101, 101, 102, 102, 105, 105, 106, 106, 108, 108, 111, 111,
    113, 113, 114, 114, 116, 116, 119, 119, 120, 120, 123, 123, 125, 125, 126, 126
};
