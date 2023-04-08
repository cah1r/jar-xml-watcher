package dev.cah1r.JarXmlWatcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
class FileManager {
    private static final String HOME_PATH = "src/main/resources/home";
    private static final String DEV_PATH = "src/main/resources/dev";
    private static final String TEST_PATH = "src/main/resources/test";
    private int allFilesCounter = 0;
    private int devCounter = 0;
    private int testCounter = 0;

    FileManager() {
        new File(HOME_PATH).mkdirs();
        new File(DEV_PATH).mkdirs();
        new File(TEST_PATH).mkdirs();
    }

    void fileChecker() {
        listFilesInHomeDir().forEach(file -> {
            if (shouldBeInDev.test(file)) {
                file.renameTo(new File(DEV_PATH + "/" + file.getName()));
                log.info("Moved file {} to [dev] folder", file.getName());
                log.info("Files moved to [dev] folder -> {}", ++devCounter);
                log.info("All files moved -> {}", ++allFilesCounter);
                file.delete();
            } else if (shouldBeInTest.test(file)) {
                file.renameTo(new File(TEST_PATH + "/" + file.getName()));
                log.info("Moved file {} to [test] folder", file.getName());
                log.info("Files moved to [test] folder -> {}", ++testCounter);
                log.info("All files moved -> {}", ++allFilesCounter);
                file.delete();
            }
            writeCounters();
        });
    }

    private void writeCounters() {
        String str = "dev -> " + devCounter + "\ntest -> " + testCounter + "\nall files -> " + allFilesCounter;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(HOME_PATH + "/counter.txt"));
            writer.write(str);
            writer.close();
        } catch (Exception e) {
            log.error("Error updating counters");
        }
    }

    private final Predicate<File> shouldBeInDev = file ->
            file.getName().endsWith(".xml")
            || file.getName().endsWith(".jar")
                    && (getMinutes(file).isPresent()
                    && ((getMinutes(file).get() % 2) == 0));

    private final Predicate<File> shouldBeInTest = file ->
            file.getName().endsWith(".jar")
                    && getMinutes(file).isPresent()
                    && ((getMinutes(file).get() % 2) != 0);

    private Optional<Integer> getMinutes(File file) {
        try {
            FileTime created = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
            return Optional.of(created.toInstant().atZone(ZoneOffset.UTC).getMinute());
        } catch (Exception e) {
            log.warn("Error moving file. Couldn't get creation time.");
            return Optional.empty();
        }
    }

    private List<File> listFilesInHomeDir() {
        return Stream.of(Objects.requireNonNull(new File(HOME_PATH).listFiles()))
                .filter(file -> !file.isDirectory())
                .filter(f -> !f.getName().equals("count.txt"))
                .collect(Collectors.toList());
    }
}