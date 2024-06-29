package presentation;

import javax.swing.*;
import java.awt.*;

public class HomeUI extends JFrame {
    public HomeUI() {
        setTitle("Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240)); // Setting background color

        // Create a panel for the text and buttons
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        JLabel welcomeLabel = new JLabel("Marhbe Bik Ya Mr fy AL Prod", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 50));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setBackground(new Color(240, 240, 240));

        // Record Button
        JButton recordButton = new JButton("Record");
        recordButton.setPreferredSize(new Dimension(150, 50));
        recordButton.setFont(new Font("Arial", Font.BOLD, 16));
        recordButton.setBackground(new Color(0, 0, 0)); // Button background color
        recordButton.setForeground(Color.WHITE); // Button text color
        recordButton.setFocusPainted(false); // Remove focus border
        recordButton.addActionListener(e -> {
            MusicUI recordUI = new MusicUI();
            recordUI.setVisible(true);
            recordUI.setLocationRelativeTo(null);
            dispose();
        });
        buttonPanel.add(recordButton);

        // Go see lyrics Button
        JButton lyricsButton = new JButton("Go see lyrics");
        lyricsButton.setPreferredSize(new Dimension(150, 50));
        lyricsButton.setFont(new Font("Arial", Font.BOLD, 16));
        lyricsButton.setBackground(new Color(0, 0, 0)); // Button background color
        lyricsButton.setForeground(Color.WHITE); // Button text color
        lyricsButton.setFocusPainted(false); // Remove focus border
        lyricsButton.addActionListener(e -> {
            NoteUI lyricsUI = new NoteUI();
            lyricsUI.setVisible(true);
            lyricsUI.setLocationRelativeTo(null);
            dispose();
        });
        buttonPanel.add(lyricsButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
       
            HomeUI h = new HomeUI();
            h.show();
            h.setLocationRelativeTo(null);
        
    }
}
