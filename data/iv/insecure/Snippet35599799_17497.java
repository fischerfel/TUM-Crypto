public class DES {

private static final int POLYNOMIAL   = 0x8408;
private static final int PRESET_VALUE = 0x6363;

public static byte[] gen_sessionKey(byte[] b) {

    byte[] key = new byte[] { (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
            (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
            (byte) 0x0 };
    byte[] response = decrypt(key, b);
    byte[] rndB = response;
    byte[] rndA = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
    byte[] rndAB = new byte[16];
    System.arraycopy(rndA, 0, rndAB, 0, 8);
    rndB = leftShift(rndB);
    rndB = xorBytes(rndA, rndB);
    rndB = decrypt(key, rndB);
    System.arraycopy(rndB, 0, rndAB, 8, 8);
    return rndAB;
}
public static byte[] gen_piccKey(byte[] key) {

    byte[] rndA = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
    byte[] rndB = new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, (byte) 0x88, (byte) 0x99, (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD, (byte) 0xEE, (byte) 0xFF, (byte) 0xFF };
    byte[] rndAB = new byte[24];
    rndA = xorBytes(rndA, rndB);
    Log.v("xor length", String.valueOf(rndB.length));
    byte[] rndABC = iso14443a_crc(rndA);
    byte[] rndABB = iso14443a_crc(rndB);
    Log.v("rnd length", ByteArrayToHexString(rndABC)+"\n"+ByteArrayToHexString(rndABB));
    System.arraycopy(rndA, 0, rndAB, 0, 16);
    System.arraycopy(rndABC, 0, rndAB, 16, 2);
    System.arraycopy(rndABB, 0, rndAB, 18, 2);
    byte[] rndABCD = new byte[] { 0x00, 0x00, 0x00, 0x00}; 
    System.arraycopy(rndABCD, 0, rndAB, 20, 4);
    Log.v("final", ByteArrayToHexString(rndAB));
    rndAB = decrypt(key , rndAB);
    return rndAB;
}

private static byte[] xorBytes(byte[] rndA, byte[] rndB) {
    // TODO Auto-generated method stub
    byte[] b = new byte[rndB.length];
    for (int i = 0; i < rndB.length; i++) {
        b[i] = (byte) (rndA[i] ^ rndB[i]);
    }
    return b;
}


public static byte[] leftShift(byte[] data) {
    // TODO Auto-generated method stub
    byte[] temp = new byte[data.length];
    temp[data.length - 1] = data[0];
    for (int i = 1; i < data.length; i++) {
        temp[i - 1] = data[i];
    }
    return temp;
}

public static byte[] decrypt(byte[] key, byte[] enciphered_data) {

    try {
        byte[] iv = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        SecretKey s = new SecretKeySpec(key, "DESede");
        Cipher cipher;
        cipher = Cipher.getInstance("DESede/CBC/NoPadding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, s, ivParameterSpec);
        byte[] deciphered_data = cipher.doFinal(enciphered_data);
        return deciphered_data;
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchProviderException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return null;
}

 static byte[] iso14443a_crc(byte[] Data)   // DESFireSAM crc16 do not invert the result
    {
        int  bt;
        int wCrc = 0x6363;
        int j = 0;
        int t8 = 0;
        int t9 = 0;
        int tA = 0;
        int Len = Data.length;
        final int maskB = 0x0000000000000000FF;
        final int maskW = 0x00000000000000FFFF;


        do
        {
            bt = Data[j++]              & maskB;
            bt =  (bt^(wCrc & 0x00FF))  & maskB;
            bt =  (bt^(bt<<4))          & maskB;


            t8 = (bt << 8)          & maskW;
            t9 = (bt<<3)            & maskW;
            tA = (bt>>4)            & maskW;
            wCrc = (wCrc >> 8)^(t8^t9^tA)  & maskW;
        }
        while (j < Len);


        byte[] bb = new byte[2];
        bb[0] = (byte) (wCrc          & maskB);
        bb[1] = (byte) ((wCrc >>8)    & maskB);
        return bb;
}
 private static String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }
