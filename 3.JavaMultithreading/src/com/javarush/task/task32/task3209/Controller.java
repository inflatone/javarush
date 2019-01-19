package com.javarush.task.task32.task3209;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;

public class Controller {
    private View view;
    private HTMLDocument document;

    private File currentFile;

    public Controller(View view) {
        this.view = view;
    }

    public static void main(String[] args) {
        View view = new View();
        Controller controller = new Controller(view);
        view.setController(controller);
        view.init();
        controller.init();
    }

    public HTMLDocument getDocument() {
        return document;
    }


    public void init() {
        createNewDocument();
    }

    public void exit() {
        System.exit(0);
    }

    public void resetDocument() {
        if (document != null) {
            document.removeUndoableEditListener(view.getUndoListener());
        }
        document = (HTMLDocument) new HTMLEditorKit().createDefaultDocument();
        document.addUndoableEditListener(view.getUndoListener());
        view.update();
    }

    public String getPlainText() {
        String result = "";
        try (StringWriter writer = new StringWriter()) {
            new HTMLEditorKit().write(writer, document, 0, document.getLength());
            result = writer.toString();
        } catch (IOException | BadLocationException e) {
            ExceptionHandler.log(e);
        }
        return result;
    }

    public void setPlainText(String text) {
        resetDocument();
        try (StringReader reader = new StringReader(text)) {
            new HTMLEditorKit().read(reader, document, 0);
        } catch (IOException | BadLocationException e) {
            ExceptionHandler.log(e);
        }
    }

    public void createNewDocument() {
        view.selectHtmlTab();
        resetDocument();
        view.setTitle("HTML редактор");
        view.resetUndo();
        currentFile = null;
    }

    public void openDocument() {
        view.selectHtmlTab();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new HTMLFileFilter());
        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            resetDocument();
            view.resetUndo();
            currentFile = fileChooser.getSelectedFile();
            view.setTitle(currentFile.getName());
            openFromCurrentFile();
        }
    }

    public void saveDocument() {
        if (currentFile == null) {
            saveDocumentAs();
        } else {
            view.selectHtmlTab();
            saveToCurrentFile();
        }
    }

    public void saveDocumentAs() {
        view.selectHtmlTab();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new HTMLFileFilter());
        if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            view.setTitle(currentFile.getName());
            saveToCurrentFile();
        }
    }

    private void saveToCurrentFile() {
        try (FileWriter writer = new FileWriter(currentFile)) {
            new HTMLEditorKit().write(writer, document, 0, document.getLength());
        } catch (IOException | BadLocationException e) {
            ExceptionHandler.log(e);
        }
    }

    private void openFromCurrentFile() {
        try (FileReader reader = new FileReader(currentFile)) {
            new HTMLEditorKit().read(reader, document, 0);
        } catch (IOException | BadLocationException e) {
            ExceptionHandler.log(e);
        }
    }
}