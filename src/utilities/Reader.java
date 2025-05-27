package utilities;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.Console;

public final class Reader {

    private Reader() {}



    public static float readFloatInRange(float left, float right)
    {
        Scanner scanner = new Scanner(System.in);
        float number;
        while(true) {
            try {
                number = scanner.nextFloat();

                if( !( left <= number && number <= right ) )
                    throw new InputMismatchException();

                return number;
            } catch (InputMismatchException e) {
                System.out.println("The input has to be a number between " + left + " and " + right + ".\n Please try again: ");
                scanner.nextLine();
            }
        }
    }

    public static int readIntInRange(int left, int right)
    {

        Scanner scanner = new Scanner(System.in);
        int number;
        while(true) {
            try {
                number = scanner.nextInt();

                if( !( left <= number && number <= right ) )
                    throw new InputMismatchException();

                return number;
            } catch (InputMismatchException e) {
                System.out.println("The input has to be an integer between " + left + " and " + right + ".\n Please try again: ");
                scanner.nextLine();
            }
        }
    }


    public static String readString()
    {
        Scanner scanner = new Scanner(System.in);
        String string  = scanner.nextLine();
        while(string.equals(""))
        {
            System.out.println("The input has to be a non-empty string.\n Please try again: ");
            string = scanner.nextLine();
        }
        return string;
    }

    public static String readNoSpaceString()
    {
        Scanner scanner = new Scanner(System.in);
        String string  = scanner.nextLine();
        while(string.equals("") || string.contains(" "))
        {
            System.out.println("The input has to be a non-empty string without spaces.\n Please try again: ");
            string = scanner.nextLine();
        }
        return string;
    }

    public static String readPassword()
    {
        Console console = System.console();
        if (console == null) {
            String password = readNoSpaceString();
            return password;
        }

        String password = new String(console.readPassword());
        while(password.equals("") || password.contains(" "))
        {
            System.out.println("The password has to be a non-empty string without spaces.\n Please try again: ");
            password = new String(console.readPassword());
        }
        return password;
    }

    public static void readEmpty()
    {
        Scanner scanner = new Scanner(System.in);
        String string  = scanner.nextLine();
    }
}
