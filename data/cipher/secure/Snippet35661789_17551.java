    DataInputStream dis = new DataInputStream(msock.getInputStream());
    DataOutputStream dos = new DataOutputStream(msock.getOutputStream());

    String file2dl = dis.readLine(); //2
    File file = new File(sharedDirectory.toString() + "\\" + file2dl);
    dos.writeLong(file.length()); //3+

    //Get file name without extension.
    String fileName = Files.getNameWithoutExtension(file2dl);

    //AES-128 bit key initialization.
    byte[] keyvalue = "AES128BitPasswd".getBytes();
    SecretKey key = new SecretKeySpec(keyvalue, "AES");

    //Initialize the Cipher.
    Cipher encCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    encCipher.init(Cipher.ENCRYPT_MODE, key);

    //Get the IV from cipher.
    IvParameterSpec spec = null;
    try {
        spec = encCipher.getParameters().getParameterSpec(IvParameterSpec.class);
    } catch (InvalidParameterSpecException ex) {
        Logger.getLogger(PeersController.class.getName()).log(Level.SEVERE, null, ex);
    }

    byte[] iv = spec.getIV();

    dos.write(iv, 0, iv.length);
    File tempDir = new File(tempDirectory.toString());
    //Encryption Mechanism.
        try (FileInputStream fis = new FileInputStream(file)) {
            try (CipherOutputStream cos = new CipherOutputStream(dos, encCipher);
                    FileInputStream stream = new FileInputStream(tempDir + "\\" + fileName + ".encr")) {
                int read, r;
                byte[] buffer = new byte[1024 * 1024];
                while ((read = fis.read(buffer)) != -1) {
                    cos.write(buffer, 0, read);
                }
        }
    }
 }
