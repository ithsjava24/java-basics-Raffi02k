package org.example;

import java.util.Arrays;
import java.util.Scanner;

public class App {

    private static double[] elpriser = new double[0]; // Array för att lagra elpriser
    private static Scanner scanner = new Scanner(System.in); // Scanner för inmatning

    public static void main(String[] args) {
        while (true) { // Meny-loop som körs tills användaren väljer att avsluta
            printMenu(); // Skriv ut menyn
            String choice = scanner.nextLine(); // Läs användarens val

            switch (choice.toLowerCase()) { // Hantera valet
                case "1":
                    inputPrices(); // Hantera inmatning av elpriser
                    break;
                case "2":
                    calculateMinMaxAvg(); // Beräkna min, max och medelvärden
                    break;
                case "3":
                    sortPrices(); // Sortera elpriserna
                    break;
                case "4":
                    bestChargingTime(); // Hitta bästa laddningstid (4h)
                    break;
                case "e":
                    System.out.println("Avslutar programmet..."); // Avsluta programmet
                    return; // Avsluta main-metoden
                default:
                    System.out.println("Ogiltigt val, försök igen."); // Hantera ogiltig inmatning
            }
        }
    }

    // Skriv ut menyn
    private static void printMenu() {
        System.out.println("Elpriser");
        System.out.println("========");
        System.out.println("1. Inmatning");
        System.out.println("2. Min, Max och Medel");
        System.out.println("3. Sortera");
        System.out.println("4. Bästa Laddningstid (4h)");
        System.out.println("e. Avsluta");
        System.out.print("Välj ett alternativ: ");
    }

    // Metod för inmatning av elpriser
    private static void inputPrices() {
        System.out.println("Ange antalet elpriser:");
        int antal = scanner.nextInt();
        elpriser = new double[antal]; // Skapa en array för att lagra elpriserna

        for (int i = 0; i < antal; i++) {
            System.out.print("Ange elpris #" + (i + 1) + ": ");
            elpriser[i] = scanner.nextDouble(); // Läs in varje elpris
        }
        scanner.nextLine(); // Rensa scanner-buffer
    }

    // Metod för att beräkna min, max och medelvärde
    private static void calculateMinMaxAvg() {
        if (elpriser.length == 0) {
            System.out.println("Inga elpriser inmatade.");
            return;
        }

        double min = Arrays.stream(elpriser).min().orElse(Double.NaN);
        double max = Arrays.stream(elpriser).max().orElse(Double.NaN);
        double avg = Arrays.stream(elpriser).average().orElse(Double.NaN);

        System.out.println("Minsta elpris: " + min);
        System.out.println("Största elpris: " + max);
        System.out.println("Medelvärde: " + avg);
    }

    // Metod för att sortera elpriser
    private static void sortPrices() {
        if (elpriser.length == 0) {
            System.out.println("Inga elpriser inmatade.");
            return;
        }

        Arrays.sort(elpriser); // Sortera arrayen
        System.out.println("Elpriser sorterade: " + Arrays.toString(elpriser));
    }

    // Metod för att hitta bästa laddningstid (4 timmar)
    private static void bestChargingTime() {
        if (elpriser.length < 4) {
            System.out.println("Inte tillräckligt med data för att hitta bästa laddningstid.");
            return;
        }

        double minSum = Double.MAX_VALUE;
        int bestStartIndex = 0;

        // Iterera genom alla möjliga 4-timmarsintervall
        for (int i = 0; i <= elpriser.length - 4; i++) {
            double sum = elpriser[i] + elpriser[i + 1] + elpriser[i + 2] + elpriser[i + 3];
            if (sum < minSum) {
                minSum = sum;
                bestStartIndex = i;
            }
        }

        System.out.println("Bästa laddningstiden är från timme " + (bestStartIndex + 1) + " till " + (bestStartIndex + 4));
        System.out.println("Med ett sammanlagt elpris på: " + minSum);
    }
}
