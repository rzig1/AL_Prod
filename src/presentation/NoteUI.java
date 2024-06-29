package presentation;

import javax.swing.*;
import dao.GestionNote;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class NoteUI extends JFrame {

    // Components
    private JTextArea lyricsTextArea;
    private JTable notesTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;
    private JButton linkButton;
    private JButton playButton;
    private JButton loadButton;
    private JButton displayButton;
    private JButton backButton;
    private GestionNote databaseManager;

    public NoteUI() {
        // Frame setup
        setTitle("Song Notes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Initialize components
        lyricsTextArea = new JTextArea();
        lyricsTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane lyricsScrollPane = new JScrollPane(lyricsTextArea);
        add(lyricsScrollPane, BorderLayout.CENTER);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Note"}, 0);
        notesTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(notesTable);
        add(tableScrollPane, BorderLayout.EAST);

        // Button panel setup
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add Note button
        addButton = new JButton("Add Note");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        addButton.setBackground(new Color(0, 0, 0)); // Button background color
        addButton.setForeground(Color.WHITE); // Button text color
        addButton.setFocusPainted(false); // Remove focus border
        addButton.addActionListener(e -> addNote());
        buttonPanel.add(addButton);

        // Edit Note button
        editButton = new JButton("Edit Note");
        editButton.setFont(new Font("Arial", Font.BOLD, 14));
        editButton.setBackground(new Color(0, 0, 0)); // Button background color
        editButton.setForeground(Color.WHITE); // Button text color
        editButton.setFocusPainted(false); // Remove focus border
        editButton.addActionListener(e -> editNote());
        buttonPanel.add(editButton);

        // Display Text button
        displayButton = new JButton("Display Text");
        displayButton.setFont(new Font("Arial", Font.BOLD, 14));
        displayButton.setBackground(new Color(0, 0, 0)); // Button background color
        displayButton.setForeground(Color.WHITE); // Button text color
        displayButton.setFocusPainted(false); // Remove focus border
        displayButton.addActionListener(e -> displayNoteText());
        buttonPanel.add(displayButton);

        // Remove Note button
        removeButton = new JButton("Remove Note");
        removeButton.setFont(new Font("Arial", Font.BOLD, 14));
        removeButton.setBackground(new Color(0, 0, 0)); // Button background color
        removeButton.setForeground(Color.WHITE); // Button text color
        removeButton.setFocusPainted(false); // Remove focus border
        removeButton.addActionListener(e -> removeNote());
        buttonPanel.add(removeButton);

        // Link Audio button
        linkButton = new JButton("Link Audio");
        linkButton.setFont(new Font("Arial", Font.BOLD, 14));
        linkButton.setBackground(new Color(0, 0, 0)); // Button background color
        linkButton.setForeground(Color.WHITE); // Button text color
        linkButton.setFocusPainted(false); // Remove focus border
        linkButton.addActionListener(e -> LinkAudioUrl());
        buttonPanel.add(linkButton);

        // Load Notes button
        loadButton = new JButton("Load Notes");
        loadButton.setFont(new Font("Arial", Font.BOLD, 14));
        loadButton.setBackground(new Color(0, 0, 0)); // Button background color
        loadButton.setForeground(Color.WHITE); // Button text color
        loadButton.setFocusPainted(false); // Remove focus border
        loadButton.addActionListener(e -> loadNotesFromDatabase());
        buttonPanel.add(loadButton);

        // Play Audio button
        playButton = new JButton("Play Audio");
        playButton.setFont(new Font("Arial", Font.BOLD, 14));
        playButton.setBackground(new Color(0, 0, 0)); // Button background color
        playButton.setForeground(Color.WHITE); // Button text color
        playButton.setFocusPainted(false); // Remove focus border
        playButton.addActionListener(e -> playAudio());
        buttonPanel.add(playButton);

        // Back button
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(0, 0, 0)); // Button background color
        backButton.setForeground(Color.WHITE); // Button text color
        backButton.setFocusPainted(false); // Remove focus border
        backButton.addActionListener(e -> {
            dispose();
            HomeUI homeUI = new HomeUI();
            homeUI.setVisible(true);
        });
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Initialize database manager and load notes
        databaseManager = new GestionNote();
        databaseManager.connect();
        loadNotesFromDatabase();

        // Set frame visibility
        setVisible(true);
    }

    // Method to add a note
    private void addNote() {
        String noteName = JOptionPane.showInputDialog(this, "Enter Note Text:");
        String noteText = lyricsTextArea.getText().trim();
        databaseManager.addNoteToDatabase(noteName, noteText, tableModel);
    }

    // Method to edit a note
    private void editNote() {
        int row = notesTable.getSelectedRow();
        if (row != -1) {
            int id = (Integer) tableModel.getValueAt(row, 0);
            String currentName = (String) tableModel.getValueAt(row, 1);
            String currentNote = (String) tableModel.getValueAt(row, 2);

            String editedName = JOptionPane.showInputDialog(this, "Edit Note Name:", currentName);
            String editedNote = JOptionPane.showInputDialog(this, "Edit Note Text:", currentNote);

            databaseManager.editNoteInDatabase(id, editedName, editedNote, tableModel, this);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a note to edit.");
        }
    }

    // Method to display note text in the text area
    private void displayNoteText() {
        int selectedRow = notesTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (Integer) tableModel.getValueAt(selectedRow, 0);
            String noteText = databaseManager.getNoteText(id);
            if (noteText != null) {
                lyricsTextArea.setText(noteText);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to retrieve note text from the database.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a note to display.");
        }
    }

    // Method to remove a note
    private void removeNote() {
        int row = notesTable.getSelectedRow();
        if (row != -1) {
            int id = (Integer) tableModel.getValueAt(row, 0);
            databaseManager.removeNoteFromDatabase(id, tableModel, this);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a note to remove.");
        }
    }
 // Method to display MusicUI and get selected audio file path
    public String getMusicfile() {
        MusicUI m = new MusicUI();
        m.setVisible(false);
        return m.getSelectedAudioFilePath();
    }

    // Method to link audio URL to a note
    private void LinkAudioUrl() {
        int selectedRow = notesTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (Integer) tableModel.getValueAt(selectedRow, 0);
            String audioFilePath = getMusicfile();
            databaseManager.updateNoteAudioURL(id, audioFilePath);
            databaseManager.loadNotesFromDatabase(tableModel, this);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a note to link the audio URL.");
        }
    }

    

    // Method to play audio
    private void playAudio() {
        int selectedRow = notesTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (Integer) tableModel.getValueAt(selectedRow, 0);
            String audioURL = databaseManager.getAudioURL(id);
            if (audioURL != null) {
                MusicUI musicUI = new MusicUI(audioURL);
                musicUI.setLocationRelativeTo(null);
                musicUI.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "No audio URL found for the selected note.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a note to play the audio.");
        }
    }

    // Method to load notes from the database
    private void loadNotesFromDatabase() {
        databaseManager.loadNotesFromDatabase(tableModel, this);
    }

    // Main method
    public static void main(String[] args) {
        NoteUI n= new NoteUI();
        n.show();
        n.setLocationRelativeTo(null);
    }
}
