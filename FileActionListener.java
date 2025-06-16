import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.*;

import javax.swing.JOptionPane;

public class FileActionListener implements ActionListener {

    Notepad notepad;
    // Variables to store the file directory and name
    String fileDirectory, fileName;

    public FileActionListener(Notepad notepad) {
        this.notepad = notepad;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        switch (event.getActionCommand()) {
            case "New":
                newFile();
                break;

            case "Open":
                open();
                break;

            case "Save":
                save();
                break;

            case "Save As":
                saveAs();
                break;

            case "Print":
                try {
                    boolean printed = notepad.textarea.print();
                    if (printed)
                        JOptionPane.showMessageDialog(notepad, "File Printed!");
                } catch (PrinterException e) {
                    System.err.println("Error while printing!");
                }
                break;

            case "Close":
                notepad.dispose();
                break;

            default:
                break;
        }
    }

    // Method to create a new file
    private void newFile() {
        fileName = null; // Reset file name
        fileDirectory = null; // Reset file directory
        notepad.setTitle("Notepad - New"); // Set title to "New"
        notepad.textarea.setText(""); // Clear text area
        notepad.wordCount = 0;
        notepad.undoManager.discardAllEdits(); // Discard all undo edits
    }

    // Method to open an existing file
    private void open() {
        // Create and open a file dialog for loading
        FileDialog fd = new FileDialog(notepad, "Open", FileDialog.LOAD);
        // fd.setFilenameFilter(new FilenameFilter() {
        // @Override
        // public boolean accept(File file, String s) {
        // return s.contains(".txt");
        // }
        // });
        fd.setVisible(true);

        if (fd.getFile() == null)
            return; // If no file is selected, return

        fileDirectory = fd.getDirectory(); // Get the selected file's directory
        fileName = fd.getFile(); // Get the selected file's name
        notepad.setTitle("Notepad - " + fileName); // Set the notepad title to the file name

        try {
            // Read the file content
            BufferedReader reader = new BufferedReader(new FileReader(fileDirectory + fileName));
            notepad.textarea.setText(""); // Clear the text area
            String line;
            while ((line = reader.readLine()) != null)
                notepad.textarea.append(line + "\n"); // Append each line to the text area
            notepad.undoManager.discardAllEdits(); // Discard all undo edits
            notepad.textarea.requestFocus(); // Request focus for the text area
            notepad.textarea.setCaretPosition(notepad.textarea.getDocument().getLength()); // Set the caret position to
                                                                                           // the end
            reader.close();
        } catch (Exception e) {
            System.err.println("Error reading file!");
        }
        // Invoke a keyReleased event manually to update the details label on file open
        notepad.textarea.dispatchEvent(new KeyEvent(
                notepad.textarea, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, 0, 'O'));
    }

    // Method to save the current file
    private void save() {
        if (fileName == null) {
            saveAs(); // If no file name, call saveAs()
            return;
        }

        try {
            // Write the text area content to the file
            FileWriter writer = new FileWriter(fileDirectory + fileName);
            writer.write(notepad.textarea.getText());
            writer.close();
        } catch (Exception e) {
            System.err.println("Error saving file!");
        }
    }

    // Method to save the current file with a new name
    private void saveAs() {
        // Create and open a file dialog for saving
        FileDialog fd = new FileDialog(notepad, "Save As", FileDialog.SAVE);
        fd.setFile(".txt"); // Set default file extension
        fd.setVisible(true);

        if (fd.getFile() == null)
            return; // If no file is selected, return

        fileDirectory = fd.getDirectory(); // Get the selected file's directory
        fileName = fd.getFile(); // Get the selected file's name
        notepad.setTitle("Notepad - " + fileName); // Set the notepad title to the file name

        try {
            // Write the text area content to the file
            FileWriter writer = new FileWriter(fileDirectory + fileName);
            writer.write(notepad.textarea.getText());
            writer.close();
        } catch (Exception e) {
            System.err.println("Error saving file!");
        }
    }
}