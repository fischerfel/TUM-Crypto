import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.io.BufferedReader;
import java.lang.StringBuilder;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;

public class RsaTest
{
    public static String readTokenFile() throws IOException, FileNotFoundException
    {
        BufferedReader br = new BufferedReader(new FileReader("token.txt"));
        String everything;
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } finally {
            br.close();
        }
        return everything;
    }

    public static void main(String args[])
    {
        BigInteger d = new BigInteger("295037637291191738956635211796692963240490064989667014592826352469032152" +
        "554302874575878226107217392868462250989293041036431894475130143513053896027950170894654313285696024572" +
        "555534662699797386157658559164375812029364988576033978927620169476835254057350243662602730036794048964" +
        "222696882434825511051317613969394699304819377647584836298910958813068451008480950615750481597424491969" +
        "616122089713036168262562790026376709519880676568817708332018002498979363533291261269633760290508292885" +
        "685631554642526071767451720995816315263034482926025947996376673879307443752926326504743341468067701663" +
        "647011041587666229832948060125468304864468556385524233326117655101116575438068518128417547551238351385" +
        "860308667178310273798307275716775496726007972420724941548071903190058191309574401221590126135595556423" +
        "538805604263913766128874929792845212075676240959916344274635408931764294117768329862724498717467735936" +
        "771182991195111736108715670105340851228608489685386356809638648591870140613333363474891211943508005174" +
        "556955182421433325620501790085286962409667669154804819913241047270891375736816718480232577171854082683" +
        "282930378415208382344040938073467357285325060767468878914840146155371082700586582168876180646622366892" +
        "654491190772272882871959076736231875073");
        BigInteger e = new BigInteger("65537");

        byte[] encryptedBytes = new byte[]{};
        try {
            String encryptedToken = readTokenFile();
            encryptedBytes = DatatypeConverter.parseBase64Binary(encryptedToken);
        } catch (FileNotFoundException ex) {
            System.out.println("File token.txt was not found.");
        } catch (IOException ex) {
            System.out.println("IO error.");
        }
        RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(d, e);
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            RSAPrivateKey privateKey = (RSAPrivateKey) factory.generatePrivate(privateKeySpec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] token = cipher.doFinal(encryptedBytes);
            System.out.println(token);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Missing algorithm.");
        } catch (InvalidKeySpecException ex) {
            System.out.println("Invalid key spec.");
        } catch (NoSuchPaddingException ex) {
            System.out.println("No such padding.");
        } catch (InvalidKeyException ex) {
            System.out.println("Invalid key.");
        } catch (IllegalBlockSizeException ex) {
            System.out.println("Wrong block size.");
        } catch (BadPaddingException ex) {
            System.out.println("Bad padding.");
        }
    }
}
