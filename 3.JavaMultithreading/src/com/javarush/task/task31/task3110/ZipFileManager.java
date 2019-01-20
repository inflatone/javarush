package com.javarush.task.task31.task3110;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileManager {
    private Path zipFile;

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;
    }

    public void createZip(Path source) throws Exception {
        try (
                ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile));
                InputStream in = Files.newInputStream(source)
        ) {
            ZipEntry entry = new ZipEntry(source.getFileName().toString());
            zipOutputStream.putNextEntry(entry);
            byte[] buffer = new byte[8 * 1024];
            for (int len = in.read(buffer); len > 0; len = in.read(buffer)) {
                zipOutputStream.write(buffer, 0, len);
            }
            zipOutputStream.closeEntry();
        }
    }
}
