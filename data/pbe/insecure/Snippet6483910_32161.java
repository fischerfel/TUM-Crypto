String msg = "This is a message.";
String privpem = "MIICoTAbBgoqhkiG9w0BDAEDMA0ECFavEvdkv3fEAgEUBIICgAWvHvH6OktLiaaqo9v+X6XEuY3M\nZr465VmZWzP9nsbTqwSKQQjseiD/rWAxK7RS+V+hit5ZxlNRAUbkg0kwl8SRNX3v6q8noJtcB0OY\ndBEuNJDmWHMHh8qcnfRYc9WXPPmWdjQM2AkfZNfNOxHVlOMhancScy6P4h3Flri9VyUE8w2/zZqK\nBAd2w39V7gprCQXnnNenNuvr4p8MjsdBm8jh00o2HJzN0I6u+9s7M3qLXxwxNepptgU6Qt6eKHi6\njpsV/musVaohLhFMFAzQ87FeGvz/W8dyS9BtAKMRSuDu/QdWIJMRNKkPT0Tt1243V3tzXVXLjz0u\nm/FX6kfxL8r+eGtTr6NKTG75TJfooQzN/v08OEbmvYD/mfptmZ7uKezOGxDmgynn1Au7T/OxKFhx\nWZHpb9OFPIU0uiriUeyY9sbDVJ054zQ/Zd5+iaIjX5RsLoB4J+pfr4HuiVIZVj+Ss2rnPsOY3SjM\ntbHIFp/fLr/HODcDA5eYADRGpBIL9//Ejgzd7OqpU0mdajzZHcMTjeXfWB0cc769bFyHb3Ju1zNO\ng4gNN1H1kOMAXMF7p6r25f6v1BRS6bQyyiFz7Hs7h7JBylbBAgQJgZvv9Ea3XTMy+DIPMdepqu9M\nXazmmYJCtdLAfLBybWsfSBU5K6Pm6+Bwt6mPsuvYQBrP3h84BDRlbkntxUgaWmTB4dkmzhMS3gsY\nWmHGb1N+rn7xLoA70a3U/dUlI7lPkWBx9Sz7n8JlH3cM6jJUmUbmbAgHiyQkZ2mf6qo9qlnhOLvl\nFiG6AY+wpu4mzM6a4BiGMNG9D5rnNyD16K+p41LsliI/M5C36PKeMQbwjJKjmlmWDX0=";
byte [] privkeybytes = Base64.decode(privpem);

EncryptedPrivateKeyInfo encprivki = new EncryptedPrivateKeyInfo(privkeybytes);

Cipher cipher = Cipher.getInstance(encprivki.getAlgName());
PBEKeySpec pbeKeySpec = new PBEKeySpec("123456".toCharArray());
SecretKeyFactory secFac = SecretKeyFactory.getInstance(encprivki.getAlgName());
Key pbeKey = secFac.generateSecret(pbeKeySpec);
AlgorithmParameters algParams = encprivki.getAlgParameters();
cipher.init(Cipher.DECRYPT_MODE, pbeKey, algParams);
KeySpec pkcs8KeySpec = encprivki.getKeySpec(cipher);
KeyFactory kf = KeyFactory.getInstance("RSA");
PrivateKey pk = kf.generatePrivate(pkcs8KeySpec);

Signature sig = Signature.getInstance("SHA1withRSA");
sig.initSign(pk);
sig.update(msg.getBytes("UTF8"));
byte[] signatureBytes = sig.sign();
String b = Base64.encodeBytes(signatureBytes, Base64.DO_BREAK_LINES);
System.out.println(b); // Display the string.
