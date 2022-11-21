package rogue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.TerminalPosition;

import javax.swing.JFrame;
import java.awt.Container;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

public class WindowUI extends JFrame {

    private GridBagLayout layout = new GridBagLayout();
    private GridBagConstraints gbc = new GridBagConstraints();
    private SwingTerminal terminal;
    private TerminalScreen screen;
    public static final int WIDTH = 700;
    public static final int HEIGHT = 800;
    // Screen buffer dimensions are different than terminal dimensions
    public static final int COLS = 80;
    public static final int ROWS = 42;
    public static final int INVDIM = 70;
    public static final int INVWIDTH = 91;
    public static final int INVHEIGHT = 100;
    private static final int TBORDER = 5;
    private static final int MAXLENGTH = 50;
    private final char startCol = 0;
    private final char msgRow = 0;
    private final char roomRow = 1;
    private Container contentPane;
    private static Rogue theGame;
    private static String output = "Welcome to Rogue";
    private String playerName = "Unknown Traveller";
    private JPanel messageBox = new JPanel();
    private JLabel outputMessage = new JLabel();
    private JPanel playerNameBox = new JPanel();
    private JLabel playerNameLabel = new JLabel();
    private JLabel[] inventory;
    private JPanel inv = new JPanel(new BorderLayout());
    private static JPanel centerInv = new JPanel();
    private GridBagLayout centerInvLayout = new GridBagLayout();
    private GridBagConstraints cInv = new GridBagConstraints();
    private JLabel title = new JLabel("Inventory");
    private int lastIndex = -1;
    private int lastLength = -1;
    private JPanel terminalPanel = new JPanel();


/**
Constructor.
**/

    public WindowUI() {
        super("Rogue Game");
        contentPane = getContentPane();
        setWindowDefaults(getContentPane());
        setUpPanels();
        setUpMenu(contentPane);
        pack();
        start();
    }

    private void setUpMenu(Container parent) {
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);
        JMenu fileMenu = new JMenu("Game Options");
        menubar.add(fileMenu);
        JMenuItem playerNameItem = new JMenuItem("Change Player Name");
        JMenuItem loadNewDungeon = new JMenuItem("Load New Dungeon.JSON File");
        JMenuItem loadSavedGame = new JMenuItem("Load Saved Game");
        JMenuItem saveGame = new JMenuItem("Save Current Game");
        fileMenu.add(playerNameItem); fileMenu.add(loadNewDungeon); fileMenu.add(loadSavedGame); fileMenu.add(saveGame);
        playerNameListen(playerNameItem, contentPane);
        loadDungeonListen(loadNewDungeon, contentPane);
        loadSaveListen(loadSavedGame, contentPane);
        saveGameListen(saveGame, contentPane);
    }


    private void startGame(Container parent) {
        JOptionPane.showMessageDialog(parent, "Play");
    }

    private void saveGameListen(JMenuItem it, Container parent) {
        it.addActionListener(click -> saveFile(parent));
    }


    private void saveFile(Container parent) {
        String path = null;
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        FileNameExtensionFilter binOnly = new FileNameExtensionFilter("Rogue File", "rogue");
        jfc.addChoosableFileFilter(binOnly);
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setDialogTitle("Save Rogue Game");
        int selected = jfc.showDialog(null, "Save Game");
        if (selected == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            path = selectedFile.getAbsolutePath();
        }
        saveRogueInstance(path, parent);
    }

    //SAVE FILE SERIALIZE
    private void saveRogueInstance(String path, Container parent) {
        try {
            if (path == null) {
                return;
            }
            path = path + ".rogue";
            FileOutputStream outStream = new FileOutputStream(path);
            ObjectOutputStream outDest = new ObjectOutputStream(outStream);
            outDest.writeObject(theGame);
            outDest.close(); //add popup to comminicate saving????
            outStream.close();
            JOptionPane.showMessageDialog(parent, "Game has been saved successfully!");
        } catch (IOException e) { //file didnt save handle
            JOptionPane.showMessageDialog(parent, "Game could NOT be saved! Please Try again.");
        }
    }


    private void loadSaveListen(JMenuItem it, Container parent) {
        it.addActionListener(click -> loadSaveFile(parent));
    }

    private void loadSaveFile(Container parent) {
        JOptionPane.showMessageDialog(parent, "Loading a save file will cause any unsaved progress to be lost!");
        String path = null;
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter binOnly = new FileNameExtensionFilter("Rogue File", "rogue");
        jfc.addChoosableFileFilter(binOnly);
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setDialogTitle("Load Rogue Save File");
        int selected = jfc.showDialog(null, "Load Save");

        if (selected == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            path = selectedFile.getAbsolutePath();
        }
        loadRogueInstance(path, parent);
        reloadGame();
    }

    private void reloadGame() {
        output = "Save Loaded";
        refreshInventory();
        refreshMessage();
        refreshPlayer();
        refreshTerminal();
    }


    private void loadRogueInstance(String path, Container parent) {
        Rogue savedGame = null;
        if (path == null) {
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));) { //must handle wrong file
            savedGame = (Rogue) in.readObject();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Game could not be loaded! Please Try Again.");
            return;
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(parent, "Game could not be loaded! Please Try Again.");
            return;
        }
        theGame = savedGame; //no errors in file loading
        JOptionPane.showMessageDialog(parent, "Game loaded successfully!");
    }


    private void loadDungeonListen(JMenuItem it, Container parent) {
        it.addActionListener(click -> loadDungeoneFile(parent));
    }

    private void loadDungeoneFile(Container parent) {
        JOptionPane.showMessageDialog(parent, "Loading a new dungeon file will cause any unsaved progress to be lost!");
        String path = null;
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter jsonOnly = new FileNameExtensionFilter("JSON File", "json");
        jfc.addChoosableFileFilter(jsonOnly);
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setDialogTitle("Load Dungeon File");
        int selected = jfc.showDialog(null, "Load Dungeon");

        if (selected == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            path = selectedFile.getAbsolutePath();
        }
        implementDungeon(path, parent);
        reloadGame();
    }


    private void implementDungeon(String path, Container parent) {
        if (path == null) {
            return;
        }
        RogueParser newParser = new RogueParser(path, null);
        Rogue newGame = new Rogue(newParser);
        theGame = null;
        theGame = newGame;
        Player thePlayer = new Player("Judi");
        theGame.setPlayer(thePlayer);
    }


    private void playerNameListen(JMenuItem it, Container parent) {
        it.addActionListener(click -> changePlayerName(parent));
    }

    private void changePlayerName(Container parent) {
        String newName = JOptionPane.showInputDialog(parent, "Enter new name!");
        if (newName != null && newName.length() > MAXLENGTH) {
            JOptionPane.showMessageDialog(parent, "Name is too long! Please try again.");
            return;
        }
        if (newName != null) {
            theGame.getPlayer().setName(newName);
        }
        refreshPlayer();
    }


    private void setWindowDefaults(Container contenPane) {
        setTitle("Rogue");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        contenPane.setLayout(layout);

    }


    private void setTerminal() {
        terminal = new SwingTerminal();
        terminalPanel.add(terminal);
        terminalPanel.setBorder(new LineBorder(Color.CYAN, TBORDER));
        terminalPanel.setBackground(Color.BLACK);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPane.add(terminalPanel, gbc);
    }



    private void setMessageBox() {
        outputMessage.setForeground(Color.WHITE);
        outputMessage.setText(output);
        messageBox.add(outputMessage);
        messageBox.setBackground(Color.DARK_GRAY);
        messageBox.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2 * 2, Color.CYAN));
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPane.add(messageBox, gbc);
    }

    private void refreshMessage() {
        outputMessage.setText(output);
        messageBox.validate();
        messageBox.repaint();
    }


    private void setPlayerBox() {
        playerNameLabel.setForeground(Color.WHITE);
        playerNameBox.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2 * 2, Color.CYAN));
        playerNameLabel.setText(playerName);
        playerNameBox.add(playerNameLabel);
        playerNameBox.setBackground(Color.DARK_GRAY);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(playerNameBox, gbc);
    }

    private void refreshPlayer() {
        playerName = theGame.getPlayer().getName();
        if (playerName == null) {
            playerName = "Unknown Traveller";
            return;
        }
        playerNameLabel.setText(playerName);
        playerNameBox.validate();
        playerNameBox.repaint();
    }

    private void getOutput(String msg) {
        output = msg;
    }

    private void setInventory() {
        centerElementWhite(title);
        title.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        inv.setBorder(BorderFactory.createMatteBorder(0, 2 * 2, 0, 0, Color.CYAN));
        inv.add(title, BorderLayout.NORTH);
        centerInv.setPreferredSize(new Dimension(INVWIDTH, INVHEIGHT));
        inv.add(centerInv, BorderLayout.CENTER);
        inv.setBackground(Color.DARK_GRAY);
        centerInv.setBackground(Color.DARK_GRAY);
        centerInv.setLayout(centerInvLayout);
        addEmpty();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2 + 1;
        gbc.ipadx = INVDIM;
        contentPane.add(inv, gbc);
    }

    private void centerElementWhite(JLabel l) {
        l.setHorizontalAlignment(JLabel.CENTER);
        l.setVerticalAlignment(JLabel.CENTER);
        l.setForeground(Color.WHITE);
    }

    private void centerElementSelect(JLabel l) {
        l.setHorizontalAlignment(JLabel.CENTER);
        l.setVerticalAlignment(JLabel.CENTER);
        l.setForeground(Color.MAGENTA);
    }



    private void refreshInventory() {
        int tempIndex = theGame.getSelected();
        int tempLength = theGame.getInventoryItems().length;
        String[] its = theGame.getInventoryItems();
        if (tempIndex != lastIndex || tempLength != lastLength) {
            lastIndex = tempIndex;
            lastLength = tempLength;
            repaintInv();
        }
    }


    private void repaintInv() {
        centerInv.removeAll();
        String[] its = theGame.getInventoryItems();
        if (its == null || its.length == 0) {
            addEmpty();
        } else {
            inventory = new JLabel[its.length];
            addLabelsToInv(inventory);
        }
        centerInv.validate();
        centerInv.repaint();
    }

    private void addEmpty() {
        JLabel l = new JLabel("No Items");
        centerElementWhite(l);
        cInv.gridx = 0;
        cInv.gridy = 0;
        centerInv.add(l, cInv);
    }


    private void addLabelsToInv(JLabel[] l) {
        String[] names = theGame.getInventoryItems();
        int k = theGame.getSelected();
        for (int i = 0; i < l.length; i++) {
            l[i] = new JLabel(names[i]);
            if (k == i) {
                centerElementSelect(l[i]);
            } else {
                centerElementWhite(l[i]);
            }
            cInv.gridx = 0;
            cInv.gridy = i;
            cInv.gridwidth = 0;
            centerInv.add(l[i], cInv);
        }
    }

    private void setUpPanels() {
        setTerminal();
        setPlayerBox();
        setMessageBox();
        setInventory();
    }

    private void setUpLabelPanel(JPanel thePanel) {
        Border prettyLine = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        thePanel.setBorder(prettyLine);
        JLabel exampleLabel = new JLabel("Tomorrow and tomorrow and tomorrow");
        thePanel.add(exampleLabel);
        JTextField dataEntry = new JTextField("Enter text here", (2 * 2 * 2 * 2 * 2) - (2 * 2 * 2) + 1);
        thePanel.add(dataEntry);
        JButton clickMe = new JButton("Click Me");
        thePanel.add(clickMe);
    }

    private void start() {
        try {
            screen = new TerminalScreen(terminal);
            screen.setCursorPosition(TerminalPosition.TOP_LEFT_CORNER);
            screen.startScreen();
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
Prints a string to the screen starting at the indicated column and row.
@param toDisplay the string to be printed
@param column the column in which to start the display
@param row the row in which to start the display
**/
        public void putString(String toDisplay, int column, int row) {

            Terminal t = screen.getTerminal();
            try {
                t.setCursorPosition(column, row);
            for (char ch: toDisplay.toCharArray()) {
                t.putCharacter(ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        }

/**
Changes the message at the top of the screen for the user.
@param msg the message to be displayed
**/
            public void setMessage(String msg) {
                //putString("                                                ", 1, 1);
                //putString(msg, startCol, msgRow);
                getOutput(msg);
            }

/**
Redraws the whole screen including the room and the message.
@param message the message to be displayed at the top of the room
@param room the room map to be drawn
**/
public void draw(String message, String room) {

    try {
        screen.getTerminal().clearScreen();
        setMessage(message);
        refreshInventory();
        refreshMessage();
        refreshPlayer();
        putString(room, startCol, roomRow);
        screen.refresh(); //were in draw
    } catch (IOException e) {

    }

}


private void refreshTerminal() {
    try {
        screen.getTerminal().clearScreen();
        putString(theGame.getNextDisplay(), startCol, roomRow);
        screen.refresh();
    } catch (IOException e) {

    }
}

/**
Obtains input from the user and returns it as a char.  Converts arrow
keys to the equivalent movement keys in rogue.
@return the ascii value of the key pressed by the user
**/
public char getInput() {
    KeyStroke keyStroke = null;
    char returnChar;
    while (keyStroke == null) {
    try {
        keyStroke = screen.pollInput();
    } catch (IOException e) {
    }
}
return getInputChar(keyStroke);
}


private char getInputChar(KeyStroke keyStroke) {
char returnChar;
if (keyStroke.getKeyType() == KeyType.ArrowDown) {
    returnChar = Rogue.DOWN;  //constant defined in rogue
} else if (keyStroke.getKeyType() == KeyType.ArrowUp) {
    returnChar = Rogue.UP;
} else if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
    returnChar = Rogue.LEFT;
} else if (keyStroke.getKeyType() == KeyType.ArrowRight) {
    returnChar = Rogue.RIGHT;
} else {
    returnChar = keyStroke.getCharacter();
}
return returnChar;
}


private static void playGame(char userInput, String message, WindowUI theGameUI, Rogue game) {
    while (userInput != 'q') {
        userInput = theGameUI.getInput();
        if (userInput == 'q') {
            message = "Game Quit";
            theGameUI.draw(message, game.getNextDisplay()); break;
        }
        try {
            game = theGame;
            message = game.makeMove(userInput);
            theGameUI.draw(message, game.getNextDisplay());
        } catch (InvalidMoveException e) {
            message = "Cannot walk through wall!";
            theGameUI.setMessage(message);
        }
        }
}



/**
The controller method for making the game logic work.
@param args command line parameters
**/
    public static void main(String[] args) {
        char userInput = 'h';
        String configurationFileLocation = "fileLocations.json";
        RogueParser parser = new RogueParser(configurationFileLocation);
        WindowUI theGameUI = new WindowUI();
        theGame = new Rogue(parser);
        Player thePlayer = new Player("Liam");
        theGame.setPlayer(thePlayer);
        theGameUI.draw(output, theGame.getNextDisplay()); 
        theGameUI.setVisible(true);
        playGame(userInput, output, theGameUI, theGame);
    }


}
