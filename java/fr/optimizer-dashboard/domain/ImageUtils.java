package fr.ifpen.synergreen.domain;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileCacheImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ImageUtils {

    private final Logger log = LoggerFactory.getLogger(ImageUtils.class);

    public static String resizeImage(final String image, final int maxSize) {
        if (StringUtils.isNotBlank(image)) {

            final BufferedImage bi;
            try {
                bi = ImageUtils.decodeToImage(image);
                if (bi != null) {
                    if (bi.getHeight() > maxSize || bi.getWidth() > maxSize) {
                        return ImageUtils.encodeToString(Scalr.resize(bi, Scalr.Method.AUTOMATIC, maxSize));
                    } else {
                        return ImageUtils.encodeToString(bi);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return image;
    }

    /**
     * Converti une chaine Base 64 en {@link BufferedImage}
     *
     * @param imageString
     * @return Retourne une image décodée
     * @throws IOException
     */
    public static BufferedImage decodeToImage(final String imageString) throws IOException {
        final BufferedImage image;

        final byte[] imageByte = getImageInBytes(imageString).getContent();
        try (final ByteArrayInputStream bis = new ByteArrayInputStream(imageByte)) {
            image = ImageIO.read(bis);
        }
        return image;
    }

    public static String encodeToString(final BufferedImage image) throws IOException {
        final String imageString;
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (final ImageOutputStream imageOutputStream = new FileCacheImageOutputStream(bos, null)) {
            writeJpgImage(image, imageOutputStream);
        }
        final byte[] imageBytes = bos.toByteArray();
        imageString = "data:image/jpeg;base64," + Base64.encodeBase64String(imageBytes);
        return imageString;
    }

    /**
     * Extrait depuis la chaine de caractère passée en paramètre: le content type et l'image sous forme binaire
     *
     * @param imageString image html encodée sous forme d'une chaine base64: data:image/png;base64,...
     * @return
     */
    public static Base64Image getImageInBytes(final String imageString) {
        final Base64Image img = new Base64Image();

        // On supprime la partie de type "data:image/png;base64,"
        final int idx = imageString.indexOf(";base64,");

        if (imageString.startsWith("data:")) {
            img.setContentType(imageString.substring(5, idx));
        }
        final String content = imageString.substring(idx + 8);
        img.setContent(Base64.decodeBase64(content));

        return img;
    }

    public static void writeJpgImage(final BufferedImage input, final ImageOutputStream outputStream) throws IOException {
        BufferedImage noAlphaImage;
        if (input.getColorModel().getTransparency() != Transparency.OPAQUE) {
            noAlphaImage = fillTransparentPixels(input, Color.WHITE);
        } else {
            noAlphaImage = input;
        }

        final ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        try {
            final ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
            jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpgWriteParam.setCompressionQuality(0.5f);
            jpgWriter.setOutput(outputStream);
            final IIOImage outputImage = new IIOImage(noAlphaImage, null, null);
            jpgWriter.write(null, outputImage, jpgWriteParam);
        } finally {
            jpgWriter.dispose();
        }
    }

    public static BufferedImage fillTransparentPixels(final BufferedImage image, final Color fillColor) {
        final int w = image.getWidth();
        final int h = image.getHeight();
        final BufferedImage image2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = image2.createGraphics();
        g.setColor(fillColor);
        g.fillRect(0, 0, w, h);
        g.drawRenderedImage(image, null);
        g.dispose();
        return image2;
    }

    /**
     * Classe représentant les informations extraites d'une image encodée en Base64
     */
    public static class Base64Image {

        private String contentType;
        private byte[] content;

        public String getContentType() {
            return contentType;
        }

        public void setContentType(final String contentType) {
            this.contentType = contentType;
        }

        public byte[] getContent() {
            return content;
        }

        public void setContent(final byte[] content) {
            this.content = content;
        }
    }


}
