package selfhash;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 *
 * @author dylan
 */
public class SelfHash {

    static byte[] data;
    static byte[] hash = new byte[4];

    public static void main(String[] args) {
    // TODO code application logic here
    SecureRandom random = new SecureRandom();
    random.nextBytes(hash);
    data = new byte[hash.length + 1];
    System.arraycopy(hash, 0, data, 0, hash.length);
    long c = 0;
    while (true) {
        recalculateData();
        byte[] dataHash = crc32AsByteArray(data);
        if (c % 10000000 == 0) {
        System.out.println("Calculated " + c + " hashes");
        System.out.println("Data: " + byteArrayToHex(data));
        System.out.println("Original hash: " + byteArrayToHex(hash) + " new hash: " + byteArrayToHex(dataHash));

        }
        if (Arrays.equals(hash, dataHash)) {
        System.out.println("Found a match!");
        System.out.println("Data: " + byteArrayToHex(data));
        System.out.println("Original hash: " + byteArrayToHex(hash) + " new hash: " + byteArrayToHex(dataHash));
        break;
        }
        c++;
    }
    }

    public static void recalculateData() {
    int position = hash.length;

    while (true) {
        int valueAtPosition = unsignedToBytes(data[position]);
        if (valueAtPosition == 255) {
        //increase size of data
        if (position == data.length-1) {
            byte[] newData = new byte[data.length + 1];
            System.arraycopy(data, 0, newData, 0, data.length);
            data = newData;
        }
        data[position] = (byte) (0);
        position++;
        } else {
        data[position] = (byte) (valueAtPosition + 1);
        break;
        }
    }
    }

    public static byte[] hexToByteArray(String hexString) {
    int len = hexString.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
            + Character.digit(hexString.charAt(i + 1), 16));
    }
    return data;
    }

    private static final char[] BYTE2HEX = ("000102030405060708090A0B0C0D0E0F"
        + "101112131415161718191A1B1C1D1E1F"
        + "202122232425262728292A2B2C2D2E2F"
        + "303132333435363738393A3B3C3D3E3F"
        + "404142434445464748494A4B4C4D4E4F"
        + "505152535455565758595A5B5C5D5E5F"
        + "606162636465666768696A6B6C6D6E6F"
        + "707172737475767778797A7B7C7D7E7F"
        + "808182838485868788898A8B8C8D8E8F"
        + "909192939495969798999A9B9C9D9E9F"
        + "A0A1A2A3A4A5A6A7A8A9AAABACADAEAF"
        + "B0B1B2B3B4B5B6B7B8B9BABBBCBDBEBF"
        + "C0C1C2C3C4C5C6C7C8C9CACBCCCDCECF"
        + "D0D1D2D3D4D5D6D7D8D9DADBDCDDDEDF"
        + "E0E1E2E3E4E5E6E7E8E9EAEBECEDEEEF"
        + "F0F1F2F3F4F5F6F7F8F9FAFBFCFDFEFF").toLowerCase().toCharArray();

    ; 

  public static String byteArrayToHex(byte[] bytes) {
    final int len = bytes.length;
    final char[] chars = new char[len << 1];
    int hexIndex;
    int idx = 0;
    int ofs = 0;
    while (ofs < len) {
        hexIndex = (bytes[ofs++] & 0xFF) << 1;
        chars[idx++] = BYTE2HEX[hexIndex++];
        chars[idx++] = BYTE2HEX[hexIndex];
    }
    return new String(chars);
    }

    public static String sha256AsHexString(byte[] bytes) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return byteArrayToHex(digest.digest(bytes));
    } catch (Exception e) {
        throw new Error(e);
    }
    }

    public static byte[] sha256AsByteArray(byte[] bytes) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(bytes);
    } catch (Exception e) {
        throw new Error(e);
    }
    }

    public static byte[] crc32AsByteArray(byte[] bytes) {
    Checksum checksum = new CRC32();
    checksum.update(bytes, 0, bytes.length);
    long value = checksum.getValue();
    byte[] resultExcess = ByteBuffer.allocate(8).putLong(value).array();
    byte[] result = new byte[4];
    System.arraycopy(resultExcess, 4, result, 0, 4);
    return result;
    }

    public static int unsignedToBytes(byte b) {
    return b & 0xFF;
    }
}
