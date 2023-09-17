package com.mkippe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ImageMagick {

    private static final Logger logger = Logger.getLogger(String.valueOf(ImageMagick.class));

    public int run(String... cmds) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(cmds);
        builder.inheritIO();
        Process process = builder.start();
        boolean finished = process.waitFor(1, TimeUnit.SECONDS);
        if (!finished) {
            process.destroy();
        }
        return process.exitValue();
    }

    public void createThumbnail(Path source, Path target) {
        try {
            //logging to see how many threads are working off the images and what they're respective names are
            logger.info(Thread.currentThread().getName() + " -----> Creating thumbnail: "
                    + target.normalize().toAbsolutePath());

            List<String> cmd = new ArrayList<>(List.of("convert", "-resize", "300x",
                    source.normalize().toAbsolutePath().toString(),
                    target.normalize().toAbsolutePath().toString()));

            if (detectVersion() == Version.IM_7) {
                cmd.add(0, "magick");
            }

            //handling a process running a command for us
            ProcessBuilder builder = new ProcessBuilder(cmd);

            //configure our process before executing it
            builder.inheritIO();

            //initializes the process to start
            Process process = builder.start();

            //give each process a time boundary
            boolean finished = process.waitFor(3, TimeUnit.SECONDS);

            if (!finished) {
                //kill the process after its finished
                process.destroy();
            }

        } catch (IOException | InterruptedException e) {
            logger.info("failed to convert images because of: " + e);
            e.printStackTrace();
        }
    }

    public Version detectVersion() {
        try {
            int exitCode = run("convert", "--version");
            if (exitCode == 0) {
                return Version.IM_6;
            }

            exitCode = run("magick", "--version");
            if (exitCode == 0) {
                return Version.IM_7;
            }
            return Version.NA;
        } catch (Exception e) {
            e.printStackTrace();
            return Version.NA;
        }
    }

    public enum Version {
        NA, IM_6, IM_7
    }
}
