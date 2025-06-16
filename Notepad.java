import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;

public class Notepad extends JFrame {

    // GUI components
    protected JTextArea textarea;
    protected JPanel statusBar;
    protected JLabel statusLabel;
    protected JLabel positionLabel;
    private JToolBar toolBar;
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu, viewMenu, formatMenu, helpMenu;
    private JMenuItem newFile, open, save, saveAs, print, close;
    private JMenuItem undo, redo, cut, copy, paste, delete, selectAll, timeDate;
    private JMenuItem find, replace;
    protected JCheckBoxMenuItem wordWrap, lineWrap, showDetails;
    protected JCheckBoxMenuItem bold, italic;
    private JMenuItem formatFont;
    private JMenu formatFontSize, formatColorFg, formatColorBg;
    private JMenuItem about, keyboardShortcuts;

    protected UndoManager undoManager = new UndoManager();
    protected ButtonGroup fontGroup = new ButtonGroup();
    protected ButtonGroup fontSizeGroup = new ButtonGroup();
    protected ButtonGroup colorFgGroup = new ButtonGroup();
    protected ButtonGroup colorBgGroup = new ButtonGroup();

    protected int charCount, wordCount, lineNum = 1;
    protected String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    protected int[] fontSizes = { 12, 14, 16, 18, 20, 24, 28, 36, 48, 56, 64, 72 };
    protected String[] colors = { "Black", "White", "Red", "Blue", "Green" };
    protected Color[] awtColors = { Color.BLACK, Color.WHITE, Color.RED, Color.BLUE, Color.GREEN };

    // Modern color scheme
    private static final Color BACKGROUND_COLOR = new Color(43, 43, 43);
    private static final Color FOREGROUND_COLOR = new Color(169, 183, 198);
    private static final Color SELECTION_COLOR = new Color(33, 66, 131);
    private static final Color TOOLBAR_COLOR = new Color(53, 53, 53);
    private static final Color STATUS_BAR_COLOR = new Color(53, 53, 53);

    public Notepad() {
        setTitle("Modern Notepad");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize components
        initializeComponents();
        setupLayout();
        setupEventListeners();
        
        // Apply modern styling
        applyModernStyling();
    }

    private void initializeComponents() {
        // Create text area first
        textarea = new JTextArea();
        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);
        textarea.setForeground(FOREGROUND_COLOR);
        textarea.setBackground(BACKGROUND_COLOR);
        textarea.setCaretColor(FOREGROUND_COLOR);
        textarea.setSelectionColor(SELECTION_COLOR);
        textarea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textarea.getDocument().addUndoableEditListener(undoManager);

        // Create menu bar
        menuBar = new JMenuBar();
        addMenus();
        addFileMenuItems();
        addEditMenuItems();
        addViewMenuItems();
        addFormatMenuItems();
        addHelpMenuItems();

        // Create toolbar
        createToolbar();

        // Create status bar
        createStatusBar();
    }

    private void createToolbar() {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(TOOLBAR_COLOR);
        toolBar.setBorderPainted(false);

        // Add toolbar buttons with text instead of icons for now
        addToolbarButton("New", e -> new FileActionListener(this).actionPerformed(
            new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "New")));
        addToolbarButton("Open", e -> new FileActionListener(this).actionPerformed(
            new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Open")));
        addToolbarButton("Save", e -> new FileActionListener(this).actionPerformed(
            new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Save")));
        toolBar.addSeparator();
        addToolbarButton("Cut", e -> new EditActionListener(this).actionPerformed(
            new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Cut")));
        addToolbarButton("Copy", e -> new EditActionListener(this).actionPerformed(
            new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Copy")));
        addToolbarButton("Paste", e -> new EditActionListener(this).actionPerformed(
            new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Paste")));
        toolBar.addSeparator();
        addToolbarButton("Find", e -> new EditActionListener(this).actionPerformed(
            new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Find")));
    }

    private void addToolbarButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setToolTipText(text);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(TOOLBAR_COLOR);
        button.setForeground(FOREGROUND_COLOR);
        button.addActionListener(listener);
        toolBar.add(button);
    }

    private void createStatusBar() {
        statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(STATUS_BAR_COLOR);
        statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        statusLabel = new JLabel("Ready");
        statusLabel.setForeground(FOREGROUND_COLOR);
        statusBar.add(statusLabel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(STATUS_BAR_COLOR);
        positionLabel = new JLabel("Line: 1, Column: 1");
        positionLabel.setForeground(FOREGROUND_COLOR);
        rightPanel.add(positionLabel);
        statusBar.add(rightPanel, BorderLayout.EAST);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setJMenuBar(menuBar);
        add(toolBar, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(textarea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }

    private void setupEventListeners() {
        // Add text area listeners
        TaActionListener tal = new TaActionListener(this);
        textarea.addKeyListener(tal);
        textarea.addCaretListener(tal);

        // Add window listener for auto-save
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Add auto-save logic here
            }
        });
    }

    private void applyModernStyling() {
        // Apply modern styling to all components
        UIManager.put("Menu.background", TOOLBAR_COLOR);
        UIManager.put("Menu.foreground", FOREGROUND_COLOR);
        UIManager.put("MenuItem.background", TOOLBAR_COLOR);
        UIManager.put("MenuItem.foreground", FOREGROUND_COLOR);
        UIManager.put("MenuItem.selectionBackground", SELECTION_COLOR);
        UIManager.put("MenuBar.background", TOOLBAR_COLOR);
        UIManager.put("MenuBar.foreground", FOREGROUND_COLOR);
        
        // Update all components
        SwingUtilities.updateComponentTreeUI(this);
    }

    // Method to add menus to the menu bar
    private void addMenus() {
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        menuBar.add(fileMenu);

        editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');
        menuBar.add(editMenu);

        viewMenu = new JMenu("View");
        viewMenu.setMnemonic('V');
        menuBar.add(viewMenu);

        formatMenu = new JMenu("Format");
        formatMenu.setMnemonic('o');
        menuBar.add(formatMenu);

        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        menuBar.add(helpMenu);
    }

    // Method to add items to the File menu
    private void addFileMenuItems() {
        FileActionListener fal = new FileActionListener(this);

        newFile = new JMenuItem("New");
        newFile.addActionListener(fal);
        newFile.setActionCommand("New");
        newFile.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(newFile);

        open = new JMenuItem("Open");
        open.addActionListener(fal);
        open.setActionCommand("Open");
        open.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(open);

        save = new JMenuItem("Save");
        save.addActionListener(fal);
        save.setActionCommand("Save");
        save.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(save);

        saveAs = new JMenuItem("Save As");
        saveAs.addActionListener(fal);
        saveAs.setActionCommand("Save As");
        saveAs.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        fileMenu.add(saveAs);

        print = new JMenuItem("Print");
        print.addActionListener(fal);
        print.setActionCommand("Print");
        print.setAccelerator(KeyStroke.getKeyStroke('P', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(print);

        fileMenu.addSeparator(); // Add a separator line

        close = new JMenuItem("Close");
        close.addActionListener(fal);
        close.setActionCommand("Close");
        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
        // Set preferred size for the menu item. This will affect the whole menu
        close.setPreferredSize(new Dimension(150, close.getPreferredSize().height));
        fileMenu.add(close);
    }

    // Method to add items to the Edit menu
    private void addEditMenuItems() {
        EditActionListener eal = new EditActionListener(this);

        undo = new JMenuItem("Undo");
        undo.addActionListener(eal);
        undo.setActionCommand("Undo");
        undo.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
        editMenu.add(undo);

        redo = new JMenuItem("Redo");
        redo.addActionListener(eal);
        redo.setActionCommand("Redo");
        redo.setAccelerator(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK));
        editMenu.add(redo);

        editMenu.addSeparator();

        cut = new JMenuItem("Cut");
        cut.addActionListener(eal);
        cut.setActionCommand("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_DOWN_MASK));
        editMenu.add(cut);

        copy = new JMenuItem("Copy");
        copy.addActionListener(eal);
        copy.setActionCommand("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK));
        editMenu.add(copy);

        paste = new JMenuItem("Paste");
        paste.addActionListener(eal);
        paste.setActionCommand("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_DOWN_MASK));
        editMenu.add(paste);

        delete = new JMenuItem("Delete");
        delete.addActionListener(eal);
        delete.setActionCommand("Delete");
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        editMenu.add(delete);

        editMenu.addSeparator();

        find = new JMenuItem("Find");
        find.addActionListener(eal);
        find.setActionCommand("Find");
        find.setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_DOWN_MASK));
        editMenu.add(find);

        replace = new JMenuItem("Replace");
        replace.addActionListener(eal);
        replace.setActionCommand("Replace");
        replace.setAccelerator(KeyStroke.getKeyStroke('H', InputEvent.CTRL_DOWN_MASK));
        editMenu.add(replace);

        editMenu.addSeparator();

        selectAll = new JMenuItem("Select All");
        selectAll.addActionListener(eal);
        selectAll.setActionCommand("Select All");
        selectAll.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_DOWN_MASK));
        editMenu.add(selectAll);

        timeDate = new JMenuItem("Time/Date");
        timeDate.addActionListener(eal);
        timeDate.setActionCommand("Time/Date");
        timeDate.setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.ALT_DOWN_MASK));
        editMenu.add(timeDate);
    }

    // Method to add items to the View menu
    private void addViewMenuItems() {
        ViewActionListener val = new ViewActionListener(this);

        wordWrap = new JCheckBoxMenuItem("Word Wrap");
        wordWrap.addActionListener(val);
        wordWrap.setActionCommand("Word Wrap");
        wordWrap.setAccelerator(KeyStroke.getKeyStroke('W', InputEvent.CTRL_DOWN_MASK));
        viewMenu.add(wordWrap);

        lineWrap = new JCheckBoxMenuItem("Line Numbers");
        lineWrap.addActionListener(val);
        lineWrap.setActionCommand("Line Numbers");
        lineWrap.setAccelerator(KeyStroke.getKeyStroke('L', InputEvent.CTRL_DOWN_MASK));
        viewMenu.add(lineWrap);

        showDetails = new JCheckBoxMenuItem("Status Bar");
        showDetails.addActionListener(val);
        showDetails.setActionCommand("Status Bar");
        showDetails.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_DOWN_MASK));
        viewMenu.add(showDetails);
    }

    // Method to add items to the Format menu
    private void addFormatMenuItems() {
        FormatActionListener fal = new FormatActionListener(this);

        // Create font menu
        JMenu fontMenu = new JMenu("Font");
        formatMenu.add(fontMenu);

        // Add font family submenu
        JMenu fontFamilyMenu = new JMenu("Font Family");
        fontMenu.add(fontFamilyMenu);

        // Add font families
        for (String font : fonts) {
            JMenuItem fontItem = new JMenuItem(font);
            fontItem.addActionListener(fal);
            fontItem.setActionCommand("Font");
            fontFamilyMenu.add(fontItem);
        }

        formatMenu.addSeparator();

        // Add font style options
        bold = new JCheckBoxMenuItem("Bold");
        bold.addActionListener(fal);
        bold.setActionCommand("Bold");
        bold.setAccelerator(KeyStroke.getKeyStroke('B', InputEvent.CTRL_DOWN_MASK));
        formatMenu.add(bold);

        italic = new JCheckBoxMenuItem("Italic");
        italic.addActionListener(fal);
        italic.setActionCommand("Italic");
        italic.setAccelerator(KeyStroke.getKeyStroke('I', InputEvent.CTRL_DOWN_MASK));
        formatMenu.add(italic);

        formatMenu.addSeparator();

        // Add font size submenu
        JMenu fontSizeMenu = new JMenu("Font Size");
        formatMenu.add(fontSizeMenu);

        // Add font sizes
        for (int size : fontSizes) {
            JMenuItem sizeItem = new JMenuItem(String.valueOf(size));
            sizeItem.addActionListener(fal);
            sizeItem.setActionCommand("fsize-" + size);
            fontSizeMenu.add(sizeItem);
        }

        formatMenu.addSeparator();

        // Add color submenus
        JMenu textColorMenu = new JMenu("Text Color");
        formatMenu.add(textColorMenu);

        // Add text color options
        for (int i = 0; i < colors.length; i++) {
            JMenuItem colorItem = new JMenuItem(colors[i]);
            colorItem.addActionListener(fal);
            colorItem.setActionCommand("fcolorfg-" + i);
            textColorMenu.add(colorItem);
        }

        JMenuItem customTextColor = new JMenuItem("Custom...");
        customTextColor.addActionListener(fal);
        customTextColor.setActionCommand("fcolorfg-Custom");
        textColorMenu.addSeparator();
        textColorMenu.add(customTextColor);

        JMenu bgColorMenu = new JMenu("Background Color");
        formatMenu.add(bgColorMenu);

        // Add background color options
        for (int i = 0; i < colors.length; i++) {
            JMenuItem colorItem = new JMenuItem(colors[i]);
            colorItem.addActionListener(fal);
            colorItem.setActionCommand("fcolorbg-" + i);
            bgColorMenu.add(colorItem);
        }

        JMenuItem customBgColor = new JMenuItem("Custom...");
        customBgColor.addActionListener(fal);
        customBgColor.setActionCommand("fcolorbg-Custom");
        bgColorMenu.addSeparator();
        bgColorMenu.add(customBgColor);
    }

    // Method to add items to the Help menu
    private void addHelpMenuItems() {
        HelpActionListener hal = new HelpActionListener(this);

        about = new JMenuItem("About");
        about.addActionListener(hal);
        about.setActionCommand("About");
        helpMenu.add(about);

        helpMenu.addSeparator();

        keyboardShortcuts = new JMenuItem("Keyboard Shortcuts");
        keyboardShortcuts.addActionListener(hal);
        keyboardShortcuts.setActionCommand("Keyboard Shortcuts");
        keyboardShortcuts.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        helpMenu.add(keyboardShortcuts);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Notepad().setVisible(true);
        });
    }
}
