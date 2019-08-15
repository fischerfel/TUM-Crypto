public static void main(String... args) throws Exception {
    String masterPassword = "password";
    String base64EncodedKey = "U2FsdGVkX18QAut2uv9ElKWb2T8QycYS4k0PgjwUtWW2NaBlEmolBwNhPkYDvg986QOYqDs/LVC7mnUUQbrRkbn0sLBbLQ/+oMjvzBpcBRIPqBnQ7bKZjPXZIBDt+15KSRWGSFAnrXVL6Udcecu7t8WNrcUoXy9bgjRpwCzqaMuVRik3sBQNmNbb/4rbf9+NM6tNEZ4KsoHBkcXj4DofzISOBQhmpjfqagjb057vb/8+Pi5cpRl4sO3GPX0Xu8XVTQsLffm3oTTywG9V+2XwXMfvb34SXrJPOkxlHBqjw2GqNx4bhz9buCoLzUFrFfTEeB+gg2yEwFONWhPtV7D/MYAOrZB8P0RaL7oQh1mAnx9ESlFJ5V6VzwbFZGxj7QeaEWG47HFBdSfSuAdibs7N+GtAFv2lQQnEK+li0+rwEYZ+AKeohqe3A6yZDGYpkGvGFr4EDsWEHnYtWl8yL6guLELJxbLXZ8HLLE+W4Sut9l69OS5QeqgLJcuvtw0l1psmrsx8SA/NyFfGNfQE4xi7RPg8qhR3ardEiG6O+IhMMD80PBHFqKZy0jLPRbZA42oApF7isbm4OUQBBrObOpuLMwTCrsMD6DrUUUy1wwd3Ij35mvF7xcJwiYGuE/B3Eqk2UDfDlMXeiWurPJ+px1ie9ZhdfXCTYJOUuRFB25fw0aXFVKO40yrlHoi69B0P2HffO2uH2bzhWQ7KOAqjRyo6mtPn5Xsd+T4rZKWNoWt+fjmLbx+evdDCiF1E/2k8oHAlrvp65DVFhbK69BKFTuMdqr+3sSX9Jl1KPuvJP9fWIk85ncF/K5BbiZ9d47pBt8DFEf+lDhCBSemM0Tjn8fSG6oDlqpdIl7ZXWc/5NhdE6idMQm9bOPbtZWIBM5kJqLpOHLt37vslZFwNv5sq0c2ULwGaBeP1WHfpE0PcaBTTweP3T56ufzo1Larsgw/X3VIXfKM7ZULgbPMQU0SxxBj//jDZJrm+8e782LJJTIMGkcs6B/AOuZCtZVe5gYz1Wlk3JGg+sinBslLyVSP7hFaKHji9KUoBhmyVJajiBYrEVdGLKdyWE7FbGQOZ5rSVNE5kLJ3TBhaejjpHvlJMuGa1WtBmVZTaq2E5ax0wShJixfm+QZMTEhq1ryM3pdsxg0ul1GMLs9x+WRps/F8YHBilwYKEAbqVJxnTRjl8vmqDUS+VrNRYkRHP982DwikzOpTtdlN6+PMta53bw3Q6Qxiiaxhl4SuPYwsV9XcAuDNYu7NcFY+2/IiMJl/ZhLQUXLsbnDT9jUbSi5HVO3W1m65mXX+O/0mFeWmIcf2Ch5aBRhxAtup74V/vKGNVUNY8vU43KrEKFpbD5vc54UY6itQZef3VM+VNqJzQrI9Ka3wKIaY7IWN87KZ/1Odw1ZjdmNQX";
    String base64EncodedPassword = "U2FsdGVkX1+H71VA6yn6cA/A/ojEP2lwCUcorrz8CqkffRHSghm/Q0pDsf/p/FTw"; //value is 'password'
    int iterations = 100000;

    Base64.Decoder base64Decoder = Base64.getDecoder();

    byte[] decodedKey = base64Decoder.decode(base64EncodedKey);
    byte[] salt = Arrays.copyOfRange(decodedKey, 8, 16);
    byte[] keyData = Arrays.copyOfRange(decodedKey, 16, decodedKey.length);


    KeySpec spec = new PBEKeySpec(masterPassword.toCharArray(), salt, iterations, 32 * 8);
    SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    SecretKey secretKey = f.generateSecret(spec);

    byte[] rawKey = secretKey.getEncoded();
    byte[] aesRaw = Arrays.copyOfRange(rawKey, 0, 16);
    byte[] aesIv = Arrays.copyOfRange(rawKey, 16, 32);

    SecretKeySpec aesKey = new SecretKeySpec(aesRaw, "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(aesIv));

    byte[] decryptedKey = cipher.doFinal(keyData);

    byte[] decodedPassword = base64Decoder.decode(base64EncodedPassword);
    byte[] passwordSalt = Arrays.copyOfRange(decodedPassword, 8, 16);
    byte[] passwordData = Arrays.copyOfRange(decodedPassword, 16,decodedPassword.length);
    // hmm...
}
