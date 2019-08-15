import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.net.*;

public class socket_client_simple {

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
        String encdecresstr= new String();
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
    public static void main(String[] args) throws Exception
    {
        String message,encrypted;
        String  returnmessage;
        String username=args[2];
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        if(args.length<3)
        {
            System.out.println("Usage: java socket_client_simple <ip_address> <Port_num> <username>");
            System.exit(1);
        }
        Socket  mysock = new Socket("localhost",Integer.parseInt(args[1])); 
        System.out.println("Socket Instantiated\n\n");      
        DataOutputStream out = new DataOutputStream( mysock.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(mysock.getInputStream()));
        out.writeBytes(username+"\n");

        do 
        {
            System.out.println("Enter Message: ");
            message=keyboard.readLine();
            System.out.println("Sending message\n");

            //System.out.println(aes_run(message.getBytes(),username,0) + "\n");
            encrypted = aes_run(message,username,0);
            System.out.println("length: "+encrypted.length());
            out.write(encrypted + "\n");
            System.out.println("message sent: "+encrypted );
            System.out.println("Waiting for reply\n\n");
            returnmessage = in.readLine();
            System.out.println("Server replied: " + aes_run(returnmessage,username,1));
        }while(!message.equals("bye"));
        mysock.close();
    }

}
