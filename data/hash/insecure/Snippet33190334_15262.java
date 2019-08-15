InputStream fis = null;
fis = new FileInputStream(fileName);

byte[] buffer = new byte[1024];
MessageDigest md =  md = MessageDigest.getInstance("MD5");

int numRead = 0;
do {
    numRead = fis.read(buffer);
    if (numRead > 0) {
        total_size+=numRead;
        md.update(buffer, 0, numRead);
    }
} while (numRead != -1);

fis.close();
byte[] hashValue = md.digest();
