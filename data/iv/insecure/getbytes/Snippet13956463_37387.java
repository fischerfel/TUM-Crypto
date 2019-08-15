//Compress, Encrypt, Decrypt, Decompress variables 
public final static String Secretkey = "###hidden######";
public final static String VectorInitializationKey = "###hidden###";
public final static String transformation = "AES/CFB8/PKCS5Padding";  

public static byte[] encryptandCompress(byte[] input)
{
    ByteArrayOutputStream ms = new ByteArrayOutputStream();
    GZIPOutputStream gos = null; // Added
    CipherOutputStream cos= null; // Added

    SecretKeySpec skeySpec = new SecretKeySpec(Secretkey.getBytes(), "AES");
    Cipher cipher;

    try {
        cipher = Cipher.getInstance(transformation);
        IvParameterSpec iv = new IvParameterSpec(VectorInitializationKey.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        cos = new CipherOutputStream(ms, cipher ); 
        gos= new GZIPOutputStream (cos);
        gos.write(input);
        gos.close(); // Must be called before we return bytes from ms
        cos.close();
        byte[] toReturn = ms.toByteArray();  
        Log.d("FileWriter", "Encrypted and compressed " + input.length + " bytes to " + toReturn.length + " bytes");
        Log.d("FileWriter", "Encrypted and compressed " + toReturn.length/16.0 + " blocks written");

        return toReturn;        

    } catch (Exception e) {
        Log.e("FileWriter", "Compression failed: " + e.getMessage());
        return null;
    }

    finally 
    {
        try 
        {
            if (gos != null) 
            {
                gos.close();
            }
            if (cos != null) 
            {
                cos.close();
            }
            if (ms != null) 
            {
                ms.close();
            }
        } 
        catch (IOException e) {

            e.printStackTrace();
        }

        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}
