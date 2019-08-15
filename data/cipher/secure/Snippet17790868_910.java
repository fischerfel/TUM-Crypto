//get private key 
String privateKeyString = "MIICWwIBAAKBgQDVIJ8H3Oszc5fWdgpwymWksF1WxkXJHIfdS6Ta1bHeqwEzPIkN f3iVk14LfaoSZpRb9Yvi/jvkXxIzJbHq6aKfnQOC6tKIiixvVvpCfxr1eV4urDdz H9RNy9bqGdXzTQdgQi+KRx0Dcy9RNsl7ZGLAGrUFRnPI4GTdH+7wm4QogQIDAQAB AoGAcUcKX7KC7HDm5h0NRY+94H/AzItLsi3Q5MT81Tc5d+EqHSJysdLrs4yFMtRS 3b7Z4dqrxDVefe4uDTNe0j3lqboKz8oKwAU+paKx3wubHb7aeQnfzwM9mPQJHgEO zBjlvbL4oEa/gklu3VohZAc1daqpPajdWuOQQp4S+jUllrECQQDrITlSjvkxt8ud /vYIcEXHew3iW4nzaAH3z4PRAGZofRpk/OusGZ6cdZoYMTZcdxYTCCbZ5eeyGukW 5QCadie1AkEA6Atx8Z0F7WhLI2lGvCGy+vIOL0vBDZSma0cvLYLAXMx8duoWQ9J2 LwT7SsnRXMeq/8wlNHL7mFEf+YFZBKKlHQJAO78kfrr/zUdjwREBWaGVyZuWKpeS FTyvi1W6rAgK/bAUXeb6x69241DqyAzxQEuuW0WuAZ5u4o39/qhQH++4JQJAAepe RW1TaDNNM3yh/dmVXabz4QYSEOeiPA55YDnNFrcFbAHgryyklxzGakaiOM7ZJYVs 5TLxyr8YsXmU34nsLQJALzC8CaFXJcnU0+6+KoKX7iq1aP3X4LgP4Gianix6pfRo aV8UHnfFLRSgPdn1ZYmKtJfnsJXJYoE+o9xEErb5EQ==";

// converts the String to a PublicKey instance
KeyFactory keyFactory = KeyFactory.getInstance("RSA");
PrivateKey key = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(privateKeyString.toString(), Base64.DEFAULT)));

// encrypts the message
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, key);
byte[] encrpytedText = cipher.doFinal(Base64.encode(phoneUid.getBytes("CP1252"), Base64.DEFAULT));
data = new String(encrpytedText, "CP1252");
