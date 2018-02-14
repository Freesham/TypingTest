/**
 The TypingTest program calculates the user's word-per-minute typing speed
 once the user types the same amount of character as the sample text.
 The TypingTest class reads a .txt input with the sample text. The GUI class
 calls this class to retrieve the StringBuilder containing the sample text.

 @author Hicham El-Abbadi
 @version 1.0
 @since 02/14/2018
 */

import java.io.*;
import java.util.Scanner;

public class TypingTest {

    //Store text using StringBuilder
    public StringBuilder textMethod(){
        StringBuilder sb = new StringBuilder();

        try
        {
            File file = new File("sample_text.txt");

            Scanner inFile = new Scanner(file);
            System.out.println("File sample_text.txt has been opened.");
            while(inFile.hasNext()) {
                String parse = inFile.useDelimiter("\\A").next();
                sb.append(parse);
            }
            inFile.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File not found.");
        }

        return sb;
    }
}
