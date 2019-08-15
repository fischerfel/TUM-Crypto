Charset charset = Charset.defaultCharset();
MessageDigest digest = MessageDigest.getInstance("SHA-512");

try (DirectoryStream<Path> dir = Files.newDirectoryStream(
        Paths.get("/online/data/test/output/2Zip"), "brita_ids-*.csv")) {

    for (Path file : dir) {
        Files.lines(file, charset)
            .map(line -> line.split(",")[1])
            .sorted(Collator.getInstance()).distinct()
            .forEach(value -> digest.update(value.getBytes(charset)));
    }
}

byte[] sum = digest.digest();

String outputFile = "/online/data/test/output/file_name.txt";
try (Formatter outputFormatter = new Formatter(outputFile)) {
    for (byte sumByte : sum) {
        outputFormatter.format("%02x", sumByte);
    }
    outputFormatter.format(" *%s%n", outputFile);
}
