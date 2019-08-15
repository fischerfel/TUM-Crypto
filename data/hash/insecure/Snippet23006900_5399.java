String filename =title.replace(" ","")+j+".nomedia";
File destination = new File(MyApplication.getPhotoStorage() ,filename);


URL url = new URL (url_image);

OutputStream os = new FileOutputStream(destination);

MessageDigest md = MessageDigest.getInstance("MD5");
try (InputStream is = url.openStream()) {
    DigestInputStream dis = new DigestInputStream(is, md);
    byte[] b = new byte[2048];
    int length;

    while ((length = dis.read(b)) != -1) {
        os.write(b, 0, length);
    }
}

byte[] digest = md.digest();

os.close();
localPhotosUrl.add(destination.getAbsolutePath());
