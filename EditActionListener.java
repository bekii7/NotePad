import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditActionListener implements ActionListener {

    Notepad notepad;

    public EditActionListener(Notepad notepad) {
        this.notepad = notepad;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        // Switch statement to handle different edit actions
        switch (event.getActionCommand()) {
            case "Undo":
                if (notepad.undoManager.canUndo()) // Check if undo is possible
                    notepad.undoManager.undo(); // Perform undo
                break;

            case "Redo":
                if (notepad.undoManager.canRedo()) // Check if redo is possible
                    notepad.undoManager.redo(); // Perform redo
                break;

            case "Cut":
                notepad.textarea.cut(); // Cut the selected text
                break;

            case "Copy":
                notepad.textarea.copy(); // Copy the selected text
                break;

            case "Paste":
                notepad.textarea.paste(); // Paste the text from clipboard
                break;

            case "Delete":
                notepad.textarea.replaceSelection(""); // Delete the selected text
                break;

            case "Select All":
                notepad.textarea.selectAll(); // Select all text in the textarea
                break;

            case "Time/Date":
                // Append the current time and date to the text area
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                notepad.textarea.insert(now.format(format), notepad.textarea.getCaretPosition());
                break;

            case "Find":
                FindReplaceDialog findDialog = new FindReplaceDialog(notepad, notepad.textarea);
                findDialog.setVisible(true);
                break;

            case "Replace":
                FindReplaceDialog replaceDialog = new FindReplaceDialog(notepad, notepad.textarea);
                replaceDialog.setVisible(true);
                break;

            default:
                break;
        }
    }
}
