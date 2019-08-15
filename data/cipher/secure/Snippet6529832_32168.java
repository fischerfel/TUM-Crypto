RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(
new BigInteger("00c897f9e401819e223ffbecc6f715a8d84dce9022762e0e2d54fa434787fcaf230d28bd0c3b6b39b5211f74ffc4871c421362ccfc07ae98b88fa9728f1e26b8210ebbf4981e45867fe810938294d0095d341b646b86dcbd4c246676c203cb1584d01eef0635299714d94fa12933ecd35e6c412573156d9e6e549b7804eb6e165660507d8748bcc8c60da10099bacb94d3f7b50b1883ee108489e0dd97ed7d28e564edd4ee5d6b4225f5c23cdaaf495c3fa08c3b82e1674946e4fa1e79b2493204d6953c261105ba5d0f8dcf3fcd39a51fbc18a5f58ffff169b1bed7ceeded2ae0e8e8e2238e8b77b324d1a482593b1a642e688c860e90d5a3de8515caf384133b", 16),
new BigInteger("11", 16));
keyFactory = KeyFactory.getInstance("RSA", "BC");
//RSAPublicKeySpec rsaKeySpec = new RSAPublicKeySpec(rsaKey.MODULUS, new BigInteger("11", 16));
RSAPublicKey pubKey = (RSAPublicKey)keyFactory.generatePublic(pubKeySpec);  

//Set up the cipher to RSA encryption
Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
cipher.init(Cipher.ENCRYPT_MODE, pubKey);

// make sure the Aes Key is less than a block size
// otherwise major errors will occur
if(AesKey.length * 8 > pubKey.getModulus().bitLength())
    return "Error: AesKey bigger than block size of RSA Key";

byte[] encryptedKey = cipher.doFinal(AesKey);

// return result Base64 encoded
return Base64.encodeToString(encryptedKey, Base64.DEFAULT);
