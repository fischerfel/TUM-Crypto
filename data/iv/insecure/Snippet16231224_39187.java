public class DecryptFinal {
private static Cipher dcipher;

private static byte[] iv = {
    (byte)0xB2, (byte)0x12, (byte)0xD5, (byte)0xB2,
    (byte)0x44, (byte)0x21, (byte)0xC3, (byte)0xC3
    };


public static void main(String[] args){

    try {
        String s = "123456789123456789111234";
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

        SecretKeyFactory keyfactory=SecretKeyFactory.getInstance("DESede");
        byte[] encodedkey=s.getBytes();
        System.out.println();
         SecretKey key = keyfactory.generateSecret(new DESedeKeySpec(encodedkey));
         System.out.println(new DESedeKeySpec(encodedkey));
        SecretKeySpec(encodedKey,0,encodedKey.length,"DESede" );

        dcipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        FileInputStream fs =new FileInputStream("E:\\Test1\\Test1\\Encrypted Files\\Wedding bells.akr");
        FileOutputStream os= new FileOutputStream("E:\\Test1\\Test1\\Encrypted Files\\Encrypted Files\\E-pub Totorials");
        byte[] buf = new byte[1024];// bytes read from stream will be decrypted
        CipherInputStream cis = new CipherInputStream(fs, dcipher);// read in the decrypted bytes and write the clear text to out
        int numRead = 0;
        while ((numRead = cis.read(buf)) >= 0) {
            os.write(buf, 0, numRead);
        }
        cis.close();// close all streams
        fs.close();
        os.close();

    }
    catch(FileNotFoundException e) {
        System.out.println("File Not Found:" + e.getMessage());
        return;
    } catch (NoSuchAlgorithmException e) {
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
    }
    catch (IOException e) {
        System.out.println("I/O Error:" + e.getMessage());
    }
    catch (InvalidKeySpecException e) {
        // TODO: handle exception
        e.printStackTrace();
    }
