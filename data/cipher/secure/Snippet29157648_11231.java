        //TEST IMPLEMENTATION

    String _path_to_file = "";

    Random _random = new Random();
    long[] _key_file = new long[4];
    _key_file[0] = _random.nextInt(Integer.MAX_VALUE);
    _key_file[1] = _random.nextInt(Integer.MAX_VALUE);
    _key_file[2] = _random.nextInt(Integer.MAX_VALUE);
    _key_file[3] = _random.nextInt(Integer.MAX_VALUE);

    long[] _iv_file = new long[4];
    _iv_file[0] = _random.nextInt(Integer.MAX_VALUE);
    _iv_file[1] = _random.nextInt(Integer.MAX_VALUE);
    _iv_file[2] = 0;
    _iv_file[3] = 0;

    long[] _returned = cbc_mac(_path_to_file, _key_file, _iv_file);


//FUNCTIONS

//this function loops over the parts of the file to calculate the cbc-mac and is the problem
public static long[] cbc_mac(String _path, long[] k, long[] n) throws Exception {
    File _file = new File(_path);
    long _file_length = _file.length();
    RandomAccessFile _raf = new RandomAccessFile(_file, "r");

    //This works fine and fast
    ArrayList<chunksData> chunks = get_chunks(_file_length);

    long[] file_mac = new long[4];
    file_mac[0] = 0;
    file_mac[1] = 0;
    file_mac[2] = 0;
    file_mac[3] = 0;

    //prepare encrypt
    String iv = "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";
    IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
    SecretKeySpec keySpec = new SecretKeySpec(a32_to_str(k).getBytes("ISO-8859-1"), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
    //end prepare encrypt

    for(chunksData _chunksData : chunks) {

        int pos = (int)_chunksData._key;
        int size = (int)_chunksData._value;

        long[] chunk_mac = new long[4];
        chunk_mac[0] = n[0];
        chunk_mac[1] = n[1];
        chunk_mac[2] = n[0];
        chunk_mac[3] = n[1];

        byte[] bytes = new byte[16];

        //this loop is the really slow part since it loops over every 16 bytes
        for (int i = pos; i < pos + size; i += 16) {
            _raf.seek(i);
            int _did_read = _raf.read(bytes, 0, 16);
            if(_did_read != 16) {
                for(int o = _did_read;o<16;o++) {
                    bytes[o] = (byte)((char)'\0');
                }
            }

            long[] block = str_to_a32(new String(bytes, "ISO-8859-1"));

            chunk_mac[0] = chunk_mac[0] ^ block[0];
            chunk_mac[1] = chunk_mac[1] ^ block[1];
            chunk_mac[2] = chunk_mac[2] ^ block[2];
            chunk_mac[3] = chunk_mac[3] ^ block[3];

            chunk_mac = str_to_a32(new String(cipher.doFinal(a32_to_str(chunk_mac).getBytes("ISO-8859-1")), "ISO-8859-1"));

        }

        file_mac[0] = file_mac[0] ^ chunk_mac[0];
        file_mac[1] = file_mac[1] ^ chunk_mac[1];
        file_mac[2] = file_mac[2] ^ chunk_mac[2];
        file_mac[3] = file_mac[3] ^ chunk_mac[3];
        file_mac = str_to_a32(new String(cipher.doFinal(a32_to_str(file_mac).getBytes("ISO-8859-1")), "ISO-8859-1"));

    }

    _raf.close();

    return file_mac;

}

//this function works fine and fast
public static ArrayList<chunksData> get_chunks(long size) {

    ArrayList<chunksData> chunks = new ArrayList<chunksData>();

    long p = 0;
    long pp = 0;

    for (int i = 1; i <= 8 && p < size - i * 0x20000; i++) {
        chunksData chunks_temp = new chunksData(p, i*0x20000);
        chunks.add(chunks_temp);
        pp = p;
        p += chunks_temp._value;
    }

    while(p < size) {
        chunksData chunks_temp = new chunksData(p, 0x100000);
        chunks.add(chunks_temp);
        pp = p;
        p += chunks_temp._value;            
    }

    chunks.get(chunks.size()-1)._value = size-pp;
    if((int)chunks.get(chunks.size()-1)._value == 0) {
        chunks.remove(chunks.size()-1);
    }

    return chunks;

}

public static class chunksData {
    public long _key = 0;
    public long _value = 0;
    public chunksData(long _keyT, long _valueT){
        this._key = _keyT;
        this._value = _valueT;
    }
}

//helper function which also contains a loop and is used in the problematic loop, so might be a problem though I don't know how to speed it up
public static long[] str_to_a32(String string) {
    if (string.length() % 4 != 0) {
        string += new String(new char[4 - string.length() % 4]);
    }
    long[] data = new long[string.length() / 4];

    byte[] part = new byte[8];
    for (int k = 0, i = 0; i < string.length(); i += 4, k++) {
        String sequence = string.substring(i, i + 4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(sequence.getBytes("ISO-8859-1"));
            System.arraycopy(baos.toByteArray(), 0, part, 4, 4);
            ByteBuffer bb = ByteBuffer.wrap(part);
            data[k] = bb.getLong();
        } catch (IOException e) {
            data[k] = 0;
        }
    }
    return data;
}

//helper function which also contains a loop and is used in the problematic loop, so might be a problem though I don't know how to speed it up
public static String a32_to_str(long[] data) {
    byte[] part = null;
    StringBuilder builder = new StringBuilder();
    ByteBuffer bb = ByteBuffer.allocate(8);
    for (int i = 0; i < data.length; i++) {
        bb.putLong(data[i]);
        part = copyOfRange(bb.array(), 4, 8);
        bb.clear();
        ByteArrayInputStream bais = new ByteArrayInputStream(part);
        while (bais.available() > 0) {
            builder.append((char) bais.read());
        }
    }
    return builder.toString();
}
