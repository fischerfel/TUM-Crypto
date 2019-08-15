SecureRandom sr = new SecureRandom(); //create new secure random
byte [] iv = new byte[8]; //create an array of 8 bytes 
sr.nextBytes(iv); //create random bytes to be used for the IV (?) Not too sure.
IvParameterSpec IV = new IvParameterSpec(iv); //creating the IV 