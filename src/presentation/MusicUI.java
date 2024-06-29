package presentation;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import metier.music_play;
import metier.RecordDialog;

public class MusicUI extends JFrame implements ActionListener {

    private JTextField filePathField;
    private JButton playButton;
    private JButton pauseButton;
    private JButton chooseButton;
    private JButton recordButton;
    private JButton backButton;
    private JFileChooser fileChooser;
    private String audioFilePath;

    public MusicUI() {
        super("Music Player");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        filePathField = new JTextField(20);
        playButton = new JButton("Play");
        pauseButton = new JButton("Pause");
        chooseButton = new JButton("Choose File");
        recordButton = new JButton("Record");
        backButton = new JButton("Back to Home");

        // Set button colors
        Color buttonColor = new Color(0, 0, 0); // Black
        Color textColor = Color.WHITE; // White
        boolean focus=false;

        playButton.setBackground(buttonColor);
        playButton.setForeground(textColor);
        playButton.setFocusPainted(focus);

        pauseButton.setBackground(buttonColor);
        pauseButton.setForeground(textColor);
        pauseButton.setFocusPainted(focus);

        chooseButton.setBackground(buttonColor);
        chooseButton.setForeground(textColor);
        chooseButton.setFocusPainted(focus);

        recordButton.setBackground(buttonColor);
        recordButton.setForeground(textColor);
        recordButton.setFocusPainted(focus);

        backButton.setBackground(buttonColor);
        backButton.setForeground(textColor);
        backButton.setFocusPainted(focus);

        playButton.addActionListener(this);
        pauseButton.addActionListener(this);
        chooseButton.addActionListener(this);
        recordButton.addActionListener(this);
        backButton.addActionListener(this);

        add(filePathField);
        add(chooseButton);
        add(playButton);
        add(pauseButton);
        add(recordButton);
        add(backButton);

        fileChooser = new JFileChooser(".");
        fileChooser.setFileFilter(new FileNameExtensionFilter("WAV Files", "wav"));

        setSize(500, 100);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public MusicUI(String audioFilePath) {
        this(); // Call the default constructor to initialize UI components

        this.audioFilePath = audioFilePath;
        filePathField.setText(audioFilePath);
        filePathField.setEditable(false);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == playButton) {
            music_play.PlayMusic(filePathField.getText());
        } else if (event.getSource() == pauseButton) {
            music_play.pauseMusic(filePathField.getText());
        } else if (event.getSource() == chooseButton) {
            chooseFile();
        } else if (event.getSource() == recordButton) {
            openRecordDialog();
        } else if (event.getSource() == backButton) {
            goBackToHome();
        }
    }

    private void chooseFile() {
        fileChooser.setCurrentDirectory(new File(".\\wavfile"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    public String getSelectedAudioFilePath() {
        chooseFile();
        return filePathField.getText();
    }

    private void goBackToHome() {
        dispose();
        HomeUI homeUI = new HomeUI();
        homeUI.setVisible(true);
    }

    private void openRecordDialog() {
        RecordDialog recordDialog = new RecordDialog(this);
        recordDialog.setVisible(true);
    }

    public static void main(String[] args) {
        MusicUI m = new MusicUI();
        m.show();
        m.setLocationRelativeTo(null);
    }
}
