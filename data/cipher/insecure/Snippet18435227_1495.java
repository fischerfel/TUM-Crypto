>>> from javax.crypto import Cipher
>>> Cipher.getMaxAllowedKeyLength("AES")
128
>>> from java.lang import Class
>>> c = Class.forName("javax.crypto.JceSecurity")
>>> isRestricted = c.getDeclaredField("isRestricted")
>>> isRestricted.setAccessible(True)
>>> isRestricted.set(None, False)
>>> isRestricted.get(None)
False
>>> Cipher.getMaxAllowedKeyLength("AES")
128
>>> from javax.crypto import KeyGenerator
>>> kge = KeyGenerator.getInstance("AES")
>>> kge.init(256)
>>> aesKey = kgen.generateKey()
>>> c2 = Cipher.getInstance("AES")
>>> c2.init(Cipher.ENCRYPT_MODE, aesKey)
>>> c2.doFinal("test")
array('b', [-81, 99, -61, -51, 93, -42, -68, -28, 107, 59, -109, -98, -25, 127, 37, 23])
