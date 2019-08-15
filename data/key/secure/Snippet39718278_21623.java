public class EncUtil {
    //String enc_key = "ea3401cec22dec24e9756a71904b8515";
        public String  encmsg(String msg,String enc_key)
            {
                try {
                    SecretKeySpec skeySpec = new SecretKeySpec(HexfromString(enc_key), "AES");
                    Cipher cipher = Cipher.getInstance("AES");
                    cipher.init(1, skeySpec);

                    byte encstr[] = cipher.doFinal(msg.getBytes());

                    String decmsg=""+HextoString(encstr);
//                    System.out.println(decmsg);

                    return ""+decmsg;

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
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                return "0";

        }
