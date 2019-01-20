package com.javarush.task.task31.task3110;

import com.javarush.task.task31.task3110.exception.PathIsNotFoundException;
import com.javarush.task.task31.task3110.exception.WrongZipFileException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFileManager {
    private Path zipFile;

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;

    }

    public void createZip(Path source) throws Exception {
        createDirectoriesIfNotExist(zipFile.getParent());
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

    public List<FileProperties> getFilesList() throws Exception {
        checkArchiveExistence();
        List<FileProperties> properties = new ArrayList<>();
        try (ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(zipFile))) {
            for (ZipEntry entry = zipIn.getNextEntry(); entry != null; entry = zipIn.getNextEntry()) {
                copyData(zipIn, new ByteArrayOutputStream());
                properties.add(
                        new FileProperties(
                                entry.getName(),
                                entry.getSize(),
                                entry.getCompressedSize(),
                                entry.getMethod()
                        )
                );
            }
        }
        return properties;
    }

    public void extractAll(Path outputFolder) throws Exception {
        checkArchiveExistence();
        createDirectoriesIfNotExist(outputFolder);
        try (ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(zipFile))) {
            for (ZipEntry entry = zipIn.getNextEntry(); entry != null; entry = zipIn.getNextEntry()) {
                Path fullFilePath = outputFolder.resolve(entry.getName());
                createDirectoriesIfNotExist(fullFilePath.getParent());
                try (OutputStream fos = Files.newOutputStream(fullFilePath)) {
                    copyData(zipIn, fos);
                }
            }
        }
    }

    private void createDirectoriesIfNotExist(Path directoryPath) throws IOException {
        if (Files.notExists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
    }

    private void checkArchiveExistence() throws Exception {
        if (!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
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
