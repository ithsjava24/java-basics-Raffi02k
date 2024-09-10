package org.example;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class App {

    public static int[] elpriser = new int[24]; // Array för att lagra elpriser för varje timme på dygnet

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Scanner för inmatning
        while (true) { // Meny-loop som körs tills användaren väljer att avsluta
            printMenu(); // Skriv ut menyn

            if (scanner.hasNextLine()) {
                String choice = scanner.nextLine(); // Läs användarens val

                switch (choice.toLowerCase()) { // Hantera valet
                    case "1":
                        inputPrices(scanner); // Hantera inmatning av elpriser
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
                        System.out.print("Avslutar programmet...\n"); // Avsluta programmet
                        scanner.close(); // Stäng scannern innan avslut
                        return; // Avsluta main-metoden
                    default:
                        System.out.print("Ogiltigt val, försök igen.\n"); // Hantera ogiltig inmatning
                }
            } else {
                break;
            }
        }
    }

    // Skriv ut menyn
    private static void printMenu() {
        System.out.print("Elpriser\n");
        System.out.print("========\n");
        System.out.print("1. Inmatning\n");
        System.out.print("2. Min, Max och Medel\n");
        System.out.print("3. Sortera\n");
        System.out.print("4. Bästa Laddningstid (4h)\n");
        System.out.print("e. Avsluta\n");
        System.out.print("Välj ett alternativ: \n");
    }

    // Metod för inmatning av elpriser
    private static void inputPrices(Scanner scanner) {
        System.out.print("Ange elpriser för varje timme på dygnet (i öre per kWh):\n");
        for (int i = 0; i < 24; i++) {
            System.out.print("Ange elpris för timme " + i + "-" + (i + 1) + ": ");
            if (scanner.hasNextInt()) {
                elpriser[i] = scanner.nextInt(); // Läs in varje elpris
            } else {
                System.out.print("Felaktig inmatning, försök igen.\n");
                scanner.next(); // Förbrukar ogiltig inmatning
                i--; // Gå tillbaka ett steg
            }
        }
        scanner.nextLine(); // Rensa scanner-buffer
    }

    // Metod för att beräkna min, max och medelvärde
    private static void calculateMinMaxAvg() {
        if (elpriser.length == 0) {
            System.out.print("Inga elpriser inmatade.\n");
            return;
        }

        int min = Arrays.stream(elpriser).min().orElse(Integer.MAX_VALUE);
        int max = Arrays.stream(elpriser).max().orElse(Integer.MIN_VALUE);
        double avg = Arrays.stream(elpriser).average().orElse(Double.NaN);

        // Hitta timmarna för min och max priser
        String minHour = "";
        String maxHour = "";
        for (int i = 0; i < elpriser.length; i++) {
            if (elpriser[i] == min) {
                minHour += (i + "-" + (i + 1) + " ");
            }
            if (elpriser[i] == max) {
                maxHour += (i + "-" + (i + 1) + " ");
            }
        }

        // Uppdaterad utskrift
        System.out.printf("Lägsta pris: %s, %d öre/kWh%n", minHour.trim(), min);
        System.out.printf("Högsta pris: %s, %d öre/kWh%n", maxHour.trim(), max);
        System.out.printf("Medelpris: %.2f öre/kWh%n", avg);
    }

    // Metod för att sortera elpriser
    private static void sortPrices() {
        if (elpriser.length == 0) {
            System.out.print("Inga elpriser inmatade.\n");
            return;
        }

        // Skapa en array av timme och pris
        Integer[][] timeAndPrices = new Integer[24][2];
        for (int i = 0; i < elpriser.length; i++) {
            timeAndPrices[i][0] = i; // Timme
            timeAndPrices[i][1] = elpriser[i]; // Pris
        }

        // Sortera efter pris i fallande ordning
        Arrays.sort(timeAndPrices, Comparator.comparingInt(a -> -a[1]));

        System.out.print("Elpriser sorterade från dyrast till billigast:\n");
        for (Integer[] timePrice : timeAndPrices) {
            System.out.printf("%02d-%02d %d öre%n", timePrice[0], timePrice[0] + 1, timePrice[1]);
        }
    }

    // Metod för att hitta bästa laddningstid (4 timmar)
    private static void bestChargingTime() {
        if (elpriser.length < 4) {
            System.out.print("Inte tillräckligt med data för att hitta bästa laddningstid.\n");
            return;
        }

        int minSum = Integer.MAX_VALUE;
        int bestStartIndex = 0;

        // Iterera genom alla möjliga 4-timmarsintervall
        for (int i = 0; i <= elpriser.length - 4; i++) {
            int sum = elpriser[i] + elpriser[i + 1] + elpriser[i + 2] + elpriser[i + 3];
            if (sum < minSum) {
                minSum = sum;
                bestStartIndex = i;
            }
        }

        double avg = minSum / 4.0;
        System.out.printf("Påbörja laddning klockan %02d%n", bestStartIndex);
        System.out.printf("Medelpris 4h: %.1f öre/kWh%n", avg);
    }
}
