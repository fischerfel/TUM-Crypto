public static byte[] Cipher(int mode, byte[] key, 
       byte[] data, string algorithm, AlgorithmParameterSpec spec)
{
  Cipher cipher = Cipher.getInstance(algorithm);
  SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);

  if (spec != null)
     cipher.init(mode, keySpec, spec);
  else
     cipher.init(mode, keySpec);

   return cipher.doFinal(data);
}
