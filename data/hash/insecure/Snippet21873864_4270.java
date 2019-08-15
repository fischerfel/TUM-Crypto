1) Write the contents of the files to temperory files / permenant ones if you are anyway      
   going to store them. 
2) While constructing the outputstream for writing the file,instead of creating normal 
   FileoutourStream create DigestOutputStream and pass the FileOutputStream to
   DigestOutputStream's constructor.
3) DigestOutputStream's constructor takes another parameter called MessageDigest.You can   
   instantiate this seperately :MessageDigest md = MessageDigest.getInstance("MD5");
   and pass on this instance to constructor in step 2. 
4) after you call write on the output stream and done with the write calls,
   Call one of the digest methods on the MessageDigest instance as follows:  
   mdos.getMessageDigest().digest().
6) store the string representation of byte array returned by digest by using technique  
   as suggested in following link :
