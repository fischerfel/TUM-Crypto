    final EditText productid = (EditText) findViewById(R.id.editText1);
    Button encryptButton = (Button) findViewById(R.id.encrypt);
    final TextView value = (TextView) findViewById(R.id.value);
    encryptButton.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {

            String valid_productid = productid.getText().toString();
            ivspec = new IvParameterSpec(iv.getBytes());

            keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

            try {
                    cipher = Cipher.getInstance("AES/CBC/NoPadding");
                    //encrypt(valid_productid);

                    byte[] cipher = encrypt(valid_productid);

                    System.out.print("cipher:  ");
                    for (int i=0; i<cipher.length; i++)
                      System.out.print(new Integer(cipher[i])+" ");
                      System.out.println("");
                      System.out.println(cipher);

                      /*String decrypted = decrypt(cipher);
                      System.out.println("decrypt: " + decrypted);
                          value.setText(cipher);*/

            } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    });  
public byte[] encrypt(String text) throws Exception
{
        if(text == null || text.length() == 0)
                throw new Exception("Empty string");

        byte[] encrypted = null;

        try {
                cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

                encrypted = cipher.doFinal(padString(text).getBytes());
        } catch (Exception e)
        {                       
                throw new Exception("[encrypt] " + e.getMessage());
        }

        return encrypted;
}

public byte[] decrypt(String code) throws Exception
{
        if(code == null || code.length() == 0)
                throw new Exception("Empty string");

        byte[] decrypted = null;

        try {
                cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

                decrypted = cipher.doFinal(hexToBytes(code));
        } catch (Exception e)
        {
                throw new Exception("[decrypt] " + e.getMessage());
        }
        return decrypted;
}



public static String bytesToHex(byte[] data)
{
        if (data==null)
        {
                return null;
        }

        int len = data.length;
        String str = "";
        for (int i=0; i<len; i++) {
                if ((data[i]&0xFF)<16)
                        str = str + "0" + java.lang.Integer.toHexString(data[i]&0xFF);
                else
                        str = str + java.lang.Integer.toHexString(data[i]&0xFF);
        }
        return str;
}


public static byte[] hexToBytes(String str) {
        if (str==null) {
                return null;
        } else if (str.length() < 2) {
                return null;
        } else {
                int len = str.length() / 2;
                byte[] buffer = new byte[len];
                for (int i=0; i<len; i++) {
                        buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
                }
                return buffer;
        }
}



private static String padString(String source)
{
  char paddingChar = ' ';
  int size = 16;
  int x = source.length() % size;
  int padLength = size - x;

  for (int i = 0; i < padLength; i++)
  {
          source += paddingChar;
  }

  return source;
}
