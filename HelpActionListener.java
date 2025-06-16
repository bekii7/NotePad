import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class HelpActionListener implements ActionListener {
    private Notepad notepad;
    private static final String VERSION = "1.0.0";
    private static final String APP_NAME = "Java Notepad";

    public HelpActionListener(Notepad notepad) {
        this.notepad = notepad;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        switch (event.getActionCommand()) {
            case "About":
                showAboutDialog();
                break;
            case "Keyboard Shortcuts":
                showKeyboardShortcutsDialog();
                break;
            default:
                break;
        }
    }

    private void showAboutDialog() {
        JDialog aboutDialog = new JDialog(notepad, "About " + APP_NAME, true);
        aboutDialog.setLayout(new BorderLayout());
        aboutDialog.setSize(400, 300);
        aboutDialog.setLocationRelativeTo(notepad);
        aboutDialog.setResizable(false);

        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // App name and version
        JLabel appNameLabel = new JLabel(APP_NAME);
        appNameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        appNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(appNameLabel);

        mainPanel.add(Box.createVerticalStrut(10));

        JLabel versionLabel = new JLabel("Version " + VERSION);
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(versionLabel);

        mainPanel.add(Box.createVerticalStrut(20));

        // Description
        JTextArea description = new JTextArea(
            "A modern text editor built with Java Swing.\n\n" +
            "Features:\n" +
            "• Text editing with undo/redo\n" +
            "• Find and replace functionality\n" +
            "• File operations (new, open, save)\n" +
            "• Text formatting options\n" +
            "• Word wrap and line numbers\n" +
            "• Character and word count\n\n" +
            "© 2024 Java Notepad. All rights reserved."
        );
        description.setEditable(false);
        description.setBackground(mainPanel.getBackground());
        description.setFont(new Font("Arial", Font.PLAIN, 12));
        description.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(description);

        // Keyboard Shortcuts button
        JButton shortcutsButton = new JButton("View Keyboard Shortcuts");
        shortcutsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        shortcutsButton.addActionListener(e -> {
            aboutDialog.dispose();
            showKeyboardShortcutsDialog();
        });
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(shortcutsButton);

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> aboutDialog.dispose());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(closeButton);

        aboutDialog.add(mainPanel, BorderLayout.CENTER);
        aboutDialog.setVisible(true);
    }

    private void showKeyboardShortcutsDialog() {
        JDialog shortcutsDialog = new JDialog(notepad, "Keyboard Shortcuts", true);
        shortcutsDialog.setLayout(new BorderLayout());
        shortcutsDialog.setSize(600, 500);
        shortcutsDialog.setLocationRelativeTo(notepad);
        shortcutsDialog.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Keyboard Shortcuts");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Create a scroll pane for the shortcuts
        JPanel shortcutsPanel = new JPanel();
        shortcutsPanel.setLayout(new BoxLayout(shortcutsPanel, BoxLayout.Y_AXIS));
        
        // Add shortcuts with section headers
        addShortcutSection(shortcutsPanel, "File Operations", new String[][] {
            {"Ctrl + N", "New File"},
            {"Ctrl + O", "Open File"},
            {"Ctrl + S", "Save"},
            {"Ctrl + Shift + S", "Save As"},
            {"Ctrl + P", "Print"},
            {"Alt + F4", "Close"},
            {"Ctrl + W", "Close Current File"}
        });

        addShortcutSection(shortcutsPanel, "Edit Operations", new String[][] {
            {"Ctrl + Z", "Undo"},
            {"Ctrl + Y", "Redo"},
            {"Ctrl + X", "Cut"},
            {"Ctrl + C", "Copy"},
            {"Ctrl + V", "Paste"},
            {"Delete", "Delete"},
            {"Ctrl + A", "Select All"},
            {"Alt + F", "Insert Time/Date"},
            {"Ctrl + F", "Find"},
            {"Ctrl + H", "Replace"}
        });

        addShortcutSection(shortcutsPanel, "Text Formatting", new String[][] {
            {"Ctrl + =", "Increase Font Size"},
            {"Ctrl + -", "Decrease Font Size"},
            {"Ctrl + B", "Bold"},
            {"Ctrl + I", "Italic"},
            {"Ctrl + U", "Underline"},
            {"Ctrl + L", "Align Left"},
            {"Ctrl + E", "Align Center"},
            {"Ctrl + R", "Align Right"}
        });

        addShortcutSection(shortcutsPanel, "View Options", new String[][] {
            {"Ctrl + W", "Toggle Word Wrap"},
            {"Ctrl + L", "Toggle Line Numbers"},
            {"Ctrl + D", "Toggle Status Bar"},
            {"Ctrl + M", "Toggle Dark Mode"}
        });

        addShortcutSection(shortcutsPanel, "Navigation", new String[][] {
            {"Home", "Start of Line"},
            {"End", "End of Line"},
            {"Ctrl + Home", "Start of Document"},
            {"Ctrl + End", "End of Document"},
            {"Ctrl + Up", "Scroll Up"},
            {"Ctrl + Down", "Scroll Down"},
            {"Ctrl + G", "Go to Line"}
        });

        // Add the shortcuts panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(shortcutsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane);

        // Add a note about customization
        JLabel noteLabel = new JLabel("Note: Some shortcuts may vary depending on your system settings");
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        noteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(noteLabel);

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> shortcutsDialog.dispose());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(closeButton);

        shortcutsDialog.add(mainPanel, BorderLayout.CENTER);
        shortcutsDialog.setVisible(true);
    }

    private void addShortcutSection(JPanel panel, String title, String[][] shortcuts) {
        // Add section header
        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(headerLabel);
        panel.add(Box.createVerticalStrut(5));

        // Create a panel for the shortcuts in this section
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));
        sectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add each shortcut
        for (String[] shortcut : shortcuts) {
            JPanel shortcutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            shortcutPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel keyLabel = new JLabel(shortcut[0]);
            keyLabel.setFont(new Font("Monospaced", Font.BOLD, 12));
            keyLabel.setForeground(new Color(0, 102, 204)); // Blue color for keys
            
            JLabel descLabel = new JLabel(shortcut[1]);
            descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            
            shortcutPanel.add(keyLabel);
            shortcutPanel.add(Box.createHorizontalStrut(20));
            shortcutPanel.add(descLabel);
            sectionPanel.add(shortcutPanel);
        }

        panel.add(sectionPanel);
        panel.add(Box.createVerticalStrut(10));
    }
}
