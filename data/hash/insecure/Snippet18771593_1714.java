InputStream is = null;
File f = ...
int bufSize = ...
byte[] md5sum = null;

try {
    MessageDigest digest = MessageDigest.getInstance("MD5");
    is = new FileInputStream(f);
    byte[] buffer = new byte[bufSize];

    int read = 0;
    while((read = is.read(buffer)) > 0) digest.update(buffer,0,read);
    md5sum = digest.digest();
} catch (Exception e){
} finally {
    try{
        if(is != null) is.close();
    } catch (IOException e){}
}
