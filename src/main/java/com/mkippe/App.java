package com.mkippe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class App {

    static String userHome = System.getProperty("user.home");

    //adding logging for easier debugging
    private static final Logger logger = Logger.getLogger(String.valueOf(App.class));

    static Path thumbnailsDir = Path.of(userHome).resolve(".photos");

    public static void main(String[] args) throws IOException {

        //checks if the directory exists
        Files.createDirectories(thumbnailsDir);

        //this sets the directory dynamically
        String directory = args.length == 1 ? args[0] : ".";
        Path sourceDir = Path.of(directory);

        //initializes the counter
        AtomicInteger counter = new AtomicInteger();
        //gets the initial time is ms
        long start = System.currentTimeMillis();

        //assuming CPU heavy workload means number of threads = available processors
        //we are going to test if this is the most optimal way
        final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        //walk through the entire file tree or stream
        //why a try catch block?
        try (
                //returns a Java Stream, since it sequentially walks through our src directory
                Stream<Path> files = Files.walk(sourceDir)
                        .filter(Files::isRegularFile) //filter regular images & ignore directories & non images
                        .filter(App::isImage)) {
            //for each file we want to run the createThumbnail method
            // how Lambda expressions work? my guess is they are adding functional programming syntax in Java
            files.forEach(f -> {
                executorService.submit(() -> {
                    counter.incrementAndGet();
                    new ImageMagick().createThumbnail(f, thumbnailsDir.resolve(f.getFileName()));
                });
            });
        }

        executorService.shutdown();

        long end = System.currentTimeMillis();
        //adding logging to see what is going on inside our app
        logger.info("Converted " + counter + "images to thumbnails. Took" + ((end - start) * 0.001) + "seconds");

    }

    //checks the type of file if it's an image
    private static boolean isImage(Path path) {
        try {
            String mimeType = Files.probeContentType(path);
            return mimeType != null && mimeType.contains("image");
        } catch (IOException e) {
            return false;
        }
    }


}