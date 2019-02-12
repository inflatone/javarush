package com.javarush.task.task33.task3310.strategy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBucket {
    private Path path;

    public FileBucket() {
        try {
            path = Files.createTempFile(null, null);
            Files.deleteIfExists(path);
            Files.createFile(path);
            path.toFile().deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getFileSize() {
        long result;
        try {
            result = Files.size(path);
        } catch (IOException e) {
            result = 0L;
        }
        return result;
    }

    public void putEntry(Entry entry) {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(path))) {
            out.writeObject(entry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Entry getEntry() {
        Entry result = null;
        if (getFileSize() > 0) {
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
                result = (Entry) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void remove() {
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
