import java.util.Random;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.lang.String;
import java.io.File;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AESTest {

    public static byte[] update_iv(byte iv[], long blocks) {
        ByteBuffer buf = ByteBuffer.wrap(iv);
        buf.order(ByteOrder.BIG_ENDIAN);
        long tblocks = buf.getLong(8);
        tblocks += blocks;
        buf.putLong(8, tblocks);

        return buf.array();
    }

    public static void main(String args[]) throws Exception {
        if (args.length < 3) {
            System.out.println("Not enough parameters:");
            System.out.println("keyfile input output [append]");
            return;
        }


        File keyfile = new File(args[0]);
        DataInputStream key_in;
        DataOutputStream key_out;
        DataInputStream input = new DataInputStream(new FileInputStream(args[1]));
        DataOutputStream output = null;

        byte key[] = new byte[16 + 16];
        byte aeskey[] = new byte[16];
        byte iv[] = new byte[16];
        byte ivOrig[] = new byte[16];
        long blocks = 0;
        int count = 0;

        if (!keyfile.isFile()) {
            System.out.println("Creating new key");
            Random ranGen = new SecureRandom();
            ranGen.nextBytes(aeskey);
            ranGen.nextBytes(iv);

            iv = update_iv(iv, 0);

            System.arraycopy(iv, 0, ivOrig, 0, 16);
       } else {
            System.out.println("Using existing key...");
            key_in = new DataInputStream(new FileInputStream(keyfile));

            try {
                for (int i = 0; i < key.length; i++)
                    key[i] = key_in.readByte();
            } catch (EOFException e) {
            }

            System.arraycopy(key, 0, aeskey, 0, 16);
            System.arraycopy(key, 16, iv, 0, 16);
            System.arraycopy(key, 16, ivOrig, 0, 16);

            if (args.length == 4) {
                if (args[3].compareTo("append") == 0) {
                    blocks = key_in.readLong();
                    count = key_in.readInt();

                    System.out.println("Moving IV " + blocks + " forward");
                    iv = update_iv(iv, blocks);
                    output = new DataOutputStream(new FileOutputStream(args[2], true)); // Open file in append mode
                }
            }
        }

        if (output == null)
            output = new DataOutputStream(new FileOutputStream(args[2])); // Open file at the beginnging

        key_out = new DataOutputStream(new FileOutputStream(keyfile));

        Cipher c = Cipher.getInstance("AES/CTR/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(aeskey, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        c.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        if (count != 0) {
            c.update(new byte[count]);
        }

        byte cc[] = new byte[1];
        try {
            while (true) {
                cc[0] = input.readByte();
                cc = c.update(cc);
                output.writeByte(cc[0]);

                if (count == 15) {
                    blocks++;
                    count = 0;
                } else {
                    count++;
                }
            }
        } catch (EOFException e) {
        }

        cc = c.doFinal();
        if (cc.length != 0)
            output.writeByte(cc[0]);

        // Before we quit, lets write our AES key, start IV, and current IV to disk
        for (int i = 0; i < aeskey.length; i++)
            key_out.writeByte(aeskey[i]);

        for (int i = 0; i < ivOrig.length; i++)
            key_out.writeByte(ivOrig[i]);


        System.out.println("Blocks: " + blocks);
        System.out.println("Extra: " + count);
        key_out.writeLong(blocks);
        key_out.writeInt(count);

    }
}
