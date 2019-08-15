 KeyGenerator generator = KeyGenerator.getInstance("AES");      
 generator.init(256);       SecretKey secretKey = generator.generateKey();      
 byte[] raw= secretKey.getEncoded();        
 SecretKeySpec sskey= new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");         
        if (mode == Cipher.ENCRYPT_MODE) { 
           Cipher.getMaxAllowedKeyLength("AES"));   
           cipher.init(Cipher.ENCRYPT_MODE, sskey);             
           CipherInputStream cis = new CipherInputStream(is, cipher); 
           doCopy(cis, os);         
        } else if (mode == Cipher.DECRYPT_MODE) { 
           cipher.init(Cipher.DECRYPT_MODE, sskey); 
           CipherOutputStream cos = new CipherOutputStream(os, cipher);             
           doCopy(is, cos);         
        }
