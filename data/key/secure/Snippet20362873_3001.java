public void getMessage(String serverResponse){
JSONObject obj = new JSONObject(serverResponse);

JSONArray AesKeyArray = obj.getJSONArray("Key");
JSONArray IVArray = obj.getJSONArray("IV");
JSONArray MessageArray = obj.getJSONArray("Message");

// convert to java byteArrays
//**so here could be another part where i failed - as mentioned above**:
// c# byte goes from 0 to 255, whereas java byte goes from -128-127, so i just subtracted //128 from each c# byte, to get the java byte
//is this the correct conversation????

byte[] AesKeyBytes = JsonArrayToByteArray(AesKeyArray);
byte[] IVBytes = JsonArrayToByteArray(IVArray);
byte[] Messagebytes = JsonArrayToByteArray(MessageArray);

Log.i("demo", "log start");
Log.i("demo", "IVByte: " + showByteArray(IVBytes));
Log.i("demo", "KeyByte: " + showByteArray(AesKeyBytes));
Log.i("demo", "Encrypted MessageBytes: " + showByteArray(Messagebytes));

byte[] unencryptedMessageBytes = AESDecrypt(AesKeyBytes, IVBytes,Messagebytes);

Log.i("demo", "Unencrypted Messagebytes: " + showByteArray(unencryptedMessageBytes));

}

private byte[] JsonArrayToByteArray(JSONArray arr) throws Exception {
        byte[] result = new byte[arr.length()];
        for (int i = 0; i < arr.length(); i++) {
    int byteAsIntCSharp = (Integer) arr.get(i); // have as integer 0-255(c# style)
        int byteAsIntJava = byteAsIntCSharp - 128;// make it javastyle(-128 to 127
            byte byteJava = (byte) byteAsIntJava;

            result[i] = byteJava;

        }

        return result;
    }

private String showByteArray(byte[] arr) {
        String s = "";
        for (byte b : arr) {
            s += String.valueOf(b) + "|";
        }
        return s;
}

private byte[] AESDecrypt(byte[] keyBytes, byte[] IVBytes, byte[] input)
            throws Exception {
        // Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");

        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, "AES"),
                new IvParameterSpec(IVBytes));
        byte[] decrptedBytes = new byte[input.length];
        cipher.update(input, 0, input.length, decrptedBytes, 0);
        int radomPT = cipher.doFinal(decrptedBytes, 0);
        return decrptedBytes;
    }
