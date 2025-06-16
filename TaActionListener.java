import java.awt.event.*;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

public class TaActionListener implements KeyListener, CaretListener {
    private Notepad notepad;
    private JTextArea textarea;
    private JLabel statusLabel;
    private JLabel positionLabel;

    public TaActionListener(Notepad notepad) {
        this.notepad = notepad;
        this.textarea = notepad.textarea;
        this.statusLabel = notepad.statusLabel;
        this.positionLabel = notepad.positionLabel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        updateStatus();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        updateStatus();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        updateStatus();
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        updateStatus();
    }

    private void updateStatus() {
        String text = textarea.getText();
        int charCount = text.length();
        int wordCount = text.trim().isEmpty() ? 0 : text.trim().split("\\s+").length;
        
        int lineNum = 1;
        int column = 0;
        try {
            int caretPos = textarea.getCaretPosition();
            lineNum = textarea.getLineOfOffset(caretPos) + 1;
            column = caretPos - textarea.getLineStartOffset(lineNum - 1) + 1;
        } catch (Exception ex) {
            // ignore
        }
        // Update status bar
        statusLabel.setText(String.format("Characters: %d | Words: %d", charCount, wordCount));
        positionLabel.setText(String.format("Line: %d, Column: %d", lineNum, column));
    }
}
