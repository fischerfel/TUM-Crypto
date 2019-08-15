public void dec(File b) throws FileNotFoundException
{
    try {
        c1 = new Scanner(b).useDelimiter("\\Z").next();
        byte[] by=c1.getBytes();

        String key = "Bar12345Sar12345"; // 128 bit key
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        String decrypted = new String(cipher.doFinal(by));

        str2 = new String(decrypted);
        System.out.println(str2);
        System.out.println("3");

        textField_2.setText(str2);

        empty(b);

        PrintWriter writer = new PrintWriter(b);
        writer.println(str2);                       
        writer.close();

    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }


}
