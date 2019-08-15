public void vers(){
    // Das Passwort bzw der Schluesseltext
    keyStr = keyedit.getText().toString();
    // byte-Array erzeugen

    try {
        key = (keyStr).getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    // aus dem Array einen Hash-Wert erzeugen mit MD5 oder SHA

    try {
        sha = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    key = sha.digest(key);
    // nur die ersten 128 bit nutzen
    key = Arrays.copyOf(key, 16); 
    // der fertige Schluessel
    secretKeySpec = new SecretKeySpec(key, "AES");
}
