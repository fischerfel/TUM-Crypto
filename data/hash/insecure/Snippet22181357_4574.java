public class TripleDes {

    public static String EncryptTripleDES(String Cadena, String Llave)
    {
        String cryptedString = "";
        try{
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
            Cipher desEdeCipher = Cipher.getInstance("DESede/ECB/NoPadding");
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");

            byte[] arrayOfByte1 = Llave.getBytes();
            byte[] arrayOfByte3 = new byte[24];

            localMessageDigest.update(arrayOfByte1);
            byte[] result = localMessageDigest.digest();

            for (int i=0; i<999; i++){
               localMessageDigest.update(result);
               result = localMessageDigest.digest();
            }

            byte[] arrayOfByte4 = result;

            for (int n=0; n<16; n++)
               arrayOfByte3[n] = arrayOfByte4[n];

            for (int n=0; n<8; n++) 
               arrayOfByte3[(16 + n)] = arrayOfByte4[n];

            DESedeKeySpec localDESedeKeySpec = new DESedeKeySpec(arrayOfByte3, 0);
            SecretKey desEdeKey = secretKeyFactory.generateSecret(localDESedeKeySpec);
            desEdeCipher.init(1, desEdeKey);

            byte[] arrayOfByte = new byte[Cadena.length() % 8 > 0 ? Cadena.length() + 8 - Cadena.length() % 8 : Cadena.length()];
            System.arraycopy(Cadena.getBytes(), 0, arrayOfByte, 0, Cadena.length());

            //Resultado en base 64:
            String b_64 = encryptaBase64(desEdeCipher.doFinal(arrayOfByte));

            //Convertir resultado a cadena en hexadecimal
            cryptedString = StringToHexString(b_64);

        }catch(Exception e){
          e.printStackTrace();
        }
        return cryptedString;
    }

    public static String StringToHexString(String s) {
        StringBuffer stringbuffer = new StringBuffer(s.length() * 2);
        for (int i = 0; i < s.length(); i++) {
            int j = s.charAt(i) & 0xff;
            if (j < 16)
                stringbuffer.append('0');
            stringbuffer.append(Integer.toHexString(j));
        }
        return stringbuffer.toString().toUpperCase();
    }

    public static String encryptaBase64(byte[] paramArrayOfByte) throws Exception{
        int i = paramArrayOfByte.length;
        int j = (i * 4 + 2) / 3;
        int k = (i + 2) / 3 * 4;
        byte[] result = new byte[k];
        char[] map1 = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9','+','/'};
        int m = 0;
        for (int n = 0; m < i; n++)
        {
            int i1 = paramArrayOfByte[(m++)] & 0xFF;
            int i2 = m < i ? paramArrayOfByte[(m++)] & 0xFF : 0;
            int i3 = m < i ? paramArrayOfByte[(m++)] & 0xFF : 0;
            int i4 = i1 >>> 2;
            int i5 = (i1 & 0x3) << 4 | i2 >>> 4;
            int i6 = (i2 & 0xF) << 2 | i3 >>> 6;
            int i7 = i3 & 0x3F;
            result[(n++)] = ((byte)map1[i4]);
            result[(n++)] = ((byte)map1[i5]);
            result[n] = ((byte)(n < j ? map1[i6] : 61)); n++;
            result[n] = ((byte)(n < j ? map1[i7] : 61));
        }
        return new String(result);
    }

    public static String DecryptTripleDES(String Cadena, String Llave)
    {
        String decryptedString = "";
        try
        {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
            Cipher desEdeCipher = Cipher.getInstance("DESede/ECB/NoPadding");
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");

            byte[] arrayOfByte1 = Llave.getBytes();
            byte[] arrayOfByte3 = new byte[24];

            localMessageDigest.update(arrayOfByte1);
            byte[] result = localMessageDigest.digest();

            for (int i=0; i<999; i++){
               localMessageDigest.update(result);
               result = localMessageDigest.digest();
            }
            byte[] arrayOfByte4 = result;

            for (int n=0; n<16; n++)
               arrayOfByte3[n] = arrayOfByte4[n];

            for (int n=0; n<8; n++) 
               arrayOfByte3[(16 + n)] = arrayOfByte4[n];

            DESedeKeySpec localDESedeKeySpec = new DESedeKeySpec(arrayOfByte3, 0);
            SecretKey desEdeKey = secretKeyFactory.generateSecret(localDESedeKeySpec);
            desEdeCipher.init(2, desEdeKey);

            //Convertir cadena hexadecimal a String
            String hexString = "";
            for(int i = 0; i < Cadena.length()/2; i++){
                int k = Integer.parseInt(Cadena.substring(i*2, (i*2)+2), 16);
                hexString += (char)k;
            }

            result = desEdeCipher.doFinal(decrypt(hexString.getBytes()));
            decryptedString = new String(result).trim();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return decryptedString;
    }

    public static byte[] decrypt(byte[] paramArrayOfByte) throws Exception {
        byte[] map2 = new byte[256];
        char[] map1 = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9','+','/'};
        for (int i = 0; i < map2.length; i++) map2[i] = -1;
        for (int i = 0; i < 64; i++) map2[map1[i]] = ((byte)i);

        int i = paramArrayOfByte.length;
        if (i % 4 != 0) throw new IllegalArgumentException("Length of Base64 encoded input string is not a multiple of 4.");
        while ((i > 0) && (paramArrayOfByte[(i - 1)] == 61)) i--;
        int j = i * 3 / 4;
        byte[] result = new byte[j];
        int k = 0;
        int m = 0;
        while (k < i)
        {
            int n = paramArrayOfByte[(k++)];
            int i1 = paramArrayOfByte[(k++)];
            int i2 = k < i ? paramArrayOfByte[(k++)] : 65;
            int i3 = k < i ? paramArrayOfByte[(k++)] : 65;
            if ((n > 127) || (i1 > 127) || (i2 > 127) || (i3 > 127))
                throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
            int i4 = map2[n];
            int i5 = map2[i1];
            int i6 = map2[i2];
            int i7 = map2[i3];
            if ((i4 < 0) || (i5 < 0) || (i6 < 0) || (i7 < 0))
                throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
            int i8 = i4 << 2 | i5 >>> 4;
            int i9 = (i5 & 0xF) << 4 | i6 >>> 2;
            int i10 = (i6 & 0x3) << 6 | i7;
            result[(m++)] = ((byte)i8);
            if (m < j) result[(m++)] = ((byte)i9);
            if (m < j) result[(m++)] = ((byte)i10);
        }
        return result;
    }
}
