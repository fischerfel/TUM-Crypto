public FunctionClass() {  
         try {  
            keyGenerator = KeyGenerator.getInstance("Blowfish");  
            secretKey = keyGenerator.generateKey();  

           cipher = Cipher.getInstance("Blowfish");  
        } catch (NoSuchPaddingException ex) {  
           System.out.println(ex);  
        } catch (NoSuchAlgorithmException ex) {  
            System.out.println(ex);  
       }  
}
