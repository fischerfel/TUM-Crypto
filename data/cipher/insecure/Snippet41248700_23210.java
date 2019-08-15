// function in Java that I need
// javax.crypto.Cipher.getInstance("Blowfish/CBC/NoPadding").doFinal("spamshog")


var iv_vector = "2278dc9wf_178703";
var txtToEncrypt = "spamshog";
var bf = new Blowfish("spamshog", "cbc");

var encrypted = bf.encrypt(txtToEncrypt, iv_vector);

console.log(bf.base64Encode(encrypted));

Actual output: /z9/n0FzBJQ=
 What I need: /z9/n0FzBJRGS6nPXso5TQ==
