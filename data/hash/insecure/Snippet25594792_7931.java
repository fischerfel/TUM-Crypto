public void backup(String filename) {
    File file = new File(getBackupDirectory(), filename);
    FileOutputStream fileOutputStream = null;
    ZipOutputStream stream = null;
    try {
        String settingsMD5 = null;
        String databaseMD5 = null;
        if (file.exists())
            file.delete();
        fileOutputStream = new FileOutputStream(file);
        stream = new ZipOutputStream(new BufferedOutputStream(fileOutputStream));
        File database = getDatabasePath(databaseFileName);
        File dataDirectory = getFilesDir();
        if (dataDirectory != null) {
            File settings = new File(dataDirectory.getParentFile(), "/shared_prefs/" + PREFERENCES_FILENAME);
            settingsMD5 = zipFile("preferences", stream, settings);
        }
        databaseMD5 = zipFile("database.db", stream, database);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(META_DATE, new SimpleDateFormat(DATE_FORMAT, Locale.US).format(new Date()));
            jsonObject.put(META_DATABASE, databaseMD5);
            jsonObject.put(META_SHARED_PREFS, settingsMD5);
        } catch (Exception e) {
            e.printStackTrace();
        }

        InputStream metadata = new ByteArrayInputStream(jsonObject.toString().getBytes("UTF-8"));
        zipInputStream(stream, metadata, new ZipEntry("metadata"));
        stream.finish();
        stream.close();
        stream = null;
        return file;
    } catch (FileNotFoundException e) {
     //handling errrors
    } catch (IOException e) {
     //handling errrors
    } 
}

private String zipFile(String name, ZipOutputStream zipStream, File file) throws FileNotFoundException, IOException {
        ZipEntry zipEntry = new ZipEntry(name);
        return zipInputStream(zipStream, new FileInputStream(file), zipEntry);
    }

private String zipInputStream(ZipOutputStream zipStream, InputStream fileInputStream, ZipEntry zipEntry) throws IOException {
    InputStream inputStream = new BufferedInputStream(fileInputStream);
    MessageDigest messageDigest = null;
    try {
        messageDigest = MessageDigest.getInstance("MD5");
        if (messageDigest != null)
            inputStream = new DigestInputStream(inputStream, messageDigest);
    } catch (NoSuchAlgorithmException e) {
    }

    zipStream.putNextEntry(zipEntry);
    inputToOutput(inputStream, zipStream);
    zipStream.closeEntry();
    inputStream.close();

    if (messageDigest != null) {
        return getDigestString(messageDigest.digest());
    }
    return null;
}

private String getDigestString(byte[] digest) {
    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < digest.length; i++) {
        String hex = Integer.toHexString(0xFF & digest[i]);
        if (hex.length() == 1) {
            hex = new StringBuilder("0").append(hex).toString();
        }
        hexString.append(hex);
    }
    return hexString.toString();
}

private void inputToOutput(InputStream inputStream, OutputStream outputStream) throws IOException {
    byte[] buffer = new byte[BUFFER];
    int count = 0;
    while ((count = inputStream.read(buffer, 0, BUFFER)) != -1) {
        outputStream.write(buffer, 0, count);
    }
}
