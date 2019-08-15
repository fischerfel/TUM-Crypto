/*
 Copyright (c) 2010, Sungjin Han <meinside@gmail.com>
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

  * Redistributions of source code must retain the above copyright notice,
    this list of conditions and the following disclaimer.
  * Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
  * Neither the name of meinside nor the names of its contributors may be
    used to endorse or promote products derived from this software without
    specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.
 */

//package org.andlib.helpers;
public static KeyPair generateRsaKeyPair(int keySize, BigInteger publicExponent)
{
KeyPair keys = null;
try
{
  KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
  RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(keySize, publicExponent);
  keyGen.initialize(spec);
  keys = keyGen.generateKeyPair();
}
catch(Exception e)
{
//  Logger.e(e.toString());
}
return keys;


}



/**
   * generates a RSA public key with given modulus and public exponent
   * 
   * @param modulus (must be positive? don't know exactly)
   * @param publicExponent
   * @return
   */
  public static PublicKey generateRsaPublicKey(BigInteger modulus, BigInteger publicExponent)
  {
    try
    {
      return KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus,     publicExponent));
    }
    catch(Exception e)
    {
    //  Logger.e(e.toString());
    }
    return null;
  }

  /**
   * generates a RSA private key with given modulus and private exponent
   * 
   * @param modulus (must be positive? don't know exactly)
   * @param privateExponent
   * @return
   */
  public static PrivateKey generateRsaPrivateKey(BigInteger modulus, BigInteger privateExponent)
  {
    try
    {
      return KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateKeySpec(modulus, privateExponent));
    }
    catch(Exception e)
    {
    //  Logger.e(e.toString());
    }
    return null;
  }

  /**
   * RSA encrypt function (RSA / ECB / PKCS1-Padding)
   * 
   * @param original
   * @param key
   * @return
   */
  public static byte[] rsaEncrypt(byte[] original, PublicKey key)
  {
    try
    {
      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      cipher.init(Cipher.ENCRYPT_MODE, key);
      return cipher.doFinal(original);
    }
    catch(Exception e)
    {
    //  Logger.e(e.toString());
    }
    return null;
  }

  /**
   * RSA decrypt function (RSA / ECB / PKCS1-Padding)
   * 
   * @param encrypted
   * @param key
   * @return
   */
  public static byte[] rsaDecrypt(byte[] encrypted, PrivateKey key)
  {
    try
    {
      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      cipher.init(Cipher.DECRYPT_MODE, key);
      return cipher.doFinal(encrypted);
    }
    catch(Exception e)
    {
    //  Logger.e(e.toString());
    }
    return null;
  }
