package org.example;

import java.util.Scanner;

@SuppressWarnings("LanguageDetectionInspection")
public class App {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean runApplication = true;
//        int [] pricePerHour = new int[24];
//        int[] pricePerHour = {530, 530, 530, 432, 450, 500, 334, 400, 430, 350, 380, 401, 335, 431, 335, 236, 235, 234, 150, 200, 139, 138, 137, 40};
        int[] pricePerHour = {100, 99, 78, 77, 55, 54, 33, 32, 10, 9, 0, -1, -12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        while (runApplication) {

            printMenu();

            String userAnswer = scanner.nextLine();

            switch (userAnswer) {
                case "1" -> pricePerHour = setPricePerHour(scanner);
                case "2" -> minMaxMean(pricePerHour);
                case "3" -> sorted(pricePerHour);
                case "4" -> bestLoadingTime(pricePerHour, 4);
                case "5" -> visualization(pricePerHour);
                case "e", "E" -> runApplication = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Prints menu
     */
    public static void printMenu() {

        String menu = """
                Elpriser
                ========
                1. Inmatning
                2. Min, Max och Medel
                3. Sortera
                4. Bästa Laddningstid (4h)
                5. Visualisering
                e. Avsluta
                """;

        System.out.print(menu);
    }

    /**
     * 1. Inmatning
     * Inmatning ska ske med hela ören, ex. 50, 102 eller 680 öre per kW/h.
     * Priset är per 1h period på 1 dygn, ex. mellan kl.00-01, 01-02, 02-03 osv.
     */
    public static int[] setPricePerHour(Scanner scanner) {

        int[] pricePerHour = new int[24];

        for (int i = 0; i < pricePerHour.length; i++) {
            String number = scanner.nextLine();
            pricePerHour[i] = Integer.parseInt(number);
        }

        return pricePerHour;
    }

    /**
     * 2. Min Max Medel
     * Skriver ut lägsta samt högsta priset under dygnet
     * samt vilka timmar dessa infaller.
     * Dygnets medelpris beräknas och presenteras.
     */
    public static void minMaxMean(int[] array) {

        int min = min(array);
        int max = max(array);
        double mean = mean(array);
        int minIndex = -1;
        int maxIndex = -1;

        // Looparna hämtar index för första företeelsen av min och max value i arrayen.
        for (int i = 0; i < array.length; i++) {
            if (array[i] == min) {
                minIndex = i;
            } else if (array[i] == max) {
                maxIndex = i;
            }
        }

        System.out.printf("Lägsta pris: %02d-%02d, %d öre/kWh\n", minIndex, minIndex + 1, min);
        System.out.printf("Högsta pris: %02d-%02d, %d öre/kWh\n", maxIndex, maxIndex + 1, max);
        System.out.printf("Medelpris: %.2f öre/kWh\n", mean);

        /* // Alternativ 2: Allt beräknas i metoden, ej anrop till andra.
//        int min = Integer.MAX_VALUE;
//        int max = Integer.MIN_VALUE;
//        int minIndex = 0;
//        int maxIndex = 0;
//        double sum = 0.0;
//
//        for (int i = 0; i < array.length; i++) {
//
//            if (array[i] < min) {
//                min = array[i];
//                minIndex = i;
//            }
//
//            if (array[i] > max) {
//                max = array[i];
//                maxIndex = i;
//            }
//
//            sum += array[i];
//        }
//
//        System.out.printf("Lägsta pris: %02d-%02d, %d öre/kWh\n", minIndex, minIndex+1, min);
//        System.out.printf("Högsta pris: %02d-%02d, %d öre/kWh\n", maxIndex, maxIndex+1, max);
//        System.out.printf("Medelpris: %.2f öre/kWh\n", sum / array.length);
        */
    }

    /**
     * 3. Sortera
     * Bubble-sorterar parameter int [] arrayen samt dess original index i fallande ordning.
     * Skriver ut båda arrayerna.
     *
     * @param array: int [] array som ska sorteras i fallande ordning.
     */
    public static void sorted(int[] array) {

        // Skapar en index array och initierar den men index-värderna.
        int[] index = new int[array.length];

        for (int i = 0; i < array.length; i++) {
            index[i] = i;
        }

        // Bubble-sort parameter arrayen samt index arrayen i fallande ordning
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {

                if (array[j] < array[j + 1]) {

                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;

                    int tempIndex = index[j];
                    index[j] = index[j + 1];
                    index[j + 1] = tempIndex;
                }
            }
        }

        for (int i = 0; i < array.length; i++) {
            System.out.printf("%02d-%02d %d öre\n", index[i], index[i] + 1, array[i]);
        }
    }

    /**
     * 4. Bästa laddningstid (4h)
     * Beräknar vilken starttid som ger lägst medelvärde över angedd tidsperiod.
     * Skriver ut starttid samt medelvärdet för tidsperioden.
     *
     * @param array:      int [], måste vara jämt delbar med angedd tidsperiod.
     * @param timePeriod: int, tidperiodens antal timmar.
     * @throws IllegalArgumentException: kastas om array inte är jämt delbar på timePeriod.
     */
    public static void bestLoadingTime(int[] array, int timePeriod) throws IllegalArgumentException {

        // Kolla att arrayen är jämt delbar på angedd tidsperiod, annars kasta ett undantag.
        if (array.length % timePeriod != 0) {
            throw new IllegalArgumentException("Array must be a multiple of " + timePeriod);
        }

        int[] periodArray = new int[timePeriod];
        double minTimePeriod = Double.MAX_VALUE;
        int index = 0;

        // Loopa igenom hela arrayen minus sista indexen vars längd är timeperiod (undviker ArrayIndexOutOfBounce).
        for (int i = 0; i < array.length - timePeriod; i++) {

            int k = 0;                                          // Använd k som increment variabel, nollställ varje varv.

            for (int j = i; j < i + timePeriod; j++) {             // Loopa timePeriod antal varv för att hämta rätt antal element.
                periodArray[k++] = array[j];
            }

            double meanPerTimePeriod = mean(periodArray);              // Beräkna mean för varje timePeriod

            if (meanPerTimePeriod < minTimePeriod) {              // Kolla om denna tidsperiods mean är mindre än tidigare tidsperioder.
                minTimePeriod = meanPerTimePeriod;
                index = i;                                      // Spara index för tidsperioden med lägsta mean.
            }
        }

        System.out.printf("Påbörja laddning klockan %d\nMedelpris 4h: %.1f öre/kWh\n", index, minTimePeriod);

        /* // Alternativ 2 - EJ arraylängd anpassad, fungerar endast för 4h i sträck.

        double min = Double.MAX_VALUE;
        int index = 0;

        for (int i = 0; i < array.length-4; i++) {
            double sum = array[i] + array[i+1] + array[i+2] + array[i+3];

            if (sum < min){
                min = sum;
                index = i;
            }
        }

        System.out.printf("Påbörja laddning klockan %d\nMedelpris 4h: %.1f öre/kWh\n", index, min/4);
         */
    }

    /**
     * 5. Visualisering
     * Ett normalt konsol fönster har 80 teckens bredd och här vill vi utnyttja det för att visualisera priserna under
     * dygnet. Lägg till ett 5:e alternativ för detta i din meny som heter 5. Visualisering
     * Exempel på hur visualiseringen kan se ut med 76 teckens bredd. Beroende på vilket prisintervall som gäller
     * kan skalan behöva justeras dynamiskt.
     */
    public static void visualization(int[] array) {

        int tableHeight = 6;
        int tableWidth = array.length;
        char[][] table = new char[tableHeight][tableWidth];
        int minValue = min(array);
        int maxValue = max(array);
        double rangePerStep = (double) (maxValue - minValue) / (tableHeight-1);
        double lowestAcceptedValue = maxValue;

        for (int i = 0; i < tableHeight; i++) {                                         // skapa en matris där tecknet 'x' sätts ut i matrisen om värdet i array uppnår minsta värde för den raden.
            for (int j = 0; j < tableWidth; j++) {                                      // Gå igenom samtliga element per rad.

                if (array[j] >= (int)lowestAcceptedValue)                               // typecasta lowestAcceptedValue till int för att få samma range som testet.
                    table[i][j] = 'x';
                else
                    table[i][j] = ' ';
            }
            lowestAcceptedValue -= rangePerStep;                                        // för varje rad, sänk minsta värdet.
        }
                                                                                        // Skriver ut matrisen. Uppifrån och ner.
        for (int i = 0; i < tableHeight + 2; i++) {                                     // Höjd = antal rader i matrisen + 1 rad avdelare + 1 rad klockslag

            if (i == 0)
                System.out.printf("%3d|", maxValue);                                // Skriv ut de första 5 tecknen i tabellen ( 3 tecken högsta, lägsta eller inget värde + " |" )
            else if (i == tableHeight - 1)
                System.out.printf("\n%3d|", minValue);
            else
                System.out.printf("\n%3s|", " ");

            for (int j = 0; j < tableWidth; j++) {                                      // Skriv ut table per rad

                if (i < tableHeight)
                    System.out.printf("  %c", table[i][j]);
                else if (i == tableHeight)
                    System.out.printf("%s", "---");
                else
                    System.out.printf(" %02d", j);
            }
        }
        System.out.print("\n");
    }

    /**
     * Min
     * Hittar och returnerar längsta numret i en array.
     *
     * @param array: int[] array där lägsta numret ska hittas
     * @return min: int, lägsta numret.
     */
    public static int min(int[] array) {

        int min = Integer.MAX_VALUE;

        for (int i : array) {
            min = Math.min(min, i);
        }

        return min;
    }

    /**
     * Max
     * Hittar och returnerar högsta numret i en array.
     *
     * @param array: int[] array där högsta numret ska hittas
     * @return min: int, högsta numret.
     */
    public static int max(int[] array) {

        int max = Integer.MIN_VALUE;

        for (int i : array) {
            max = Math.max(max, i);
        }

        return max;
    }

    /**
     * Mean
     * Beräknar och returnerar medelvärdet av summan av en int array.
     *
     * @param array: int []
     * @return mean: double
     */
    public static double mean(int[] array) {
        return (double) sum(array) / array.length;
    }

    /**
     * Sum
     * Beräknar och returnerar summan för en array.
     *
     * @param array: int []
     * @return sum: int
     */
    public static int sum(int[] array) {

        int sum = 0;

        for (int i : array) {
            sum += i;
        }

        return sum;
    }
}
