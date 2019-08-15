try {
    File root_sd = Environment.getExternalStorageDirectory();

    //original is a folder to encrypt       
    file = new File(root_sd + "/myfile/original");  
    String filename = file.getAbsolutePath();
    System.out.println("name of file for encryption ===>"+file.toString());
    fis = new FileInputStream(filename);    

    //encrypted folder should be in filename.des                      
    fos = new FileOutputStream("/mnt/sdcard/myfile/filename" + ".des");   

  } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
    Log.i("Encrypt ACtivity", "file io exception");
 }

 // Use PBEKeySpec to create a key based on a password.
 // The password is passed as a character array

  PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
  SecretKeyFactory keyFactory;

  try {
    keyFactory = SecretKeyFactory
            .getInstance("PBEWithMD5AndDES");

    SecretKey passwordKey = keyFactory.generateSecret(keySpec);

            // PBE = hashing + symmetric encryption. A 64 bit random
            // number (the salt) is added to the password and hashed
            // using a Message Digest Algorithm (MD5 in this example.).
            // The number of times the password is hashed is determined
            // by the interation count. Adding a random number and
            // hashing multiple times enlarges the key space.

    byte[] salt = new byte[8];
    Random rnd = new Random();
    rnd.nextBytes(salt);
    int iterations = 100;

    // Create the parameter spec for this salt and interation
    // count

    PBEParameterSpec parameterSpec = new PBEParameterSpec(salt,iterations);

    // Create the cipher and initialize it for encryption.

    Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
    cipher.init(Cipher.ENCRYPT_MODE, passwordKey, parameterSpec);

    // Need to write the salt to the (encrypted) file. The
    // salt is needed when reconstructing the key for
    // decryption.

    fos.write(salt);

    // Read the file and encrypt its bytes.

    byte[] input = new byte[64];
    int bytesRead;
    while ((bytesRead = fis.read(input)) != -1) {
        byte[] output = cipher.update(input, 0, bytesRead);
            if (output != null)
                fos.write(output);
        }

    byte[] output = cipher.doFinal();
            if (output != null)
                fos.write(output);

    fis.close();
    fos.flush();
