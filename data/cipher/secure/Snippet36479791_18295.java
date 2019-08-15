 Public Sub AESCrypt(ByVal password As String)
        ' hash password with SHA-256 and crop the output to 128-bit for key
        Dim digest As MessageDigest = MessageDigest.getInstance("SHA-256")
        digest.update(password.getBytes("UTF-8"))
        Dim keyBytes() As Byte = New Byte((32) - 1) {}
        System.arraycopy(digest.digest, 0, keyBytes, 0, keyBytes.length)
        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        'cipher = Cipher.getInstance("AES/CBC/ZeroBytePadding");
        'cipher = Cipher.getInstance("AES/CBC/NoPadding");
        key = New SecretKeySpec(keyBytes, "AES")
        spec = getIV
    End Sub

    Public Function getIV() As AlgorithmParameterSpec
        Dim iv() As Byte = New Byte() {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        Dim ivParameterSpec As IvParameterSpec
        ivParameterSpec = New IvParameterSpec(iv)
        Return ivParameterSpec
    End Function

    Public Function encrypt(ByVal plainText As String) As String
        cipher.init(Cipher.ENCRYPT_MODE, key, spec)
        Dim encrypted() As Byte = cipher.doFinal(plainText.getBytes("UTF-8"))
        Dim encryptedText As String = New String(Base64.encode(encrypted, Base64.DEFAULT), "UTF-8")
        System.out.println(("Encrypt Data" + encryptedText))
        Return encryptedText
    End Function
