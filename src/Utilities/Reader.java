package Utilities;

import java.util.InputMismatchException;
import java.util.Scanner;

public final class Reader {

    private Reader() {}


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


}
