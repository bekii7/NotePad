import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultEditorKit;

public class FormatActionListener implements ActionListener {
    private Notepad notepad;
    private JDialog fontDialog;
    private JList<String> fontList;
    private JList<String> styleList;
    private JList<Integer> sizeList;
    private JTextArea previewArea;
    private Font currentFont;

    public FormatActionListener(Notepad notepad) {
        this.notepad = notepad;
        this.currentFont = notepad.textarea != null ? 
            notepad.textarea.getFont() : 
            new Font("Arial", Font.PLAIN, 12);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        
        if (command.equals("Font")) {
            showFontDialog();
        } else if (command.startsWith("fsize-")) {
            int size = Integer.parseInt(command.substring(6));
            updateFont(size);
        } else if (command.equals("Bold")) {
            updateFontStyle(Font.BOLD);
        } else if (command.equals("Italic")) {
            updateFontStyle(Font.ITALIC);
        } else if (command.startsWith("fcolorfg-")) {
            if (command.equals("fcolorfg-Custom")) {
                Color color = JColorChooser.showDialog(notepad, "Choose Text Color", 
                    notepad.textarea.getForeground());
                if (color != null) {
                    notepad.textarea.setForeground(color);
                }
            } else {
                int index = Integer.parseInt(command.substring(9));
                notepad.textarea.setForeground(notepad.awtColors[index]);
            }
        } else if (command.startsWith("fcolorbg-")) {
            if (command.equals("fcolorbg-Custom")) {
                Color color = JColorChooser.showDialog(notepad, "Choose Background Color", 
                    notepad.textarea.getBackground());
                if (color != null) {
                    notepad.textarea.setBackground(color);
                }
            } else {
                int index = Integer.parseInt(command.substring(9));
                notepad.textarea.setBackground(notepad.awtColors[index]);
            }
        }
    }

    private void showFontDialog() {
        if (fontDialog == null) {
            fontDialog = new JDialog(notepad, "Font", true);
            fontDialog.setLayout(new BorderLayout());
            fontDialog.setSize(500, 400);
            fontDialog.setLocationRelativeTo(notepad);

            // Create panels
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Font list panel
            JPanel fontPanel = new JPanel(new BorderLayout());
            fontPanel.setBorder(BorderFactory.createTitledBorder("Font"));
            fontList = new JList<>(notepad.fonts);
            fontList.setSelectedValue(currentFont.getFamily(), true);
            fontPanel.add(new JScrollPane(fontList), BorderLayout.CENTER);

            // Style and size panel
            JPanel styleSizePanel = new JPanel(new GridLayout(2, 1, 0, 10));
            
            // Style list
            JPanel stylePanel = new JPanel(new BorderLayout());
            stylePanel.setBorder(BorderFactory.createTitledBorder("Style"));
            String[] styles = {"Regular", "Bold", "Italic", "Bold Italic"};
            styleList = new JList<>(styles);
            styleList.setSelectedIndex(0);
            stylePanel.add(new JScrollPane(styleList), BorderLayout.CENTER);

            // Size list with custom size input
            JPanel sizePanel = new JPanel(new BorderLayout());
            sizePanel.setBorder(BorderFactory.createTitledBorder("Size"));
            
            // Create a panel for the size list and custom size input
            JPanel sizeInputPanel = new JPanel(new BorderLayout(5, 0));
            
            // Size list
            Integer[] sizes = new Integer[notepad.fontSizes.length];
            for (int i = 0; i < notepad.fontSizes.length; i++) {
                sizes[i] = notepad.fontSizes[i];
            }
            sizeList = new JList<>(sizes);
            sizeList.setSelectedValue(currentFont.getSize(), true);
            sizeInputPanel.add(new JScrollPane(sizeList), BorderLayout.CENTER);
            
            // Custom size input
            JPanel customSizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            customSizePanel.add(new JLabel("Custom:"));
            JTextField customSizeField = new JTextField(4);
            customSizeField.setHorizontalAlignment(JTextField.RIGHT);
            customSizePanel.add(customSizeField);
            customSizePanel.add(new JLabel("pt"));
            
            // Add custom size button
            JButton applyCustomSize = new JButton("Apply");
            applyCustomSize.addActionListener(e -> {
                try {
                    int size = Integer.parseInt(customSizeField.getText());
                    if (size > 0 && size <= 200) {
                        updateFont(size);
                        sizeList.setSelectedValue(size, true);
                    } else {
                        JOptionPane.showMessageDialog(fontDialog, 
                            "Please enter a size between 1 and 200", 
                            "Invalid Size", 
                            JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(fontDialog, 
                        "Please enter a valid number", 
                        "Invalid Input", 
                        JOptionPane.WARNING_MESSAGE);
                }
            });
            customSizePanel.add(applyCustomSize);
            
            sizeInputPanel.add(customSizePanel, BorderLayout.SOUTH);
            sizePanel.add(sizeInputPanel, BorderLayout.CENTER);

            styleSizePanel.add(stylePanel);
            styleSizePanel.add(sizePanel);

            // Preview panel with more sample text
            JPanel previewPanel = new JPanel(new BorderLayout());
            previewPanel.setBorder(BorderFactory.createTitledBorder("Preview"));
            previewArea = new JTextArea(
                "AaBbYyZz\n" +
                "The quick brown fox jumps over the lazy dog\n" +
                "0123456789\n" +
                "!@#$%^&*()_+-=[]{}|;:,.<>?"
            );
            previewArea.setEditable(false);
            previewArea.setLineWrap(true);
            previewArea.setWrapStyleWord(true);
            previewPanel.add(new JScrollPane(previewArea), BorderLayout.CENTER);

            // Add panels to main panel
            mainPanel.add(fontPanel, BorderLayout.WEST);
            mainPanel.add(styleSizePanel, BorderLayout.CENTER);
            mainPanel.add(previewPanel, BorderLayout.SOUTH);

            // Button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton resetButton = new JButton("Reset to Default");
            JButton okButton = new JButton("OK");
            JButton cancelButton = new JButton("Cancel");
            
            // Add keyboard shortcuts
            KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
            KeyStroke enterKeyStroke = KeyStroke.getKeyStroke("ENTER");
            KeyStroke ctrlBKeyStroke = KeyStroke.getKeyStroke("control B");
            KeyStroke ctrlIKeyStroke = KeyStroke.getKeyStroke("control I");
            
            Action escapeAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fontDialog.dispose();
                }
            };
            Action enterAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateFont();
                    fontDialog.dispose();
                }
            };
            
            fontDialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(escapeKeyStroke, "ESCAPE");
            fontDialog.getRootPane().getActionMap().put("ESCAPE", escapeAction);
            
            fontDialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(enterKeyStroke, "ENTER");
            fontDialog.getRootPane().getActionMap().put("ENTER", enterAction);
            
            // Add action listeners
            resetButton.addActionListener(e -> {
                currentFont = new Font("Arial", Font.PLAIN, 12);
                updateFont();
                fontList.setSelectedValue("Arial", true);
                styleList.setSelectedIndex(0);
                sizeList.setSelectedValue(12, true);
                updatePreview();
            });
            
            okButton.addActionListener(e -> {
                updateFont();
                fontDialog.dispose();
            });

            cancelButton.addActionListener(e -> fontDialog.dispose());

            // Add change listeners for live preview
            ListSelectionListener selectionListener = e -> updatePreview();
            fontList.addListSelectionListener(selectionListener);
            styleList.addListSelectionListener(selectionListener);
            sizeList.addListSelectionListener(selectionListener);

            buttonPanel.add(resetButton);
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);

            fontDialog.add(mainPanel, BorderLayout.CENTER);
            fontDialog.add(buttonPanel, BorderLayout.SOUTH);
        }

        fontDialog.setVisible(true);
    }

    private void updatePreview() {
        if (fontList.getSelectedValue() != null && 
            styleList.getSelectedValue() != null && 
            sizeList.getSelectedValue() != null) {
            
            int style = Font.PLAIN;
            String selectedStyle = styleList.getSelectedValue();
            if (selectedStyle.contains("Bold")) style |= Font.BOLD;
            if (selectedStyle.contains("Italic")) style |= Font.ITALIC;

            Font previewFont = new Font(
                fontList.getSelectedValue(),
                style,
                sizeList.getSelectedValue()
            );
            previewArea.setFont(previewFont);
        }
    }

    private void updateFont() {
        if (fontList.getSelectedValue() != null && 
            styleList.getSelectedValue() != null && 
            sizeList.getSelectedValue() != null) {
            
            int style = Font.PLAIN;
            String selectedStyle = styleList.getSelectedValue();
            if (selectedStyle.contains("Bold")) style |= Font.BOLD;
            if (selectedStyle.contains("Italic")) style |= Font.ITALIC;

            currentFont = new Font(
                fontList.getSelectedValue(),
                style,
                sizeList.getSelectedValue()
            );
            if (notepad.textarea != null) {
                notepad.textarea.setFont(currentFont);
            }
        }
    }

    private void updateFont(int size) {
        currentFont = currentFont.deriveFont((float)size);
        if (notepad.textarea != null) {
            notepad.textarea.setFont(currentFont);
        }
    }

    private void updateFontStyle(int style) {
        int newStyle = currentFont.getStyle() ^ style;
        currentFont = currentFont.deriveFont(newStyle);
        if (notepad.textarea != null) {
            notepad.textarea.setFont(currentFont);
        }
    }
}
