package org.example;


import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
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
                    case "5":
                        visualizePrices(); // Visualisera elpriserna
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
        System.out.println("""
                Elpriser
                ========
                1. Inmatning
                2. Min, Max och Medel
                3. Sortera
                4. Bästa Laddningstid (4h)
                5. Visualisering
                e. Avsluta
                """);
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
        Locale.setDefault(new Locale("sv", "SE"));
        if (elpriser.length == 0) {
            System.out.print("Inga elpriser inmatade.\n");
            return;
        }


        int min = Arrays.stream(elpriser).min().orElse(Integer.MAX_VALUE);
        int max = Arrays.stream(elpriser).max().orElse(Integer.MIN_VALUE);
        double avg = Arrays.stream(elpriser).average().orElse(Double.NaN);


        // Hitta första förekomsten av timme för min och max priser
        String minHour = "";
        String maxHour = "";
        for (int i = 0; i < elpriser.length; i++) {
            if (elpriser[i] == min && minHour.isEmpty()) {
                minHour = formatTime(i);
            }
            if (elpriser[i] == max && maxHour.isEmpty()) {
                maxHour = formatTime(i);
            }
            // Bryt loopen om båda min och max timmar har hittats
            if (!minHour.isEmpty() && !maxHour.isEmpty()) {
                break;
            }
        }


        // Uppdaterad utskrift
        System.out.printf("Lägsta pris: %s, %d öre/kWh\n", minHour, min);
        System.out.printf("Högsta pris: %s, %d öre/kWh\n", maxHour, max);
        System.out.printf("Medelpris: %.2f öre/kWh\n", avg);
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
            System.out.printf("%02d-%02d %d öre\n", timePrice[0], timePrice[0] + 1, timePrice[1]);
        }
    }


    // Metod för att hitta bästa laddningstid (4 timmar)
    private static void bestChargingTime() {
        Locale.setDefault(new Locale("sv", "SE"));
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
        System.out.printf("Påbörja laddning klockan %02d\n", bestStartIndex);
        System.out.printf("Medelpris 4h: %.1f öre/kWh\n", avg);
    }


    // Metod för att visualisera elpriser
    private static void visualizePrices() {
        int maxPris = Arrays.stream(elpriser).max().orElse(1);
        int minPris = Arrays.stream(elpriser).min().orElse(1);
        final int HEIGHT = 6; // Antal rader för att representera grafen
        final int COLUMN_COUNT = elpriser.length;
        final float DIFFERENCE = (maxPris - minPris) / (HEIGHT - 1f);

        System.out.println("Visualisering av elpriser:");

        // Loopa genom raderna, från den högsta till den lägsta
        for (int i = HEIGHT; i > 0; i--) {
            StringBuilder output = new StringBuilder();
            int lowerBound = (i == 1) ? minPris : (int) (maxPris - (HEIGHT - i) * DIFFERENCE);

            int maxLength = Integer.toString(maxPris).length();
            int minLength = Integer.toString(minPris).length();
            int longest = Math.max(maxLength, minLength);

            // Skriv ut maxvärde för den översta raden och minvärde för den nedersta
            if (i == HEIGHT) {
                String spaces = maxLength < longest ? addSpaces(longest - maxLength) : "";
                output.append(spaces).append(maxPris).append("|");
            } else if (i == 1) {
                String spaces = minLength < longest ? addSpaces(longest - minLength) : "";
                output.append(spaces).append(minPris).append("|");
            } else {
                output.append(addSpaces(longest)).append("|");
            }

            // Rita "x" om priset för varje kolumn är högre eller lika med den aktuella nivån
            for (int j = 0; j < COLUMN_COUNT; j++) {
                int currentPrice = elpriser[j];
                if (currentPrice >= lowerBound) {
                    output.append("  x");
                } else {
                    output.append("   ");
                }
            }
            System.out.println(output);
        }

        // Skriv ut en linje under grafen
        System.out.print("   |------------------------------------------------------------------------\n");
        System.out.print("   | 00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23\n");
    }

    // Hjälpmetod för att lägga till mellanslag
    private static String addSpaces(int count) {
        return " ".repeat(count);
    }


    // Metod för att formatera tiden
    private static String formatTime(int hour) {
        return String.format("%02d-%02d", hour, hour + 1);
    }
}