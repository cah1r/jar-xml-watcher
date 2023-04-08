package dev.cah1r.JarXmlWatcher;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class FileWatcher {

    private FileManager fm;

    @Scheduled(fixedRate = 5000L)
    void FileWatcher() {
        fm.fileChecker();
    }
}
