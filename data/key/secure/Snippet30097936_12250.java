FileInputStream epubCifr = new FileInputStream(Environment.getExternalStorageDirectory() + "/Skin/readium/epub_content/" + title + "/" + title + ".epub");
FileOutputStream epubDec = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Skin/readium/epub_content/" + title + "/" + title + "_dec.epub");

SecretKeySpec sks = new SecretKeySpec(key, "AES");

Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
cipher.init(Cipher.DECRYPT_MODE, sks);
CipherInputStream cis = new CipherInputStream(epubCifr, cipher);
int b;
byte[] d = new byte[16];
while ((b = cis.read(d)) != -1)
    epubDec.write(d, 0, b);

epubDec.flush();
epubDec.close();
cis.close();
