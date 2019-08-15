dh=new data_helper(Resy.this);
        mac_db=dh.getData();

//        getdata=mac_db.toString();

        KeyGenerator kgen;
        try {
            kgen = KeyGenerator.getInstance("AES");

             kgen.init(128); // 192 and 256 bits may not be available

  // Generate the secret key specs.
                SecretKey skey = kgen.generateKey();
                byte[] raw = skey.getEncoded();

                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");


                // Instantiate the cipher

                Cipher cipher = Cipher.getInstance("AES");
                         getdata=mac_db.toString();
//              byte g1[]=getdata.getBytes();
//              System.out.println(g1);

        byte b[]=hexStringToByteArray(getdata);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] m1=cipher.doFinal(b);   // here pad block corrupt exception came.
                String originalString_mac = new String(original_macadress);
                Toast.makeText(getApplicationContext(),"Original : " +originalString_mac + " " + asHex(original_macadress) , Toast.LENGTH_LONG).show();
