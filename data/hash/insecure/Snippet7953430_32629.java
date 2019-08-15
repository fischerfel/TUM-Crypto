in = new FileInputStream("/mnt/sdcard/200mb");
MessageDigest digester = MessageDigest.getInstance("sha1");
byte[] bytes = new byte[8192];
int byteCount;
int total = 0;
while ((byteCount = in.read(bytes)) > 0) {
    total += byteCount;
    digester.update(bytes, 0, byteCount);
    Log.d("sha", "processed " + total);
}    
