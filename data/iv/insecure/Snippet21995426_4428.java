String decodee = "1234";
String key = "oY3[r.Ri4oF";
String iv = "\0\0\0\0\0\0\0\0";

SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "Blowfish");
IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());

Cipher cipher = Cipher.getInstance("Blowfish/CFB/NoPadding");
cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

byte[] res = cipher.doFinal(decodee.getBytes());
String s = new String(res);  
int[] x = new int[s.length()]; for (int i = 0 ; i < s.length() ; i++) x[i] = (int) s.charAt(i);
System.out.println(Arrays.toString(x));
