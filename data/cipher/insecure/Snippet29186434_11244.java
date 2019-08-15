public class DES {
    private KeyGenerator keyGen;
    private SecretKey secretKey;
    private Cipher cipher;
    private byte[] bytes_to_encrypt;
    private byte[] encrypted_bytes;
    private byte[] decrypted_bytes;
    public DES(byte[] bytes_to_encrypt) {
        this.bytes_to_encrypt = bytes_to_encrypt;
        generate_key();
        init_cipher();
        encrypt_text();
    }
    private void generate_key(){
        try{
            keyGen = KeyGenerator.getInstance("DES");
        }catch(Exception e){
            System.out.println(e.toString());   
        }
        keyGen.init(56);
        secretKey = keyGen.generateKey();
    }
    private void init_cipher(){
        try{
            cipher = Cipher.getInstance("DES");   
        }catch(Exception e){
            System.out.println(e.toString());    
        }
    }
    private void encrypt_text(){
        try{
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encrypted_bytes = cipher.doFinal(bytes_to_encrypt);
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    private void decrypt_text(){
        try{
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decrypted_bytes = cipher.doFinal(encrypted_bytes);
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    public byte[] get_encrypted_data(){
        return this.encrypted_bytes;    
    }
    public byte[] get_decrypted_data(){
        decrypt_text();
        return this.decrypted_bytes;    
    }
    public byte[] get_original_data(){
        return this.bytes_to_encrypt;
    }
    public SecretKey get_key(){
        return this.secretKey;    
    }
}
