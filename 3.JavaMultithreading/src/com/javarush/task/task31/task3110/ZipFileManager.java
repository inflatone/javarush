package com.javarush.task.task31.task3110;

import com.javarush.task.task31.task3110.exception.PathIsNotFoundException;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileManager {
    private Path zipFile;

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;

    }

    public void createZip(Path source) throws Exception {
        Path zipDirectory = zipFile.getParent();
        if (Files.notExists(zipDirectory)) {
            Files.createDirectories(zipDirectory);
        }
        try (
                ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile))
        ) {
            if (Files.isRegularFile(source)) {
                addNewZipEntry(zipOutputStream, source.getParent(), source.getFileName());
            } else if (Files.isDirectory(source)) {
                List<Path> fileNames = new FileManager(source).getFileList();
                for (Path file : fileNames) {
                    addNewZipEntry(zipOutputStream, source, file);
                }
            } else {
                throw new PathIsNotFoundException();
            }
        }
    }

    private void addNewZipEntry(ZipOutputStream zipOutputStream, Path filePath, Path fileName) throws Exception {
        try (
                InputStream inputStream = Files.newInputStream(filePath.resolve(fileName));
        ) {
            ZipEntry zipEntry = new ZipEntry(fileName.toString());
            zipOutputStream.putNextEntry(zipEntry);
            copyData(inputStream, zipOutputStream);
            zipOutputStream.closeEntry();

        }
    }

    private void copyData(InputStream in, OutputStream out) throws Exception {
        byte[] buffer = new byte[8 * 1024];
        for (int len = in.read(buffer); len > 0; len = in.read(buffer)) {
            out.write(buffer, 0, len);
        }
    }
}
