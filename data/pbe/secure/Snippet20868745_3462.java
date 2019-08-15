public String Padding_key() {

  try {

    PBEKeySpec pbeKeySpec = new PBEKeySpec(STReditTxtPass.toCharArray());
    Log.e("PBEkEYsPEC", pbeKeySpec.toString());
    Toast.makeText(this, "step 1", Toast.LENGTH_SHORT).show();
    Cipher cipher = Cipher.getInstance("AES");
    Toast.makeText(this, "after ciphering", Toast.LENGTH_SHORT).show();
    SecretKeyFactory keyFactory = SecretKeyFactory
    .getInstance("PBEWithMD5AndDES");
    Toast.makeText(this, "after keyFactory", Toast.LENGTH_SHORT).show();

    SecretKey pbeKey = keyFactory.generateSecret(pbeKeySpec);
    Log.e("PBEkEYsPEC", pbeKey.toString());
    Toast.makeText(this, "after SecreteKey", Toast.LENGTH_SHORT).show();

    PBEParameterSpec pbeSpec = new PBEParameterSpec(salt, iterations);
    Log.e("PBEkEYsPEC", pbeSpec.toString());
    Toast.makeText(this, "after PBEParameterSpec", Toast.LENGTH_SHORT).show();
    cipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeSpec);

    Toast.makeText(this, "after cypher.init", Toast.LENGTH_SHORT).show();

    byte[] cipherText = cipher.doFinal(PlainText.getBytes("UTF-8"));
    Toast.makeText(this, "after byte[]", Toast.LENGTH_SHORT).show();

    cyphertext = String.format("%s%s%s", toBase64(salt), "]",
    toBase64(cipherText));
    Toast.makeText(this, "after cypherText.format", Toast.LENGTH_SHORT).show();

    edit_txt_enc_string.setText(cyphertext);

    strPaddingencryption = edit_txt_enc_string.getText().toString();

  } catch (Exception e) {

  }
  return strPaddingencryption;
}
