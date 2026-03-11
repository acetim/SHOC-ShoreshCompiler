package CodeGeneration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class codePrinter  {
    private BufferedWriter writer;

    public codePrinter(String path) {
        try {
            this.writer = new BufferedWriter(new FileWriter(path));
        } catch (IOException e) {
            System.err.println("בעיה בזמן יצירת קובץ");
            System.exit(1);
        }
    }
    public void write(String s){
        try {
            this.writer.write(s);
        } catch (IOException e) {
            System.err.println("בעיה בזמן כתיבה לקבוץ");
            this.closePrinter();
            throw new RuntimeException(e);
        }
    }

    public void closePrinter(){
        try {
            this.writer.close();
            System.out.println("\u001B[32m" + "כותב הקבצים נסגר בהצלחה" + "\u001B[0m");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
