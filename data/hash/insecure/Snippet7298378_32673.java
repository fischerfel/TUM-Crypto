        BufferedImage clipImage1 = (BufferedImage) clipboard
            .getData(DataFlavor.imageFlavor);
    RenderedImage renderclipImage1 = createImage(clipImage1);
    File clipImage1png = new File("clipImage1.png");
    ImageIO.write(renderclipImage1, "png", clipImage1png);
    byte[] clipeImage1Bytes = bufImageToBytesConverter(clipImage1);
    MessageDigest mdInst1 = MessageDigest.getInstance("MD5");
    mdInst1.update(clipeImage1Bytes);
    byte[] md5hashClipImage1 = mdInst1.digest();
    System.out.println(returnHex(md5hashClipImage1));

    BufferedImage clipImage2 = (BufferedImage) clipboard
            .getData(DataFlavor.imageFlavor);
    RenderedImage renderclipImage2 = createImage(clipImage2);
    File clipImage2png = new File("clipImage2.png");
    ImageIO.write(renderclipImage2, "png", clipImage2png);
    byte[] clipImage2Bytes = bufImageToBytesConverter(clipImage2);
    MessageDigest msInst2 = MessageDigest.getInstance("MD5");
    msInst2.update(clipImage2Bytes);
    byte[] md5hashClipImage2 = msInst2.digest();
    System.out.println(returnHex(md5hashClipImage2));
