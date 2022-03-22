import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.util.Random;

public class sudoku {
    public static int n;
    public static int backtracks;
    public static int anzahlGefundeneLoesungen;

    public sudoku() {
        n = 0;
        backtracks = 0;
        anzahlGefundeneLoesungen = 0;
    }

    public static void main(String[] args) throws IOException {
        // test();
        long start = System.nanoTime();
        int[][]test = erstelle(9, 27, 1);
        long Ende = System.nanoTime() - start;
        System.out.println("Zeit für erstellen: "+Ende);
        start = System.nanoTime();
        loese(test);
        Ende = System.nanoTime() - start;
        System.out.println("Backtracks: " + backtracks);
        System.out.println("Hinweise: "+getHinweisAnzahl(test));
        System.out.print("Zeit " + Ende);
    }
    
    /**
     * Löst ein Sudoku mit beliebiger Feldlänge
     *
     * @param sudoku | das Sudokubrett als Array, dass gelöst werden soll
     * @return void | das gelöste Sudoku-Brett wird in der Konsole ausgegeben
     */
    public static void loese(int sudoku[][]) {
        int feldgröße = sudoku.length;
        for (int y = 0; y < feldgröße; y++) // Die Zeilen werden durchlaufen
        {
            for (int x = 0; x < feldgröße; x++) // Die Felder der jeweiligen Zeile werden durchlaufen
            {
                if (sudoku[y][x] == 0) // WENN noch keine Zahl auf dem Feld ist...
                {
                    for (int zahl = 1; zahl <= feldgröße; zahl++) // DANN probiere von n 1 bis 9 aus,
                    {
                        if (isGueltig(sudoku, zahl, y, x)) // ob es möglich ist n an der jeweiligen Stelle einzufügen
                        {
                            sudoku[y][x] = zahl; // wenn möglich, setze n ein
                            loese(sudoku); // Rekursionsschritt: loese() ruft sich selbst nochmal auf und versucht
                                           // die
                                           // nächste Stelle richtig zu füllen
                            backtracks++;
                            sudoku[y][x] = 0; // wenn der Methodenaufruf von loese(sudoku) durch return; (Zeile 52)
                                              // abgebrochen wird, setze den Wert des Feldes wieder auf 0 zurück
                            // nun werden alle restlichen Zahlen der For-Schleife ausprobiert
                        }
                    }
                    return; // Wenn keine der 9 Zahlen gepasst hat, breche die Methode ab
                }
            }
        }

        // Ausgabe der Lösung oder ggf. Lösungen
        System.out.println("Eine Lösung:");
        ausgeben(sudoku);
    }



    private static void rekZählen(int sudoku[][]) {
        int feldgröße = sudoku.length;
        for (int y = 0; y < feldgröße; y++) // Die Zeilen werden durchlaufen
        {
            for (int x = 0; x < feldgröße; x++) // Die Felder der jeweiligen Zeile werden durchlaufen
            {
                if (sudoku[y][x] == 0) // WENN noch keine Zahl auf dem Feld ist...
                {
                    for (int zahl = 1; zahl <= feldgröße; zahl++) // DANN probiere von n 1 bis 9 aus,
                    {
                        if (isGueltig(sudoku, zahl, y, x)) // ob es möglich ist n an der jeweiligen Stelle einzufügen
                        {
                            sudoku[y][x] = zahl; // wenn möglich, setze n ein
                            rekZählen(sudoku); // Rekursionsschritt: loese() ruft sich selbst nochmal auf und versucht
                                               // die
                                               // nächste Stelle richtig zu füllen
                            sudoku[y][x] = 0; // wenn der Methodenaufruf von loese(sudoku) durch return; (Zeile 52)
                                              // abgebrochen wird, setze den Wert des Feldes wieder auf 0 zurück
                            // nun werden alle restlichen Zahlen der For-Schleife ausprobiert
                        }
                    }
                    return; // Wenn keine der 9 Zahlen gepasst hat, breche die Methode ab
                }
            }
        }

        n++;
    }
    
    /**
     * Erstellt ein ungelöstes Sudoku-Brett mit einer beliebigen Anzahl von Hinweisen und Lösungen und einer beliebigen Feldgröße. 
     *
     * @param feldgröße | die Feldgröße, die das Sudoku haben soll
     * @param hinweise   | die Anzahl an Hinweisen, die gegeben sein sollen
     * @param anzahlLösungen  | die Anzahl an Lösungen, die vorhanden sein sollen
     * 
     * @return Das erstellte Sudoku-Brett als zweidimensionaler Array
     */
    public static int[][] erstelle(int feldgröße, int hinweise, int anzahlLösungen) {
        n = 0;
        int[][] sudoku = new int[feldgröße][feldgröße]; // ein leeres Sudoku-Brett einer beliebigen Feldgröße wird initialisiert
        while (n != anzahlLösungen) {   // Solange nicht genau die geforderte Menge an Lösungen gegeben sind
            n = 0;  // setze den Zähler auf 0
            sudoku = erstellehilfe(feldgröße, hinweise);    // erstelle ein neues Sudoku mit einer beliebigen Anzahl an Hinweisen
            rekZählen(sudoku);  // zähle die Lösungen
        }
        return sudoku;  // gebe das Sudoku-Brett zurück
    }

    private static int[][] erstellehilfe(int feldgröße, int hinweise) {
        int[][] sudokuBrett = new int[feldgröße][feldgröße];    // ein leeres Sudoku-Brett einer beliebigen Feldgröße wird initialisiert
        Random r = new Random();

        for (int i = 0; i < feldgröße; i++) {
            for (int j = 0; j < feldgröße; j++) {
                int zufallszahl = r.nextInt(feldgröße) + 1; // eine zufällige Zahl wird "ausgewürfelt"
                if (isGueltig(sudokuBrett, zufallszahl, i, j)) {    // Wenn der Zug möglich ist...
                    sudokuBrett[i][j] = zufallszahl;    // setze die Zufallszahl in das jeweilige Feld ein
                }
            }
        }
        for (int i = getHinweisAnzahl(sudokuBrett); hinweise < i; i--) {    // solange die Anzahl der Hinweise auf dem Sudoku-Brett kleiner als die geforderte Anzahl ist...
            int y = r.nextInt(feldgröße);   // zufällige y-Koordinate
            int x = r.nextInt(feldgröße);   // zufällige x-Koordinate

            if (sudokuBrett[y][x] != 0) {   // wenn das zufällige Feld keine 0 ist...
                sudokuBrett[y][x] = 0;  // setze das Feld auf 0
            } else {
                i++;    // Sonst wiederhole den Schritt mit einem anderen zufälligem Feld
            }
        }
        return sudokuBrett; // gebe das Sudoku-Bett zurück
    }

    /**
     * Prüft ob es regelkonform bzw. legal wäre, eine bestimmte Zahl in ein
     * bestimmtes Feld zu setzen
     *
     * @param sudoku | das Sudokubrett als Array, auf dem der Zug überprüft werden
     *               soll
     * @param zahl   | die Zahl (von 1-9), welche eingefügt werden soll
     * @param reihe  | Reihe und Spalte der zu überprüfenden Stelle
     * @param spalte
     * @return Ist es regelkonform, die Zahl (zahl) an einer beliebigen Stelle im
     *         Sudokubrett einzufügen ?
     */
    private static boolean isGueltig(int sudoku[][], int zahl, int reihe, int spalte) {
        int feldgroeße = sudoku.length;
        for (int i = 0; i < feldgroeße; i++) // wiederholt für alle Zahlen in der Reihe/Spalte
        {
            if (sudoku[reihe][i] == zahl) // wenn es die Zahl bereits in der jeweiligen Reihe gibt...
                return false; // dann gebe False zurück
            if (sudoku[i][spalte] == zahl) // wenn es die Zahl bereits in der jeweiligen Spalte gibt...
                return false; // dann gebe False zurück
        }

        int quadratzahl = 0;
        int y = 0;
        int x = 0;
        int x0 = 0;
        int y0 = 0;
        // Wenn die Quadratwurzel der Feldgröße gerade ist, ist dies die Länge eines Quadrates
        if (Math.sqrt(feldgroeße) % 1 == 0)
            quadratzahl = (int) Math.sqrt(feldgroeße);
        if (quadratzahl != 0) {
            y = quadratzahl;
            x = quadratzahl;
            x0 = spalte / x * x;
            y0 = reihe / y * y;
        } else if (feldgroeße % 2 == 0 && quadratzahl == 0) {   // Sonst: wenn die feldlänge gerade ist, werden die Breite und Höhe angenähert
            int j = 0;
            while (j * 2 != feldgroeße) {
                j++;
            }
            int n = 2;
            while (n < j) {
                n *= 2;
                j /= 2;
            }
            if (n * j != feldgroeße) {
                if (n * j == feldgroeße - 2) {
                    n = n + 1;
                } else {
                    n = n - 1;
                }
            }
            y = j;
            x = n;
            // Berechnung der Anfangskoordinaten des Rechtecks
            x0 = spalte / x * x;
            y0 = reihe / y * y;
        }

        for (int i = 0; i < y; i++) // wiederholt für alle Zahlen in der Zeile des Rechtecks
        {
            for (int j = 0; j < x; j++) // wiederholt für alle Zahlen in der Reihe des Rechtecks
            {
                if (sudoku[y0 + i][x0 + j] == zahl) // wenn es die Zahl bereits in dem Rechteck gibt...
                    return false; // dann gebe False zurück
            }
        }

        // Wenn es regelkonform ist, das heißt bis hierhin noch kein False zurückgegeben
        // wurde...
        return true; // dann gebe True zurück
    }

    public static int getHinweisAnzahl(int[][] arr) {
        int n = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (arr[i][j] != 0) {
                    n++;
                }
            }
        }
        return n;
    }

    public static void test() {
        int[][] sudoku = {
                { 0, 9, 0, 0, 0, 0, 0, 6, 0 },
                { 5, 0, 0, 0, 0, 0, 0, 0, 2 },
                { 0, 0, 0, 0, 9, 1, 0, 0, 8 },
                { 4, 5, 7, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 6, 0, 0, 3, 0, 0, 0 },
                { 0, 0, 2, 8, 0, 6, 0, 0, 0 },
                { 0, 0, 0, 0, 6, 0, 7, 4, 0 },
                { 0, 0, 0, 4, 0, 0, 0, 0, 0 },
                { 0, 2, 0, 7, 0, 0, 5, 0, 0 } };

        int[][] einfach = {
                { 0, 2, 0, 0, 0, 9, 6, 0, 0 },
                { 5, 8, 0, 0, 6, 2, 0, 0, 0 },
                { 7, 0, 6, 0, 3, 0, 0, 1, 9 },
                { 4, 7, 2, 0, 0, 0, 5, 6, 0 },
                { 9, 5, 0, 6, 0, 0, 3, 4, 2 },
                { 0, 3, 8, 0, 0, 4, 1, 0, 7 },
                { 8, 1, 7, 0, 0, 5, 0, 0, 6 },
                { 3, 0, 0, 7, 0, 0, 0, 0, 1 },
                { 0, 0, 0, 0, 9, 0, 0, 7, 0 } };

        int[][] mittel = {
                { 0, 0, 2, 0, 0, 7, 0, 9, 6 },
                { 7, 0, 5, 0, 9, 0, 0, 1, 8 },
                { 1, 0, 0, 0, 0, 4, 7, 0, 0 },
                { 0, 0, 9, 7, 0, 0, 1, 0, 5 },
                { 0, 0, 0, 0, 2, 8, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 5, 0, 6, 2 },
                { 0, 0, 0, 6, 7, 2, 0, 0, 1 },
                { 0, 0, 0, 8, 0, 0, 0, 4, 0 },
                { 0, 0, 3, 0, 4, 0, 0, 2, 0 } };

        int[][] schwer = {
                { 8, 5, 0, 0, 0, 2, 4, 0, 0 },
                { 7, 2, 0, 0, 0, 0, 0, 0, 9 },
                { 0, 0, 4, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 7, 0, 0, 2 },
                { 3, 0, 5, 0, 0, 0, 9, 0, 0 },
                { 0, 4, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 8, 0, 0, 7, 0 },
                { 0, 1, 7, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 3, 6, 0, 4, 0 } };

        int[][] schwer2 = {
                { 5, 0, 0, 0, 0, 8, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 4, 0, 0 },
                { 0, 0, 8, 0, 9, 0, 1, 0, 7 },
                { 0, 0, 7, 0, 0, 0, 0, 4, 0 },
                { 2, 0, 0, 1, 0, 0, 7, 0, 6 },
                { 0, 0, 0, 0, 6, 0, 0, 5, 0 },
                { 0, 3, 0, 2, 0, 0, 0, 0, 0 },
                { 0, 0, 5, 0, 1, 0, 8, 0, 9 },
                { 0, 0, 0, 0, 0, 0, 0, 6, 0 } };

        int[][] schwer3 = {
                { 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                { 0, 0, 0, 0, 0, 0, 0, 2, 3 },
                { 0, 0, 4, 0, 0, 5, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0, 0, 0 },
                { 2, 0, 0, 0, 3, 0, 6, 0, 0 },
                { 0, 0, 7, 0, 0, 0, 5, 8, 0 },
                { 0, 0, 0, 0, 6, 7, 0, 0, 0 },
                { 0, 1, 0, 0, 0, 4, 0, 0, 0 },
                { 5, 2, 0, 0, 0, 0, 0, 0, 0 } };

        int[][] worstcase = {
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 3, 0, 8, 5 },
                { 0, 0, 1, 0, 2, 0, 0, 0, 0 },
                { 0, 0, 0, 5, 0, 7, 0, 0, 0 },
                { 0, 0, 4, 0, 0, 0, 1, 0, 0 },
                { 0, 9, 0, 0, 0, 0, 0, 0, 0 },
                { 5, 0, 0, 0, 0, 0, 0, 7, 3 },
                { 0, 0, 2, 0, 1, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 4, 0, 0, 0, 9 } };

        loese(sudoku);
        System.out.println("Hinweise: " + getHinweisAnzahl(sudoku));
        System.out.println("Backtracks: " + backtracks);

        backtracks = 0;
        loese(einfach);
        System.out.println("Hinweise: " + getHinweisAnzahl(einfach));
        System.out.println("Backtracks: " + backtracks);

        backtracks = 0;
        loese(mittel);
        System.out.println("Hinweise: " + getHinweisAnzahl(mittel));
        System.out.println("Backtracks: " + backtracks);

        backtracks = 0;
        loese(schwer);
        System.out.println("Hinweise: " + getHinweisAnzahl(schwer));
        System.out.println("Backtracks: " + backtracks);

        backtracks = 0;
        loese(schwer2);
        System.out.println("Hinweise: " + getHinweisAnzahl(schwer2));
        System.out.println("Backtracks: " + backtracks);

        backtracks = 0;
        loese(schwer3);
        System.out.println("Hinweise: " + getHinweisAnzahl(schwer3));
        System.out.println("Backtracks: " + backtracks);

        backtracks = 0;
        loese(worstcase);
        System.out.println("Hinweise: " + getHinweisAnzahl(worstcase));
        System.out.println("Backtracks: " + backtracks);
    }

    /**
     * liest ein 9*9 Sudokubrett aus einem Textdokument ein und gibt es als Array
     * aus
     *
     * @return Das eingelesene Sudokubrett als Array
     */
    public static int[][] sudokuLesen() throws IOException {
        File file = new File("sudokuEinlesen.txt");
        Scanner txt = new Scanner(file);
        int[][] s = new int[9][9];

        while (txt.hasNext()) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    s[i][j] = txt.nextInt();
                }
                if (txt.hasNextLine()) {
                    txt.nextLine();
                }
            }
        }
        txt.close();

        anzahlGefundeneLoesungen = 0;
        System.out.println("Anzahl der möglichen Lösungen: " + anzahlGefundeneLoesungen);
        System.out.println("Anzahl der Hinweise: " + getHinweisAnzahl(s));


        System.out.println("Eingelesenes Sudoku Feld:");
        ausgeben(s);

        return s;
    }

    // gibt ein Array bzw. das Sudokubrett in einer übersichtlichen Matrix wieder
    public static void ausgeben(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("---------------------");
        System.out.println();
    }
}
