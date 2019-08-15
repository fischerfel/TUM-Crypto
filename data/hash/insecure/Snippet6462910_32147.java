public String convert(String str){
        String a=null;
         char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
                  'a', 'b', 'c', 'd', 'e', 'f' }; 
        try {
            a=new   String(str.getBytes("ISO8859_1"),"UTF-8");
            byte[] strTemp = str.getBytes(); 
            MessageDigest mdTemp = MessageDigest.getInstance("MD5"); 
               mdTemp.update(strTemp); 
               byte[] md = mdTemp.digest(); 
               int j = md.length; 
               char str1[] = new char[j * 2]; 
               int k = 0; 
               for (int i = 0; i < j; i++) { 
                byte b = md[i]; 
                //System.out.println((int)b);  
                str1[k++] = hexDigits[b >> 4 & 0xf]; 
                str1[k++] = hexDigits[b & 0xf]; 
               } 


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
 return new String(str1); 

}
