java -version ; \
echo 'System.err.println(javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding").getMaxAllowedKeyLength("AES"));'  \
| java -cp /usr/share/java/bsh-*.jar bsh.Interpreter >/dev/null
