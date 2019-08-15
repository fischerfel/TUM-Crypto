    byte[] iv = new byte[16];
    in.read(iv);

    Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding"); 
    SecretKeySpec keySpec = new SecretKeySpec("password12345678".getBytes(), "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(iv);
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

    cos = new CipherInputStream(in, cipher);        

   while (offset < tmpBuffer.length && (numRead=cos.read(tmpBuffer, offset,      tmpBuffer.length-offset)) >= 0) {
      offset += numRead;
      savedFileSize = savedFileSize + numRead;
   }    
   // CREATE HASH FROM THE DOWNLOAD CHUNK PART
   String retCrC = DoEncryption.getCRC32ChecksumFromArray(tmpBuffer);
   String hash2 = Long.toHexString( Long.parseLong(retCrC) );

   // TEST SO THE REMOTE HASH MATCH THE LOCAL HASH
   if(!hash1.equals(hash2)){
   ...
