MessageDigest md = MessageDigest.getInstance("SHA-256");
FileInputStream fis = new FileInputStream("~/Documents/Path/To/File.txt");

byte[] dataBytes = new byte[1024];

int nread = 0; 
while ((nread = fis.read(dataBytes)) != -1) {
  md.update(dataBytes, 0, nread);
};
byte[] mdbytes = md.digest();

//Convert "mdbytes" to hex String:
StringBuffer hexString = new StringBuffer();
for (int i=0;i<mdbytes.length;i++) {
  hexString.append(Integer.toHexString(0xFF & mdbytes[i]));
}

return hexString.toString();
