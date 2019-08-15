 public void enc(File a) throws FileNotFoundException {


                try {
                    c = new Scanner(a).useDelimiter("\\Z").next();

                    String key = "Bar12345Sar12346"; // 128 bit key
                    Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
                    Cipher cipher = Cipher.getInstance("AES");

                    cipher.init(Cipher.ENCRYPT_MODE, aesKey);
                    byte[] encrypted = cipher.doFinal(c.getBytes());

                    str1 = new String(encrypted);
                    textField_1.setText(str1);

                    empty(a); \\ To clear the text file

                    PrintWriter writer = new PrintWriter(a);
                    writer.println(str1);                       
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
