package com.javarush.task.task31.task3110;

import com.javarush.task.task31.task3110.exception.PathIsNotFoundException;
import com.javarush.task.task31.task3110.exception.WrongZipFileException;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
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

    public void removeFile(Path path) throws Exception {
        removeFiles(Collections.singletonList(path));
    }

    public void removeFiles(List<Path> pathList) throws Exception {
        checkArchiveExistence();
        Path temp = Files.createTempFile(null, null);
        try (
                ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(zipFile));
                ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(temp))
        ) {
            for (ZipEntry entry = zipIn.getNextEntry(); entry != null; entry = zipIn.getNextEntry()) {
                Path filePath = Paths.get(entry.getName());
                if (pathList.contains(filePath)) {
                    ConsoleHelper.writeMessage(String.format("%s удалён из архива.", entry.getName()));
                } else {
                    zipOut.putNextEntry(new ZipEntry(entry.getName()));
                    copyData(zipIn, zipOut);
                    zipOut.closeEntry();
                }
            }
        }
        Files.copy(temp, zipFile, StandardCopyOption.REPLACE_EXISTING);
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
