public class DES {
    public static byte[] doDecryptData(byte[] OriginalData,byte[]key , int sizeKey , byte[] iv , int sizeIV)
    {
    byte[] masterKeyBytes =new byte[sizeKey];
    masterKeyBytes = key;
        byte[] ivBytes = new byte[sizeIV];
     ivBytes = iv;
        byte[] encipheredData=new byte[sizeIV];

        try{

            DESKeySpec desKeySpec = new DESKeySpec(masterKeyBytes);  
            SecretKeyFactory desKeyFact = SecretKeyFactory.getInstance("DES");
            SecretKey s = desKeyFact.generateSecret(desKeySpec);
            Cipher aliceCipher = Cipher.getInstance("DES/CBC/NoPadding");
            aliceCipher.init(Cipher.DECRYPT_MODE, s, new IvParameterSpec(ivBytes));

            encipheredData= aliceCipher.doFinal(OriginalData);
            return encipheredData;
        }
        catch(Exception e)
            {
                Log.e("error", "111"+e.toString());
            }
        return null;
    }
