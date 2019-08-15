class Crypt {

private final String TAG = Crypt.class.getSimpleName();

private String seedValue = "";
private String ivValue = "";

private Context c;

Crypt() {
    if (!checkKeys() && c != null) {
        generateKeys();
    } else {
        Log.d(TAG, "No context - can't generate keys");
    }
}

Crypt(Context c) {
    this.c = c;
    if (!checkKeys()) {
        generateKeys();
    } else {
        SharedPreferences seed = c.getSharedPreferences("Seed", Context.MODE_PRIVATE);
        seedValue = seed.getString("SeedKey", "");
        SharedPreferences iv = c.getSharedPreferences("Iv", Context.MODE_PRIVATE);
        ivValue = iv.getString("IvKey", "");
    }
}

String encrypt(String input) {
    String encryptedString = "";
    Log.d(TAG + "$Encrypt", seedValue + "\n" + ivValue + "\n" + String.valueOf(checkKeys()));
    try {
        SecretKeySpec aesSpec = new SecretKeySpec(seedValue.getBytes("UTF-8"), 0, 32, "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        IvParameterSpec ivSpec = new IvParameterSpec(ivValue.getBytes("UTF-8"), 0, 16);
        c.init(Cipher.ENCRYPT_MODE, aesSpec, ivSpec);
        byte[] encryptedBytes = c.doFinal(input.getBytes("UTF-8"));
        encryptedString = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        Log.d(TAG, encryptedString);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return encryptedString;
}

String decrypt(String input) {
    String decryptedString = "";
    Log.d(TAG + "$Decrypt", seedValue + "\n" + ivValue + "\n" + String.valueOf(checkKeys()));
    try {
        SecretKeySpec aesSpec = new SecretKeySpec(seedValue.getBytes("UTF-8"), 0, 32, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        IvParameterSpec ivSpec = new IvParameterSpec(ivValue.getBytes("UTF-8"), 0, 16);
        cipher.init(Cipher.DECRYPT_MODE, aesSpec, ivSpec);
        byte[] decodedBytes = Base64.decode(input.getBytes(), Base64.DEFAULT);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        decryptedString = Base64.encodeToString(decryptedBytes, Base64.DEFAULT);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return decryptedString;
}

private void generateKeys() {
    KeyGeneratorAsyncTask keyGenerator = new KeyGeneratorAsyncTask();
    String seedIv = keyGenerator.doInBackground();

    final StringBuilder seedRegenerator = new StringBuilder();
    for (int i = 0; i < 32; i++) {
        seedRegenerator.append(seedIv.charAt(i));
    }
    seedValue = seedRegenerator.toString();

    final StringBuilder ivRegenerator = new StringBuilder();
    for (int i = 33; i < 49; i++) {
        ivRegenerator.append(seedIv.charAt(i));
    }
    ivValue = ivRegenerator.toString();

    SharedPreferences prefs = c.getSharedPreferences("Keys", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putBoolean("HasKeys", true).apply();

    SharedPreferences hasSeed = c.getSharedPreferences("Seed", Context.MODE_PRIVATE);
    SharedPreferences.Editor hasSeedEditor = hasSeed.edit();
    hasSeedEditor.putString("SeedKey", seedValue).apply();

    SharedPreferences hasIv = c.getSharedPreferences("Iv", Context.MODE_PRIVATE);
    SharedPreferences.Editor hasIvEditor = hasIv.edit();
    hasIvEditor.putString("IvKey", ivValue).apply();
}

private boolean checkKeys() {
    SharedPreferences preferences = c.getSharedPreferences("Keys", Context.MODE_PRIVATE);
    return preferences.getBoolean("HasKeys", false);
}
