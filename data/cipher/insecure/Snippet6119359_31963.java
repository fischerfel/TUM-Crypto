Cipher cipher = Cipher.getInstance("BlowFish/ECB/PKCS5Padding");

byte[] keyBytes = Encoding.ASCII.GetBytes(key);
        string keyHex = Hex.ToHexString(keyBytes); //4b334c33315551354f38325059344739

        string parameters = "{\"userId\":\"6440870\"}";
        byte[] parametersByte = Encoding.ASCII.GetBytes(parameters);
        string parametersHex = Hex.ToHexString(parametersByte); //7b22757365724964223a2236343430383730227d

        BlowFish bl = new BlowFish(keyHex);
        byte[] outputEncryptedByte = bl.Encrypt_ECB(parametersByte);
        string outputEncrypted = Encoding.ASCII.GetString(outputEncryptedByte); //7lC[t$?mQd?g???kE?W?[?
        string outputBase64 = System.Convert.
