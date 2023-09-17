package com.mkippe;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

class ImageMagickTest {

    private static final Logger logger = Logger.getLogger(String.valueOf(ImageMagickTest.class));

    public ImageMagick imageMagick = new ImageMagick();

    @Test
    void imageMagick_is_installed () {
        //checks if imageMagick is installed first and if it isn't it should the tests after it
        assertThat(imageMagick.detectVersion()).isNotEqualTo(ImageMagick.Version.NA);
    }
    @Test
    @EnabledIfImageMagickIsInstalled
    void thumbnail_creation_works (@TempDir Path testDir) throws IOException {
        //how do we test that a particular method actually works
        Path originalImage = copyTestImageTo(testDir.resolve("large.jpg"));
        Path thumbnail = testDir.resolve("thumbnail.jpg");

        Path targetDir = Paths.get("/Users/mosseskippe/Documents");
        imageMagick.createThumbnail(originalImage,thumbnail);

        // softAssertions does all the tests then combines the output
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(thumbnail).exists();
        softly.assertThat(Files.size(thumbnail)).isLessThan(Files.size(originalImage)/2);
        softly.assertThat(getDimensions(thumbnail).width).isEqualTo(300);
        softly.assertAll();
    }

    private Dimensions getDimensions(Path path) {
        try (InputStream is = Files.newInputStream(path)){
            BufferedImage read = ImageIO.read(is); //what is a bufferedImage? ImageI/O allows you to read images
            return new Dimensions(read.getWidth(), read.getHeight());
        } catch (IOException e) {
            return new Dimensions(-1,-1);
        }
    }

    public record Dimensions(int width, int height) { }

    // helper method that allows us to a reference to our test picture
    private static Path copyTestImageTo(Path targetFile) {
        try (InputStream resourceAsStream = ImageMagick.class.getResourceAsStream("/99.jpg")) {

            assert resourceAsStream != null;
            Files.copy(resourceAsStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
            logger.info("copied test image to: = " + targetFile.toAbsolutePath());
            return targetFile;
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
