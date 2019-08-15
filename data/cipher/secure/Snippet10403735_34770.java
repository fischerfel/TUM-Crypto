 String clen = in.readLine(); // read the length 
 byte[] array = new byte[Integer.parseInt(clen)]; // create the array of that length 
 dist.readFully(array); // read the array 

 // i am unable to read the array here at all !

 PrivateKey priKey = readKeyFromFileprivate("aliceprivate.txt");
 Cipher vote = Cipher.getInstance("RSA");
 vote.init(Cipher.DECRYPT_MODE, priKey);
 byte[] voteData = vote.doFinal(array);
 System.out.println(voteData);

// finally print the decrypted array
