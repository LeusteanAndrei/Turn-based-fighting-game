package utilities;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;


public class Csv {

    private static Path filepath = Path.of("C:\\Users\\andre\\Desktop\\FMI\\AN 2\\SEM II\\Java\\Proiect\\src\\Classes\\csv_reports\\csv_report.csv");

    public static void create(String fileName)
    {
        String currentDirectory = System.getProperty("user.dir");
        Path path = Path.of(currentDirectory + "\\src\\Classes\\csv_reports\\" + fileName + ".csv");
        try
        {
            Files.createFile(path);
            filepath = path;

        }
        catch (Exception e)
        {
            throw  new RuntimeException(e);
        }


    }

    public static void write(String data)
    {
        try
        {
            String to_write = data + ", " +  Instant.now().toEpochMilli() + "\n";
            Files.writeString(filepath, to_write, StandardOpenOption.APPEND);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }



}
