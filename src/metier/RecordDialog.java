package metier;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class RecordDialog extends JDialog implements ActionListener {

    private JButton startButton;
    private JButton stopButton;
    private TargetDataLine line;
    private ByteArrayOutputStream byteArrayOutputStream;

    public RecordDialog(JFrame parent) {
        super(parent, "Record Audio", true); // Create a modal dialog with the specified title
        setLayout(new FlowLayout());
     // Set button colors
        Color buttonColor = new Color(0, 0, 0); // Black
        Color textColor = Color.WHITE; // White
        boolean focus=false;
        // Initialize start and stop buttons
        startButton = new JButton("Start Recording");
        startButton.setBackground(buttonColor);
        startButton.setForeground(textColor);
        startButton.setFocusPainted(focus);
        stopButton = new JButton("Stop Recording");
        stopButton.setBackground(buttonColor);
        stopButton.setForeground(textColor);
        stopButton.setFocusPainted(focus);
        

        // Add action listeners to start and stop buttons
        startButton.addActionListener(x -> {
            startRecording();
        });

        stopButton.addActionListener(x -> {
            stopRecording();
        });

        // Add buttons to the dialog
        add(startButton);
        add(stopButton);

        // Pack components into the dialog and set its location relative to the parent frame
        pack();
        setLocationRelativeTo(parent);
    }

    // Method to start recording audio
    private void startRecording() {
        try {
            // Define audio format
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

            // Define line info
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            // Check if the system supports the specified line info
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                return;
            }

            // Open the target data line and start recording
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            // Initialize byte output stream
            byteArrayOutputStream = new ByteArrayOutputStream();

            // Runnable to read data from the target data line continuously
            Runnable recordTask = () -> {
                byte[] buffer = new byte[line.getBufferSize() / 5];
                int bytesRead;

                while (true) {
                    bytesRead = line.read(buffer, 0, buffer.length);
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
            };

            // Start recording in a separate thread
            new Thread(recordTask).start();

            // Disable start button and enable stop button
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Method to stop recording audio
    private void stopRecording() {
        try {
            // Stop recording and close the target data line
            line.stop();
            line.close();

            // Get recorded audio data from the byte output stream
            byte[] audioData = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();

            // Define folder path to save the recorded audio file
            String folderPath = "C:\\Users\\MSI\\eclipse-workspace\\AL_Prod\\wavfile";
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs(); // Create the folder if it doesn't exist
            }

            // Prompt the user to enter the file name
            String fileName = JOptionPane.showInputDialog(this, "Enter file name:", "Save Recorded Audio", JOptionPane.PLAIN_MESSAGE);

            // If the user cancels or enters an empty name, use default name
            if (fileName == null || fileName.trim().isEmpty()) {
                fileName = "recorded.wav";
            } else {
                fileName += ".wav"; // Add extension
            }

            // Create the file to save the recorded audio
            File file = new File(folder, fileName);

            // Create an input stream from the recorded audio data
            ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
            AudioInputStream audioInputStream = new AudioInputStream(bais, line.getFormat(), audioData.length / line.getFormat().getFrameSize());

            // Write the audio data to the file in WAV format
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, file);

            // Close the audio input stream
            audioInputStream.close();

            // Enable start button and disable stop button
            startButton.setEnabled(true);
            stopButton.setEnabled(false);

            // Close the dialog
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ActionListener method (not used in this implementation)
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
    }
}
