public byte[] encrypt(byte[] data) {                                                                                                                         
    try {                                                                                                                          
        cipher.init(ENCRYPT_MODE, skey, new IvParameterSpec(iv));                                                                  
        DeflaterInputStream deflaterInput = new DeflaterInputStream(
            new CipherInputStream(new ByteArrayInputStream(data), cipher));

        return IOUtils.toByteArray(deflaterInput);                                                                                 
    } catch (Exception e) {                                                                                                        
        e.printStackTrace();                                                                                                       
        return new byte[0];                                                                                                        
    }                                                                                                                              
} 
