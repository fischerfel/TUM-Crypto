public class Cryptastic {
public final static String CHARSET = "US-ASCII";

public byte[] encrypt(String toEncrypt, byte[] keyBytes, boolean base64Encode)
{
    try
    {
        toEncrypt = serialize(toEncrypt);
        byte[] newEncrypt = toEncrypt.getBytes(CHARSET);
        BlockCipher blockCipher = new RijndaelEngine(256);
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[32];           
        secureRandom.nextBytes(iv);


        CipherParameters cipherParameters = new ParametersWithIV(new KeyParameter(keyBytes), iv);
        RijndaelEngine rijndael = new RijndaelEngine(256);
        SICBlockCipher ctrMode = new SICBlockCipher(rijndael);
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(ctrMode, new ZeroBytePadding());
        cipher.init(true, cipherParameters);

        int size = cipher.getOutputSize(newEncrypt.length);
        byte[] encrypted = new byte[size];
        System.out.println(displayBytes(encrypted, false));
        int outputLength = cipher.processBytes(newEncrypt, 0, newEncrypt.length, encrypted, 0);
        cipher.doFinal(encrypted, outputLength);
        if(base64Encode)
        {
            encrypted = Base64.encode(encrypted);
        }

        byte[] temp = new byte[encrypted.length + 32];
        for(int i = 0; i < iv.length; i++)
        {
            temp[i] = iv[i];
        }
        for(int i = iv.length; i < temp.length; i++)
        {
            temp[i] = encrypted[i - iv.length];
        }

        encrypted = temp;
        byte[] mac  = generateKey(encrypted, keyBytes, 1000, 32);

        temp = new byte[encrypted.length + mac.length];
        for(int i = 0; i < encrypted.length; i++)
        {
            temp[i] = encrypted[i];
        }
        for(int i = encrypted.length; i < temp.length; i++)
        {
            temp[i] = mac[i - encrypted.length];
        }

        return encrypted;

        //System.out.println("Original IV: " + displayBytes(iv, false));
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    return null;
}

public byte[] decrypt(String toDecrypt, byte[] keyBytes, boolean base64Encode)
{
    try
    {
        byte[] newDecrypt = hexToByte(toDecrypt);
        //toDecrypt = hexToString(toDecrypt);
        if(base64Encode)
        {
            newDecrypt = Base64.decode(newDecrypt);
            //toDecrypt = new String(Base64.decode(toDecrypt.getBytes(CHARSET)), CHARSET);
        }    
        byte[] iv = new byte[32];
        byte[] extractedMac = new byte[32];

        int offset = newDecrypt.length - 32;
        for(int i = 0; i < 32; i++)
        {
            iv[i] = newDecrypt[i];
            extractedMac[i] = newDecrypt[i + offset];
        }

        byte[] temp = new byte[offset - 32];
        for(int i = 0; i < temp.length; i++)
        {
            temp[i] = newDecrypt[i + 32];
        }

        newDecrypt = temp;

        byte[] combo = new byte[newDecrypt.length + iv.length];
        for(int i = 0; i < iv.length; i++)
        {
            combo[i] = iv[i]; 
        }

        for(int i = iv.length; i < combo.length; i++)
        {
            combo[i] = newDecrypt[i - iv.length];
        }

        byte[] mac = generateKey(new String(combo, CHARSET), new String(keyBytes, CHARSET), 1000, 32);
        boolean matches = new String(extractedMac).equals(new String(mac));

        if(!matches)
        {
            byte[] toReturn = new byte[0];
            return toReturn;
        }

        CipherParameters cipherParameters = new ParametersWithIV(new KeyParameter(keyBytes), iv);
        RijndaelEngine rijndael = new RijndaelEngine(256);
        SICBlockCipher ctrMode = new SICBlockCipher(rijndael);
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(ctrMode, new ZeroBytePadding());
        cipher.init(true, cipherParameters);

        int size = cipher.getOutputSize(newDecrypt.length);
        byte[] decrypted = new byte[size];

        System.out.println(displayBytes(newDecrypt, false));
        System.out.println(displayBytes(decrypted, false));
        int outputLength = cipher.processBytes(newDecrypt, 0, newDecrypt.length, decrypted, 0);
        cipher.doFinal(decrypted, outputLength);
        System.out.println(displayBytes(decrypted, false));

        temp = new byte[newDecrypt.length];
        for(int i = 0; i < temp.length; i++)
        {
            temp[i] = decrypted[i];
        }
        decrypted = temp;

        decrypted = unserialize(new String(decrypted, CHARSET)).getBytes(CHARSET);
        return decrypted;
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    return null;
}

public byte[] generateKey(String password, String salt, int blockIterations, int keyLength)
{
    try
    {
        SHA256Digest digest = new SHA256Digest();
        int hashLength = digest.getDigestSize();
        long keyBlocks = (long) Math.ceil( (double) keyLength / (double) hashLength);
        String derived_key = "";
        //System.out.println("Number of blocks: " + keyBlocks);
        for(long block = 1; block <= keyBlocks; block++)
        {
            byte[] initialHash = hash_hmac(salt + new String(pack(block)/*, CHARSET */), password);
            byte[] compareHash = initialHash;
            //System.out.println("Block: " + block);
            for(int i = 1; i < blockIterations; i++) 
                // XOR each iterate
                compareHash = hash_hmac(compareHash, password);
                for(int j = 0; j < initialHash.length; j++)
                {
                    initialHash[j] = (byte) (initialHash[j] ^ compareHash[j]);
                }
            }
            derived_key += new String(initialHash/*, CHARSET */);
        }
        return derived_key.substring(0, keyLength).getBytes(CHARSET);
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }

    return null;
}

public byte[] generateKey(byte[] password, byte[] salt, int blockIterations, int keyLength)
{
    try
    {
        SHA256Digest digest = new SHA256Digest();
        int hashLength = digest.getDigestSize();
        long keyBlocks = (long) Math.ceil( (double) keyLength / (double) hashLength);
        byte[] derived_key = null;
        //System.out.println("Number of blocks: " + keyBlocks);
        for(long block = 1; block <= keyBlocks; block++)
        {
            byte[] packed = pack(block);
            byte[] combined = new byte[salt.length + packed.length];
            for(int i = 0; i < salt.length; i++)
            {
                combined[i] = salt[i];
            }
            for(int i = salt.length; i < combined.length; i++)
            {
                combined[i] = packed[i - salt.length];
            }

            byte[] initialHash = hash_hmac(combined, password);
            byte[] compareHash = initialHash;
            //System.out.println("Block: " + block);
            for(int i = 1; i < blockIterations; i++) 
            {
                // XOR each iterate
                compareHash = hash_hmac(compareHash, password);
                for(int j = 0; j < initialHash.length; j++)
                {
                    initialHash[j] = (byte) (initialHash[j] ^ compareHash[j]);
                }
            }
            if(derived_key == null)
            {
                derived_key = initialHash;
            }
            else
            {
                byte[] temp = new byte[derived_key.length + initialHash.length];
                for(int i = 0; i < derived_key.length; i++)
                {
                    temp[i] = derived_key[i];
                }
                for(int i = derived_key.length; i < temp.length; i++)
                {
                    temp[i] = initialHash[i - derived_key.length];
                }
                derived_key = temp;
            }
        }
        byte[] toReturn = new byte[keyLength];
        for(int i = 0; i < toReturn.length; i++)
        {
            toReturn[i] = derived_key[i];
        }
        return toReturn;
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }

    return null;
}

public byte[] pack(long toPack)
{       
    String test = Long.toHexString(toPack);
    while(test.length() < 8)
    {
        test = "0" + test;
    }       
    byte[] toReturn = new byte[4];

    toReturn[0] = Byte.parseByte(test.substring(0, 2));
    toReturn[1] = Byte.parseByte(test.substring(2, 4));
    toReturn[2] = Byte.parseByte(test.substring(4, 6));
    toReturn[3] = Byte.parseByte(test.substring(6, 8));
    return toReturn;
}

public String hexToString(String hex)
{
    return new String(hexToByte(hex));
}

public byte[] hexToByte(String hex)
{
    if(hex.length() % 2 != 0)
    {
        hex = "0" + hex;
    }

    byte[] toReturn = new byte[hex.length() / 2];

    for(int i = 0; i < toReturn.length; i++)
    {
        toReturn[i] = Byte.parseByte(hex.substring(i * 2, (i + 1) * 2), 16);
    }
    //System.out.println(displayBytes(toReturn, false));
    return toReturn;
}

public byte[] hash_hmac(String data, String key)
{
    return hash_hmac("HmacSHA256", data, key);
}

public byte[] hash_hmac(String algorithm , String data , String key)
{
    try
    {
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(/*CHARSET*/), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secret);
        byte[] digest = mac.doFinal(data.getBytes(/*CHARSET*/));
        return digest;
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    return null;
}

public byte[] hash_hmac(byte[] data, String key)
{
    return hash_hmac("HmacSHA256", data, key);
}

public byte[] hash_hmac(byte[] data, byte[] key)
{
    return hash_hmac("HmacSHA256", data, key);
}

public byte[] hash_hmac(String algorithm , byte[] data , byte[] key)
{
    try
    {
        SecretKeySpec secret = new SecretKeySpec(key, algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secret);
        byte[] digest = mac.doFinal(data);
        return digest;
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    return null;
}

public byte[] hash_hmac(String algorithm , byte[] data , String key)
{
    try
    {
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(/*CHARSET*/), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secret);
        byte[] digest = mac.doFinal(data);
        return digest;
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    return null;
}


public String displayBytes(byte[] bytes, boolean rawDisplay)
{
    String toReturn = "";
    if(rawDisplay)
    {
            toReturn = new String(bytes/*, CHARSET */);
    }
    else
    {
        for (byte b : bytes) 
        {
            toReturn += String.format("%02x", b);
        }
    }
    return toReturn;
}

public String toHex(String toConvert) {
    if(toConvert.getBytes().length == 0)
    {
        return "";
    }
    return String.format("%04x", new BigInteger(toConvert.getBytes(/*YOUR_CHARSET?*/)));
}

/*
 * This creates a string representation that should line up with PHPs serialize.
 */
public String serialize(String toSerialize)
{
    return "s:" + toSerialize.length() + ":\"" + toSerialize + "\";";
}

public String unserialize(String toUnserialize)
{
    System.out.println(toUnserialize);
    int endIndex = toUnserialize.indexOf("\";");
    while (endIndex < toUnserialize.length() && endIndex < toUnserialize.indexOf("\";", endIndex + 1))
    {
        endIndex = toUnserialize.indexOf("\";", endIndex + 1);
    }
    return toUnserialize.substring(toUnserialize.indexOf(":\"") + 2, endIndex);
}
