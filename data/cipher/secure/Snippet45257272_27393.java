 try
        {
            if(txtPrivateKey.getText()=="")
            {
                Toast.makeText(MainActivity.this, "Generate key first please!!!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                String origin = txtChuoi.getText().toString();
                Cipher c = Cipher.getInstance("EC");
                c.init(Cipher.ENCRYPT_MODE,privateKey);
                encodeBytes = c.doFinal(origin.getBytes());
                txtMaHoa.setText(Base64.encodeToString(encodeBytes,Base64.DEFAULT));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
