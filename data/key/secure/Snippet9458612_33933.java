import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.net.*;

public class socket_server_simple {

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
        // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
        offset += numRead;
    }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
    public static String asHex (byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)
                strbuf.append("0");

            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }

    public static String aes_run(String message, String username, int mode) throws Exception
    {

        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(256); // 192 and 256 bits may not be available
        // Generate the secret key specs.
        SecretKey skey = kgen.generateKey();
        String keyfilepath=new String(username+".key");
        File keyfile = new File(keyfilepath);
        byte[] raw = getBytesFromFile(keyfile);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        System.out.println("Key file found\n\n");
           // Instantiate the cipher

        byte[] encdecres;
        String encdecresstr=new String();
        Cipher cipher = Cipher.getInstance("AES");
        if(mode==0)
            {
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
                encdecres= cipher.doFinal(message.getBytes());
                encdecresstr= new String(encdecres);
            }
            else if(mode==1)
            {
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
                encdecres = cipher.doFinal(message.getBytes());
                encdecresstr= new String(encdecres);
            }
            return encdecresstr;
    }

    public static void main(String args[]) throws Exception
    {
        char[] buffer = new char[3000];
        String message;
        String username;
        String orgmsg;
        char encryptedmsg[] = new char[400];
            if(args.length<1)
                {
                    System.out.println("Usage: java socket_server_simple <port_num>");
                    System.exit(1);
                }
        ServerSocket serversock = new ServerSocket(Integer.parseInt(args[0])); //can be any port
        System.out.println("Socket Instantiated!\n");
        Socket  connsock = serversock.accept();
        InputStreamReader instr =  new InputStreamReader(connsock.getInputStream());
        DataOutputStream outstr = new DataOutputStream(connsock.getOutputStream());
        System.out.println("Streams Instantiated!\n");  
        BufferedReader in = new BufferedReader(instr);
        System.out.println("Server is up! Waiting for username\n\n");
        username = in.readLine();
        System.out.println("Username recieved: "+username+"\n\n");              
        while(true)
        {
            System.out.println("Waiting for message\n");
            //message=in.readLine();
            //int len = instr.readLine(encryptedmsg,0,300);
            int len = in.read(buffer, 0,3000);  
            String strEnc = new String(buffer,0,len);
            //message = in.readLine();          
            //System.out.println("len: "+len);
            System.out.println("Encrypted msg received: "+strEnc);
            /*for(int i=0; i<400; i++)
            {
                System.out.print((encryptedmsg[i]));
            }*/
            //String strEnc = new String(encryptedmsg);
            //System.out.println(strEnc);
            orgmsg=aes_run(strEnc,username,1);          
            System.out.println("Decrypting message : "+orgmsg+"\n");
            //orgmsg=aes_run(encryptedmsg.toString(),username,1);

            //System.out.println("Encrypted Message :"+asHex(message.getBytes())+"\nPlain text"+orgmsg+"\n");
                //messagereturn = "\"You send me ->" +message.toUpperCase() + "\"\n";
                //outstr.writeBytes(messagereturn);
        }
    }
}
