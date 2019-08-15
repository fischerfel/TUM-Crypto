System.out.println("adding BouncyCastleProvider2");
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Error e) {
            e.printStackTrace();
        }
        try{
            // La clef RSA
            PEMReader pemReader = new PEMReader(new StringReader(key));
            KeyPair pObj = (KeyPair) pemReader.readObject(); 

            // Encrypt
            Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPPadding"); 
            cipher.init(Cipher.DECRYPT_MODE, pObj.getPrivate());
            return cipher.doFinal(array);

        }catch(Exception e){
            e.printStackTrace();
        }
