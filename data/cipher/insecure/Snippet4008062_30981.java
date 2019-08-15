public class NewClass1{

    private Key key;

    private void generateKey() throws NoSuchAlgorithmException{
        KeyGenerator generator;
        generator = KeyGenerator.getInstance("AES");
        generator.init(new SecureRandom());
        key = generator.generateKey();
    }

    private String decrypt(String encrypted) throws InvalidKeyException,
        NoSuchAlgorithmException,
        NoSuchPaddingException,
        IllegalBlockSizeException,
        BadPaddingException,
        IOException{

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] raw = decoder.decodeBuffer(encrypted);
        byte[] stringBytes = cipher.doFinal(raw);
        // converts the decoded message to a String
        String clear = new String(stringBytes);
        return clear;
    }

    public NewClass1(String encrypted){
        try{
            System.out.println("encrypted message: " + encrypted);
            generateKey();
            String decrypted = decrypt(encrypted);
            System.out.println("decrypted message: " + decrypted);
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch(NoSuchPaddingException e){
            e.printStackTrace();
        } catch(InvalidKeyException e){
            e.printStackTrace();
        } catch(UnsupportedEncodingException e){
            e.printStackTrace();
        } catch(IllegalBlockSizeException e){
            e.printStackTrace();
        } catch(BadPaddingException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new NewClass1("vbfhdhhhjhtrrrrrrrrrrrrrrjrdfes");
    }
}
