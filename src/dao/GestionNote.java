package dao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class GestionNote {

    private final DatabaseConnectionManager connectionManager;

    // Constructor initializing the singleton DatabaseConnectionManager
    public GestionNote() {
        connectionManager = DatabaseConnectionManager.getInstance();
    }

    // Method to establish a database connection
    public void connect() {
        try {
            // Attempt to establish a database connection
            connectionManager.getConnection();
        } catch (SQLException e) {
            // Handle SQL errors by printing the stack trace and showing an error message
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to database: " + e.getMessage());
            System.exit(1); // Terminate the application
        }
    }

    // Method to add a note to the database
    public void addNoteToDatabase(String noteName, String noteText, DefaultTableModel tableModel) {
        // Check if noteText is not null or empty
        if (noteText != null && !noteText.isEmpty()) {
            // Use try-with-resources to automatically close resources
            try (Connection connection = connectionManager.getConnection(); 
                 PreparedStatement preparedStatement = connection.prepareStatement(
                         "INSERT INTO notee (namenote, note_text) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, noteName);
                preparedStatement.setString(2, noteText);
                preparedStatement.executeUpdate();
                // Retrieve the generated keys to get the ID of the inserted note
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        // Add the new note to the JTable
                        tableModel.addRow(new Object[]{id, noteName, noteText});
                        JOptionPane.showMessageDialog(null, "Lyric added to database successfully.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error adding note to database: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Note text is required.");
        }
    }

    // Method to edit a note in the database
    public void editNoteInDatabase(int id, String editedName, String editedNote, DefaultTableModel tableModel, JFrame frame) {
        // Check if both editedName and editedNote are not null or empty
        if ((editedName != null && !editedName.isEmpty()) && (editedNote != null && !editedNote.isEmpty())) {
            try (Connection connection = connectionManager.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(
                         "UPDATE notee SET namenote = ?, note_text = ? WHERE idnotee = ?")) {
                preparedStatement.setString(1, editedName);
                preparedStatement.setString(2, editedNote);
                preparedStatement.setInt(3, id);
                int rowsAffected = preparedStatement.executeUpdate();
                // Update the JTable with the edited values
                if (rowsAffected > 0) {
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        if ((Integer) tableModel.getValueAt(i, 0) == id) {
                            tableModel.setValueAt(editedName, i, 1);
                            tableModel.setValueAt(editedNote, i, 2);
                            break;
                        }
                    }
                    JOptionPane.showMessageDialog(frame, "Lyric updated successfully.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error updating note: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Both name and text are required.");
        }
    }

    // Method to retrieve the text of a note from the database
    public String getNoteText(int id) {
        String noteText = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT note_text FROM notee WHERE idnotee = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                noteText = resultSet.getString("note_text");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noteText;
    }

    // Method to remove a note from the database
    public void removeNoteFromDatabase(int id, DefaultTableModel tableModel, JFrame frame) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM notee WHERE idnotee = ?")) {
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                // Remove the note from the JTable
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if ((Integer) tableModel.getValueAt(i, 0) == id) {
                        tableModel.removeRow(i);
                        break;
                    }
                }
                JOptionPane.showMessageDialog(frame, "Lyric removed successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "No note found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error removing note: " + e.getMessage());
        }
    }

    // Method to update the audio URL of a note in the database
    public void updateNoteAudioURL(int id, String audioURL) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE notee SET audio_url = ? WHERE idnotee = ?")) {
            preparedStatement.setString(1, audioURL);
            preparedStatement.setInt(2, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Audio URL updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "No rows were affected. Note with ID: " + id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating audio URL: " + e.getMessage());
        }
    }

    // Method to retrieve the audio URL of a note from the database
    public String getAudioURL(int id) {
        String audioURL = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT audio_url FROM notee WHERE idnotee = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                audioURL = resultSet.getString("audio_url");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving audio URL: " + e.getMessage());
        }
        return audioURL;
    }

    // Method to load notes from the database into the JTable
    public void loadNotesFromDatabase(DefaultTableModel tableModel, JFrame frame) {
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT idnotee, namenote, note_text FROM notee");
            tableModel.setRowCount(0); // Clear existing rows
            while (resultSet.next()) {
                int id = resultSet.getInt("idnotee");
                String name = resultSet.getString("namenote");
                String note = resultSet.getString("note_text");
                tableModel.addRow(new Object[]{id, name, note});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading notes: " + e.getMessage());
        }
    }
}
