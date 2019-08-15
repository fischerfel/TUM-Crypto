public class EncryptDocument {

    private ARCFour rc4 = new ARCFour();
    public static final byte[] ENCRYPT_PADDING = new byte[]{(byte)40, (byte)-65, (byte)78, (byte)94, (byte)78, (byte)117, (byte)-118, (byte)65, (byte)100, (byte)0, (byte)78, (byte)86, (byte)-1, (byte)-6, (byte)1, (byte)8, (byte)46, (byte)46, (byte)0, (byte)-74, (byte)-48, (byte)104, (byte)62, (byte)-128, (byte)47, (byte)12, (byte)-87, (byte)-2, (byte)100, (byte)83, (byte)105, (byte)122};
    byte[] userPass;
    byte[] ownerPass;
    String destPath;
    int revision;
    int version;
    private static final byte[] pad = new byte[]{(byte)40, (byte)-65, (byte)78, (byte)94, (byte)78, (byte)117, (byte)-118, (byte)65, (byte)100, (byte)0, (byte)78, (byte)86, (byte)-1, (byte)-6, (byte)1, (byte)8, (byte)46, (byte)46, (byte)0, (byte)-74, (byte)-48, (byte)104, (byte)62, (byte)-128, (byte)47, (byte)12, (byte)-87, (byte)-2, (byte)100, (byte)83, (byte)105, (byte)122};
    private int keyLength;


    public EncryptDocument(byte[] userPass, byte[] ownerPass, String destPath,int keylength) {
        this.userPass = userPass;
        this.ownerPass = ownerPass;
        this.destPath = destPath;
        this.keyLength=keylength;
    }

    public byte[][] computeKeys(byte[] userPass, byte[] ownPass, int permissions, byte[] documentId) {
        //pad both user and owner pass
        byte[] userPad = padPassword(userPass);
        byte[] ownerPad = padPassword(ownPass);
        byte[][] data = new byte[2][32];
        byte[] userKey = new byte[0];
        byte[] encryptionKey;
        permissions= computePermissions(permissions);
        byte[] ownerKey  = new byte[0];
        try {
            ownerKey = computeOwnerKey(userPad, ownerPad);
            encryptionKey = computeEncryptionKey(userPad,ownerKey,documentId,permissions);
            userKey = computeUserKey(userPad,ownerKey,permissions,documentId,encryptionKey);

        } catch (IOException e) {
            e.printStackTrace();
        }
        data[0]=ownerKey;
        data[1]=userKey;
        return data;
    }

    private byte[] computeUserKey(byte[] userPad, byte[] ownerKey, int permissions, byte[] documentId, byte[] mkey) throws IOException {
        //algorithm 5
        byte[] digest;
        ByteArrayOutputStream userKey = new ByteArrayOutputStream();

        ARCFour rc4 = new ARCFour();

        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(pad); // step b
        digest = md5.digest(documentId); //c
        //build the input of rc4 encryption
        userKey.write(digest);
        byte[] iterationKey = new byte[mkey.length];
        //encrypt it first time plus other 19 times
        for (int i = 0; i < 20; ++i) {
            System.arraycopy(mkey, 0, iterationKey, 0, iterationKey.length);
            for (int input = 0; input < iterationKey.length; ++input) {
                iterationKey[input] = (byte) (iterationKey[input] ^ i);
            }
            rc4.setKey(iterationKey);
            ByteArrayInputStream tmpRes = new ByteArrayInputStream(userKey.toByteArray());
            userKey.reset();
            rc4.write(tmpRes, userKey);
        }
        return userKey.toByteArray();
    }

    private byte[] computeEncryptionKey(byte[] userPad,byte[] ownerKey, byte[] documentId,int permissions){
        //initialize hash function
        MessageDigest md5 = null;
        byte[] encryptedKey =new byte[keyLength / 8];
        byte[] digest;
        try {
            md5 = MessageDigest.getInstance("MD5"); //md5.reset()
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        md5.update(userPad);///pass the padded user password (step b)
        md5.update(ownerKey); //pass o entry to the hash function (step c)

        byte[] ext = int_to_bb_le(permissions); //new byte[]{(byte)permissions, (byte)(permissions >> 8), (byte)(permissions >> 16), (byte)(permissions >> 24)};

        md5.update(ext, 0, 4); //pass permission to the hash function stp d
        md5.update(documentId); //pass first element of document id (step e)
        if (this.revision>=4)
        md5.update(new byte[]{(byte)-1, (byte)-1, (byte)-1, (byte)-1}); //metadata not encryped --> add padding  (step f)
        digest = md5.digest();
        //compute encryption key step g


        if(revision == 3 || revision == 4) { //do 50 times step h
            for(int k = 0; k < 50; ++k) {
                md5.reset();
                md5.update(digest,0,keyLength / 8);
                digest=md5.digest();
            }
        }
        System.arraycopy(digest, 0, encryptedKey, 0, encryptedKey.length);
        return encryptedKey;
    }

    private byte[] computeOwnerKey(byte[] userPad, byte[] ownerPad) throws IOException {

        MessageDigest md5 = null;
        ARCFour rc4 = new ARCFour();

        try {
            md5 = MessageDigest.getInstance("MD5"); //md5.reset()
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] digest = md5.digest(ownerPad);

        byte[] mkey = new byte[this.keyLength / 8];
        int i;
        for (i = 0; i < 50; ++i) {
            md5.update(digest, 0, mkey.length);
            System.arraycopy(md5.digest(), 0, digest, 0, mkey.length);
        }
        //encrypt the padded user password using the result of md5 computation on owner padded password
        //revision >3 ==> do it 19 times
        ByteArrayOutputStream ownerKey = new ByteArrayOutputStream();
        //System.arraycopy(userPad, 0, ownerKey, 0, 32);
        rc4.write(new ByteArrayInputStream(userPad), ownerKey);
        byte[] iterationKey = new byte[mkey.length];
            for (i = 0; i < 20; ++i) {
                System.arraycopy(mkey, 0, iterationKey, 0, mkey.length);
                //prepare encryption key  bitwhise xor between encrypted owner padded password and iteration counter
                for (int j = 0; j < iterationKey.length; ++j) {
                    iterationKey[j] = (byte) (digest[j] ^ i);
                }
            //encrypt with arc4
            rc4.setKey(iterationKey);
            ByteArrayInputStream tmpres = new ByteArrayInputStream(ownerKey.toByteArray());
            ownerKey.reset();
            rc4.write(tmpres, ownerKey);
        }
        //at the 19 invocation the own key is obtained
        return ownerKey.toByteArray();
    }

    public int computePermissions(int permissions){
        permissions |= this.revision != 3 && this.revision != 4 && this.revision != 5?-64:-3904;
        permissions &= -4;
        return permissions;
    }

    public byte[] padPassword(byte[] password) {
        byte[] userPad = new byte[32];
        if(password == null) {
            System.arraycopy(pad, 0, userPad, 0, 32);
        } else {
            System.arraycopy(password, 0, userPad, 0, Math.min(password.length, 32));
            if(password.length < 32) {
            System.arraycopy(pad, 0, userPad, password.length, 32 - password.length);
            }
        }
        return userPad;
    }
    public static  byte[] int_to_bb_le(int myInteger){
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(myInteger).allocatelocaterray();
     }
}
