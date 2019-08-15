public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return bytes;
    }

public String bytetoString(byte[] input) {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(input);
    }

public byte[] getHashWithSalt(String input, HashingTechqniue technique, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(technique.value);
        digest.reset();
        digest.update(salt);
        byte[] hashedBytes = digest.digest(stringToByte(input));
        return hashedBytes;
    }
public byte[] stringToByte(String input) {
        if (Base64.isBase64(input)) {
            return Base64.decodeBase64(input);

        } else {
            return Base64.encodeBase64(input.getBytes());
        }
    }
