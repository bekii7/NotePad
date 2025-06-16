import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewActionListener implements ActionListener {

    private Notepad notepad;
    // Variables to store the status of word wrap and line wrap
    private boolean wWrap, lWrap, sDetails;

    public ViewActionListener(Notepad notepad) {
        this.notepad = notepad;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        switch (event.getActionCommand()) {
            case "Word Wrap":
                wWrap = notepad.wordWrap.isSelected();
                notepad.textarea.setLineWrap(wWrap);
                notepad.textarea.setWrapStyleWord(wWrap);
                break;

            case "Line Numbers":
                lWrap = notepad.lineWrap.isSelected();
                // TODO: Implement line numbers display
                break;

            case "Status Bar":
                sDetails = notepad.showDetails.isSelected();
                notepad.statusBar.setVisible(sDetails);
                break;

            default:
                break;
        }
    }
}
