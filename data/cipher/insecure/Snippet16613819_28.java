try{
   Cipher c = Cipher.getInstance("AES");
}
catch(NoSuchAlgorithmException e){
   //handle the case of having no matching algorithm
}
catch(NoSuchPaddingException e){
   //handle the case of a padding problem
}
