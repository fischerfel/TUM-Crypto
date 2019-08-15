try
    {
        String val[];
        String text="";
        if(get.contains(","))
        {
            val=get.split(",");
            text=val[0];
        }
        else
        {
            text = get;
        }
        pubtxt2.setText(""+text.length());
        String key=cipkey;
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte sam[]=hextobin(text);
        String decrypted = new String(cipher.doFinal(sam));
        output.setText(decrypted);
        chk=decrypted;
        pubtxt3.setText(""+chk.length());
        a2++;
    }
    catch(Exception e)
    {
        System.err.println(e);
    }
