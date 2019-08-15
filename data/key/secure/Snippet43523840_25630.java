    public Key unwrapKey(byte[] tmkByte) throws Exception {
                Cipher cipher = pool.get();
                byte[] de = cipher.doFinal(tmkByte);
                SecretKey tmk = new SecretKeySpec(de, "De");
                return tmk;    
            }

   public Key wrapKey(byte[] tByte) throws Exception {
                Cipher cipher = pool.get();  
                // byte,SecretKey...  
            }
