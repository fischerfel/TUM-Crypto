public String encrypt(SecretKey key, String stringIn){

    String outString = "";      

    if (stringIn.isEmpty() || stringIn.toUpperCase().equals("NULL")){
        return "";
    }


    try {   

        if (key == null)
            key = this.key;

        Cipher ecipher = Cipher.getInstance("DESede");

        ecipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] bytes = stringIn.getBytes("UTF8");

        byte[] encVal = ecipher.doFinal(bytes);

        outString = new sun.misc.BASE64Encoder().encode(encVal);

    } catch (UnsupportedEncodingException e) {

        e.printStackTrace();

    } catch (NoSuchAlgorithmException e) {

        e.printStackTrace();

    } catch (NoSuchPaddingException e) {

        e.printStackTrace();

    } catch (InvalidKeyException e) {

        e.printStackTrace();

    } catch (IOException e) {

        e.printStackTrace();

    } finally {

        return outString;
    }

}
