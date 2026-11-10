
package util;

import java.util.Scanner;

public class Input {

    public static Scanner sc = new Scanner(System.in);

    public static int getId(String message) {

        while (true) {
            System.out.print(message);

            try {
                int id = Integer.parseInt(sc.nextLine().trim());

                if (id > 0)
                    return id;

                System.out.println("Error: ID must be positive!");

            } catch (NumberFormatException e) {
                System.out.println("Error: Enter a valid number!");
            }
        }
    }

    public static String getText(String message) {

        while (true) {
            System.out.print(message);
            String text = sc.nextLine().trim();

            if (!text.isEmpty())
                return text;

            System.out.println("Error: Field cannot be empty!");
        }
    }

    public static String getName(String message) {

        while (true) {
            String name = getText(message);

            if (name.matches("[a-zA-Z ]+"))
                return name;

            System.out.println("Error: Enter a valid name!");
        }
    }

    public static String getEmail(String message) {

        while (true) {
            String email = getText(message);

            if (email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
                return email;

            System.out.println("Error: Enter a valid email!");
        }
    }

    public static int getRating(String message) {

        while (true) {
            System.out.print(message);

            try {
                int rating = Integer.parseInt(sc.nextLine().trim());

                if (rating >= 1 && rating <= 5)
                    return rating;

                System.out.println("Error: Rating must be 1-5!");

            } catch (NumberFormatException e) {
                System.out.println("Error: Enter a valid number!");
            }
        }
    }

    public static int getChoice(int max) {

        while (true) {
            System.out.print("Enter Choice: ");

            try {
                int choice = Integer.parseInt(sc.nextLine().trim());

                if (choice >= 1 && choice <= max)
                    return choice;

                System.out.println("Error: Invalid menu option!");

            } catch (NumberFormatException e) {
                System.out.println("Error: Enter numbers only!");
            }
        }
    }

    public static int getYear(String message) {

        while (true) {
            System.out.print(message);

            try {
                int year = Integer.parseInt(sc.nextLine().trim());

                if (year >= 1 && year <= 4)
                    return year;

                System.out.println("Error: Year must be 1-4!");

            } catch (NumberFormatException e) {
                System.out.println("Error: Enter a valid number!");
            }
        }
    }

    public static int getPositiveNumber(String message) {

        while (true) {
            System.out.print(message);

            try {
                int number = Integer.parseInt(sc.nextLine().trim());

                if (number > 0)
                    return number;

                System.out.println("Error: Enter a positive number!");

            } catch (NumberFormatException e) {
                System.out.println("Error: Enter a valid number!");
            }
        }
    }
}
