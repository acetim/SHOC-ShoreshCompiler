import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;

public class Main {
    public static void main(String [] args){

        //////////write to file

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));//args1 is path to compiled file
            writer.write("abcd");
            writer.close();
        } catch(IOException e){//boiler plate
            e.printStackTrace();
        }
        //////////read to file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));//args0 is path source code
            System.out.println(reader.read());
            reader.close();
        } catch(IOException e){//boiler plate
            e.printStackTrace();
        }








    }
}