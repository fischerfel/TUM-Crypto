       MessageDigest md = MessageDigest.getInstance("MD5");


       String salts = "a,d,d,e,d,_,s,a,l,t";

        String salttmps[] = salts.split(",");
        byte salt[] = new byte[salttmps.length];

        for (int i = 0; i < salt.length; i++) {
          salt[i] = Byte.parseByte(salttmps[i]);
        }
        md.update(salt); 
        md.update(password.getBytes());

        byte byteData[] = md.digest();


        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        } 
        password = sb.toString();
