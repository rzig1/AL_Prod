package metier;
import java.io.File;


import javax.sound.sampled.*;
// Import classes  javax.sound.sampled package bech nekhdmou bil audio

public class music_play {
 // classe clip tpreloday data wou optimale lil reservation mtaa audio data fil memoire tkhalini nabda nA9ra mnin  audio data min any point

    private static Clip clip;
// attr static alakhtr chytekhedem fy methode static

    public static void PlayMusic(String location) {
// Methode  bich talaaab musica wou static bich mtets7açch insatnaciation mtaa classe

        try {

            File musicpath = new File(location);
// Creating a new File object with the file path specified by the 'location' parameter

            if (musicpath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicpath);
// Obtaining an AudioInputStream from the specified file

                AudioFormat baseFormat = audioInput.getFormat();
// Obtaining the audio format of the input stream

                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,  //Pulse Code Modultaion
// Creating a new AudioFormat for the decoded audio data
                        44100, 16, baseFormat.getChannels(), baseFormat.getChannels() * 2,
                        44100, false);

                AudioInputStream audioInput2 = AudioSystem.getAudioInputStream(decodedFormat, audioInput);
// Obtaining a new AudioInputStream with the specified decoded audio format

                clip = AudioSystem.getClip();
// Obtaining a new Clip instance

                clip.open(audioInput2);
// Opening the Clip with the decoded audio input stream

                clip.start();
// Starting playback of the audio clip

            } else {
// Else block if the specified file does not exist

                System.out.println("Can't find file");
// Printing a message indicating that the file cannot be found

            }
        } catch (Exception e) {
// Catching any exceptions that occur during execution

            e.printStackTrace();
// Printing the stack trace of the caught exception

        }
    }

    public static void pauseMusic(String location) {
// Declaration of a public static method named 'pauseMusic' which takes a String parameter 'location'

        try {
// Beginning of try block for exception handling

            if (clip != null && clip.isRunning()) {
// Checking if the Clip instance is not null and is currently running

                long clipTimePosition = clip.getMicrosecondPosition();
// Obtaining the current position of the Clip's playback

                clip.stop();
// Stopping playback of the audio clip

                clip.setMicrosecondPosition(clipTimePosition);
// Setting the position of the Clip's playback to the previously saved position

            }
        } catch (Exception e) {
// Catching any exceptions that occur during execution

            e.printStackTrace();
// Printing the stack trace of the caught exception

        }
    }
    
}
