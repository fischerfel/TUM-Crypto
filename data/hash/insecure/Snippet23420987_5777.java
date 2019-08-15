protected void ShowHash(android.net.Uri uri) {
    MessageDigest md = null;
    try {
        md = MessageDigest.getInstance("MD5");
        BufferedInputStream is = new BufferedInputStream(getContentResolver().openInputStream(uri));
        DigestInputStream dis = new DigestInputStream(is, md);
        while(dis.read() != -1) ;
        Toast.makeText(getApplicationContext(), bytesToHex(md.digest()),
                Toast.LENGTH_LONG).show();
    } catch(Exception e) {
        Toast.makeText(getApplicationContext(), e.toString(),
                Toast.LENGTH_LONG).show();
    }
    return;
}
