URL url = new URL(url);
URLConnection urlConnection = url.openConnection();
urlConnection.setConnectTimeout(1000);
urlConnection.setReadTimeout(1000);
logger.error(urlConnection.getContent() + " ");
InputStream is = urlConnection.getInputStream();


// first reading of file is:

int i;
File file = new File("nameOfFile");
BufferedInputStream bis = new BufferedInputStream(is);
BufferedOutputStream bos = 
           new BufferedOutputStream(new FileOutputStream(file.getName()));
while ((i = bis.read()) != -1) {
    bos.write(i);
}
bos.flush();
bis.close();   
sha1(file);

// second reading of file is:

BufferedReader reader = new BufferedReader(new InputStreamReader(is));
String line;

while ((line = reader.readLine()) != null) {
   // do something
}

protected byte[] sha1(final File file) throws Exception {
    if (file == null || !file.exists()) {
        return null;
    }
    final MessageDigest messageDigest = MessageDigest.getInstance(SHA1);

    InputStream is = new BufferedInputStream(new FileInputStream(file));
    try {
        final byte[] buffer = new byte[1024];
        for (int read = 0; (read = is.read(buffer)) != -1;) {
            messageDigest.update(buffer, 0, read);
        }
    } finally {
        IOUtils.closeQuietly(is);
    }
    return messageDigest.digest();
}
