import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martin
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Integer[][] integerBlock = new Integer[5][5];
        //put some values in the matrix integerBlock
        int i,j;
        for(i=0; i<integerBlock.length; i++){
            for(j=0; j<integerBlock.length; j++){
                integerBlock[i][j] = i+j;
            }
        }
        //convert integerBlock to a string
        String str = IntToStr(integerBlock);
        System.out.println("original string is :      " + str);

        //hash the string using SHA512
        byte[] shaByteResult = SHA512(str);

        //print the generated key in hex
        System.out.println("byteArray2Hex  method:    " + byteArray2Hex(shaByteResult));
        System.out.println("byteArray2Hex2 method:    " + byteArray2Hex2(shaByteResult));

        //print the generated key in decimal
        System.out.print("Decimal values in bytes[]:");
        for(i=0; i< shaByteResult.length ;i++)
                System.out.print(shaByteResult[i]);      

        System.out.println("Compare decimal and correspondig hex");
        for(i=0; i< shaByteResult.length ;i++)
                System.out.println("bytes[" +i +"] = " + " decimal is " + shaByteResult[i] + "  hexadecimal " + Integer.toString((shaByteResult[i] & 0xff) + 0x100, 16).substring(1));
}

    public static String IntToStr(Integer[][] integerBlock){
        StringBuilder SB = new StringBuilder();

        int i,j;
        for(i=0; i<integerBlock.length; i++){
            for(j=0; j<integerBlock.length; j++){
                SB.append(integerBlock[i][j].toString());
                //stringBlock = stringBlock.concat(integerBlock[i][j].toString());                
            }
        }
        return SB.toString();
    }

    private static String byteArray2Hex(final byte[] bytes) {

        Formatter formatter = new Formatter();

        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
    //another solution
    private static String byteArray2Hex2(final byte[] bytes) {

        StringBuilder sb = new StringBuilder();
        int i;

        for(i=0; i< bytes.length ;i++)
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));

        return sb.toString();
    }

    public static byte[] SHA512(String str){
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(str.getBytes());
            bytes = md.digest();

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
        }
         return bytes;
    }    
}
