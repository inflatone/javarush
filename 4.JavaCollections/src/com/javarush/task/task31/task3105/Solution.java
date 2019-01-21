package com.javarush.task.task31.task3105;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/* 
Добавление файла в архив
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        String fileAbsolutePath = args[0];
        String archiveAbsolutePath = args[1];
        Map<String, byte[]> entries = getEntriesFromArchive(archiveAbsolutePath);
        String newFileEntryName = "new" + fileAbsolutePath.substring(fileAbsolutePath.lastIndexOf('/'));
        entries.remove(newFileEntryName);

        ZipOutputStream zipOS = new ZipOutputStream(new FileOutputStream(archiveAbsolutePath));
        zipOS.putNextEntry(new ZipEntry(newFileEntryName));
        Files.copy(Paths.get(fileAbsolutePath), zipOS);
        for (Map.Entry<String, byte[]> entry : entries.entrySet()) {
            zipOS.putNextEntry(new ZipEntry(entry.getKey()));
            zipOS.write(entry.getValue());
        }
        zipOS.close();
    }

    private static Map<String, byte[]> getEntriesFromArchive(String archiveAbsolutePath) throws IOException {
        Map<String, byte[]> result = new HashMap<>();
        ByteArrayOutputStream arrayOS = new ByteArrayOutputStream();
        ZipInputStream zipIS = new ZipInputStream(new FileInputStream(archiveAbsolutePath));
        for (ZipEntry entry = zipIS.getNextEntry(); entry != null; entry = zipIS.getNextEntry()) {
            copy(zipIS, arrayOS);
            result.put(entry.getName(), arrayOS.toByteArray());
            arrayOS.reset();
        }
        zipIS.close();
        arrayOS.close();
        return result;
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[8 * 1024];
        for (int len = in.read(buffer); len > 0; len = in.read(buffer)) {
            out.write(buffer, 0, len);
        }
    }
}
