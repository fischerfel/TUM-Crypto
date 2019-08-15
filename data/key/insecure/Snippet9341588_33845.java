KeyGenerator kgen = KeyGenerator.getInstance("AES"); 
            kgen.init(128);
            byte raw[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
            SecretKeySpec spec = new SecretKeySpec(raw, "AES");
            //SecretKey key = kgen.generateKey();
            //byte keybytes[] = key.getEncoded();
            //SecretKeySpec spec = new SecretKeySpec(keybytes, "AES");
