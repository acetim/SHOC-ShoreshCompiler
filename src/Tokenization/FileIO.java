package Tokenization;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileIO {
    private BufferedReader Reader;
    public FileIO(String filePath) {
        try {
            this.Reader=new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            System.err.println("FILE NOT FOUND");
            System.exit(1);
        }
    }
    public String nextLine(){
        try {
            return Reader.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}

    }
    public void closeIO(){
        try {
            this.Reader.close();
            System.out.println("\u001B[32m" + "קורא הקבצים נסגר בהצלחה" + "\u001B[0m");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
