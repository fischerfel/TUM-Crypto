var encrypted_str = "N0dHcFM3MnQrcW1HUk9UTGwxeUJsZmlCNzcwUGhrdUdtbE9YWnUxamZFST0tLUVUcUlIU2k1ZHIvTmlDRUgzM2FsS0E9PQ==--1ede80eb2b498ddf5133f8f3a45a82db2476c740"

val parts = encrypted_str.split("--");

val encryptedData = Base64.decodeBase64(parts(0))

val iv = Base64.decodeBase64(parts(1))

val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(iv.take(16)));

val result = cipher.doFinal(encryptedData);
println(new String(result, "UTF-8"))
