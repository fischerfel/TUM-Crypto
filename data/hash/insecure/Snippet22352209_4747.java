import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.Properties;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.net.ftp.FTPClient;

MessageDigest digest = MessageDigest.getInstance("MD5");

FTPClient ftp=new FTPClient();
ftp.enterLocalPassiveMode();
ftp.connect("address");
ftp.login('username','password');
InputStream iStream=ftp.retrieveFileStream("path of the file");
BufferedInputStream bInf=new BufferedInputStream(iStream);
byte[] buffer = new byte[8192];    
int read = 0;
while ((read = bInf.read(buffer)) !=-1)
{
digest.update(buffer, 0, read);
};

byte[] md5sum = digest.digest();
BigInteger bigInt = new BigInteger(1, md5sum)
def x=bigInt.toString(16).padLeft(32,'0')
