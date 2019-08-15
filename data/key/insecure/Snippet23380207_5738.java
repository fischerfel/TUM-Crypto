private byte[] key = { (byte) 141, 75, 21, 92, (byte) 201, (byte) 255,
        (byte) 129, (byte) 229, (byte) 203, (byte) 246, (byte) 250, 120,
        25, 54, 106, 62, (byte) 198, 33, (byte) 166, 86, 65, 108,
        (byte) 215, (byte) 147 };

private final byte[] iv = { 0x1E, 0x39, (byte) 0xF3, 0x69, (byte) 0xE9, 0xD,
        (byte) 0xB3, 0x3A, (byte) 0xA7, 0x3B, 0x44, 0x2B, (byte) 0xBB,
        (byte) 0xB6, (byte) 0xB0, (byte) 0xB9 };
   long start = System.currentTimeMillis();

    // create paths
    backupPath = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/WhatsApp/Databases/msgstore.db.crypt5";
    outputPath = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/WhatsApp/Databases/msgstore.db.decrypt";

    File backup = new File(backupPath);

    // check if file exists / is accessible
    if (!backup.isFile()) {
        Log.e(TAG, "Backup file not found! Path: " + backupPath);
        return;
    }

    // acquire account name
    AccountManager manager = AccountManager.get(this);
    Account[] accounts = manager.getAccountsByType("com.google");

    if (accounts.length == 0) {
        Log.e(TAG, "Unable to fetch account!");
        return;
    }

    String account = accounts[0].name;

    try {
        // calculate md5 hash over account name
        MessageDigest message = MessageDigest.getInstance("MD5");
        message.update(account.getBytes());
        byte[] md5 = message.digest();

        // generate key for decryption
        for (int i = 0; i < 24; i++)
            key[i] ^= md5[i & 0xF];

        // read encrypted byte stream
        byte[] data = new byte[(int) backup.length()];
        DataInputStream reader = new DataInputStream(new FileInputStream(
                backup));
        reader.readFully(data);
        reader.close();

        // create output writer
        File output = new File(outputPath);
        DataOutputStream writer = new DataOutputStream(
                new FileOutputStream(output));

        // decrypt file
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secret = new SecretKeySpec(key, "AES");
        IvParameterSpec vector = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secret, vector);
        writer.write(cipher.update(data));
        writer.write(cipher.doFinal());
        writer.close();
    } catch (NoSuchAlgorithmException e) {
        Log.e(TAG, "Could not acquire hash algorithm!", e);
        return;
    } catch (IOException e) {
        Log.e(TAG, "Error accessing file!", e);
        return;
    } catch (Exception e) {
        Log.e(TAG, "Something went wrong during the encryption!", e);
        return;
    }

    long end = System.currentTimeMillis();

    Log.i(TAG, "Success! It took " + (end - start) + "ms");
