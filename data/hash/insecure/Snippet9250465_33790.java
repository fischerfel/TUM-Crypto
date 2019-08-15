String classname = "java.util.Random"; //fill in the your class
MessageDigest digest = MessageDigest.getInstance("MD5");
Class test = Class.forName(classname);
InputStream in = test.getResourceAsStream("/" + classname.replace(".", "/") + ".class");
byte[] buffer = new byte[8192];
int read = 0;

while ((read = in.read(buffer)) > 0) {
    digest.update(buffer, 0, read);
}
byte[] md5sum = digest.digest();
BigInteger bigInt = new BigInteger(1, md5sum);
String output = bigInt.toString(16);
System.out.println(output);

in.close();
