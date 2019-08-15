encodeBytes = Base64.decode(encrypted_string_above,Base64.DEFAULT);
try
        {
            KeyFactory factory = KeyFactory.getInstance("ECDSA", "SC");

            String state;
            state = Environment.getExternalStorageState();
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath()+"/disk");
            privateKeyFromFile = generatePrivateKey(factory,dir+"/private.pem");

            Cipher c = Cipher.getInstance("ECIES","SC");
            c.init(Cipher.DECRYPT_MODE,privateKeyFromFile);
            decodeBytes = c.doFinal(encodeBytes);
            String deCrypt = new String(decodeBytes,"UTF-8");


            txtHiden.setText(deCrypt);
            Toast.makeText(activity, deCrypt, Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
