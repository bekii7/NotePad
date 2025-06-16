import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FindReplaceDialog extends JDialog {
    private JTextField findField;
    private JTextField replaceField;
    private JButton findButton;
    private JButton replaceButton;
    private JButton replaceAllButton;
    private JCheckBox matchCaseCheckBox;
    private JCheckBox wholeWordCheckBox;
    private JTextArea textArea;
    private int lastFindPosition = 0;

    public FindReplaceDialog(Frame parent, JTextArea textArea) {
        super(parent, "Find and Replace", true);
        this.textArea = textArea;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(400, 200);
        setLocationRelativeTo(getOwner());

        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Find panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Find:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        findField = new JTextField(20);
        mainPanel.add(findField, gbc);

        // Replace panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Replace with:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        replaceField = new JTextField(20);
        mainPanel.add(replaceField, gbc);

        // Options panel
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        matchCaseCheckBox = new JCheckBox("Match case");
        wholeWordCheckBox = new JCheckBox("Whole word");
        optionsPanel.add(matchCaseCheckBox);
        optionsPanel.add(wholeWordCheckBox);
        mainPanel.add(optionsPanel, gbc);

        // Buttons panel
        gbc.gridy = 3;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        findButton = new JButton("Find");
        replaceButton = new JButton("Replace");
        replaceAllButton = new JButton("Replace All");
        buttonPanel.add(findButton);
        buttonPanel.add(replaceButton);
        buttonPanel.add(replaceAllButton);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Add action listeners
        findButton.addActionListener(e -> find());
        replaceButton.addActionListener(e -> replace());
        replaceAllButton.addActionListener(e -> replaceAll());

        // Add key listener for Enter key
        findField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    find();
                }
            }
        });
    }

    private void find() {
        String searchText = findField.getText();
        if (searchText.isEmpty()) return;

        String content = textArea.getText();
        if (!matchCaseCheckBox.isSelected()) {
            searchText = searchText.toLowerCase();
            content = content.toLowerCase();
        }

        int startIndex = content.indexOf(searchText, lastFindPosition);
        if (startIndex == -1) {
            // If not found from current position, start from beginning
            startIndex = content.indexOf(searchText);
            if (startIndex == -1) {
                JOptionPane.showMessageDialog(this, "Text not found", "Find", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        // Select the found text
        textArea.setSelectionStart(startIndex);
        textArea.setSelectionEnd(startIndex + searchText.length());
        lastFindPosition = startIndex + 1;
    }

    private void replace() {
        String searchText = findField.getText();
        String replaceText = replaceField.getText();
        
        if (textArea.getSelectedText() != null && 
            textArea.getSelectedText().equals(searchText)) {
            textArea.replaceSelection(replaceText);
        }
        find(); // Find next occurrence
    }

    private void replaceAll() {
        String searchText = findField.getText();
        String replaceText = replaceField.getText();
        if (searchText.isEmpty()) return;

        String content = textArea.getText();
        if (!matchCaseCheckBox.isSelected()) {
            searchText = searchText.toLowerCase();
            content = content.toLowerCase();
        }

        int count = 0;
        int startIndex = 0;
        while ((startIndex = content.indexOf(searchText, startIndex)) != -1) {
            textArea.replaceRange(replaceText, startIndex, startIndex + searchText.length());
            startIndex += replaceText.length();
            count++;
        }

        JOptionPane.showMessageDialog(this, 
            "Replaced " + count + " occurrences", 
            "Replace All", 
            JOptionPane.INFORMATION_MESSAGE);
    }
} 