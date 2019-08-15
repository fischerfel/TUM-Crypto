password = DESCodec.decode("String Returned by the output of encode")




class DESCodec {
    def static encode = { String target ->
        def cipher = getCipher(Cipher.ENCRYPT_MODE)
        return cipher.doFinal(target.bytes).encodeBase64() as String
    }

    def static decode = { String target ->
        def cipher = getCipher(Cipher.DECRYPT_MODE)
        return new String(cipher.doFinal(target.decodeBase64())) as String
    }

    private static getCipher(mode) {
        def keySpec = new DESKeySpec(getPassword())
        def cipher = Cipher.getInstance("DES")
        def keyFactory = SecretKeyFactory.getInstance("DES")
        cipher.init(mode, keyFactory.generateSecret(keySpec))
        return cipher
    }

    private static getPassword() {
        "testsaltString".getBytes("UTF-8")
    }


    static void main(args) {

        println args
        if(args.length == 1) {
            println encode(args[0])
        } else {

             println decode(args[1])
        }
    }
}
