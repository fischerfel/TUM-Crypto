public class RandomStringGenerator {
    private final int randomLength;
    private final int hashLength;
    private final MessageDigest hasher;

    public RandomStringGenerator(int randomLength, int hashLength, MessageDigest hasher) {
        this.randomLength = randomLength;
        this.hashLength = hashLength;
        this.hasher = hasher;
    }

    public String generate(String base) {
        // the multiplier accounts for the base-64 conversion
        byte[] rand = new byte[randomLength * 3 / 4];
        ThreadLocalRandom.current().nextBytes(rand);
        Base64.Encoder base64 = Base64.getEncoder();

        byte[] front = base64.encode(rand);

        hasher.update(base.getBytes(StandardCharsets.UTF_8));
        hasher.update(front);

        CharSequence hash = base64.encodeToString(hasher.digest()).subSequence(0, hashLength);

        return new String(front, StandardCharsets.UTF_8) + hash;
    }

    public boolean validate(String base, String random) {
        int totalLength = randomLength + hashLength;
        if (random.length() != totalLength)
            return false;

        String front = random.substring(0, randomLength);
        String hash = random.substring(randomLength);

        hasher.update(base.getBytes(StandardCharsets.UTF_8));
        hasher.update(front.getBytes(StandardCharsets.UTF_8));

        String calculatedHash = Base64.getEncoder().encodeToString(hasher.digest());

        return calculatedHash.startsWith(hash);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        MessageDigest hasher = MessageDigest.getInstance("Sha-256");
        RandomStringGenerator gen = new RandomStringGenerator(24, 8, hasher);

        String generated = gen.generate("FooBar");
        System.out.println(generated);

        boolean isValid = gen.validate("FooBar", generated);
        System.out.println("isValid: " + isValid);
    }
}
