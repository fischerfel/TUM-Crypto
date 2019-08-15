public static String calculateMD5(Context context, Uri fileUri) {
    MessageDigest digest;
    try {
        digest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
        Log.e(LOG_TAG, "Exception while getting digest", e);
        return null;
    }

    InputStream is;
    try {
        is = context.getContentResolver().openInputStream(fileUri);
        // is = new FileInputStream("some_file_location");
    } catch (FileNotFoundException e) {
        Log.e(LOG_TAG, "Exception while getting FileInputStream", e);
        return null;
    }

    byte[] buffer = new byte[8192];
    int read;
    try {
        while ((read = is.read(buffer)) > 0) {
            digest.update(buffer, 0, read);
        }
        byte[] md5sum = digest.digest();

        // Create Hex String
        StringBuilder hexString = new StringBuilder();
        for (byte aMessageDigest : md5sum) {
            String h = Integer.toHexString(0xFF & aMessageDigest);
            while (h.length() < 2)
                h = "0" + h;
            hexString.append(h);
        }
        return hexString.toString();
    } catch (IOException e) {
        throw new RuntimeException("Unable to process file for MD5", e);
    } finally {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Exception on closing MD5 input stream", e);
        }
    }
}
