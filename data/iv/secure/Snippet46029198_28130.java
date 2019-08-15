public byte[] decrypt(byte[] data) {                                                                                                                                                                                                                               
    byte[] result = new byte[0];                                                                                                    
    try {                                                                                                                           
        cipher.init(DECRYPT_MODE, skey, new IvParameterSpec(iv));                                                                   

        InflaterInputStream inflaterStream = new InflaterInputStream(
            new CipherInputStream(new ByteArrayInputStream(data), cipher));
        return IOUtils.toByteArray(inflaterStream);                                                                                 
    } catch (Exception e) {                                                                                                         
        e.printStackTrace();                                                                                                        
        return result;                                                                                                              
    }                                                                                                                               
}
