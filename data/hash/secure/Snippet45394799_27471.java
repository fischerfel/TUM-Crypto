package com.thecompany.test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Program {
    public static void main(final String[] args) throws NoSuchAlgorithmException {
        // Input.
        final String input = "1234nonce=1234";

        // Processing.
        final byte[] output = sha256(input);

        // Output.
        System.out.printf("Signed output: %s.%n", Arrays.toString(output));

        final List<Integer> unsignedOutput = unsignedIntStream(output)
            .boxed()
            .collect(Collectors.toList());
        System.out.printf("Unsigned output: %s.%n", unsignedOutput);
    }

    private static IntStream unsignedIntStream(final byte[] array) {
        return IntStream
            .range(0, array.length)
            .map(index -> Byte.toUnsignedInt(array[index]));
    }

    private static byte[] sha256(final String text) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(text.getBytes(StandardCharsets.UTF_8));
    }
}
