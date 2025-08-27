import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        DBHelper db = new DBHelper();

        // Verbindung öffnen
        System.out.println("---Verbindung herstellen---");
        db.connect();

        // Beispiel: Tabelle Kunden anlegen
        System.out.println("\n---Erstellen einer Tabelle---");
        db.createTableKunden();

        // Beispiel: Neues Spiel einfügen
        System.out.println("\n ---Neue Spiele einzeln hinzufügen---");
        Spiel s1 = new Spiel("Life is Strange", "Choice Based", 59.99);
        Spiel s2 = new Spiel("Final Fantasy XV", "Fantasy", 49.99);

        db.insertNeuesSpiel(s1);
        db.insertNeuesSpiel(s2);

        // Beispiel: Mehrere Spiele auf einmal einfügen
        System.out.println("\n ---Neue Spiele gleichzeitig hinzufügen---");
            // Liste von Spielen vorbereiten
            ArrayList<Spiel> neueSpiele = new ArrayList<>();
            neueSpiele.add(new Spiel("Final Fantasy XVI", "Fantasy", 69.99));
            neueSpiele.add(new Spiel("Elden Ring", "Action RPG", 59.99));
            neueSpiele.add(new Spiel("Pikmin 4", "Strategy", 49.99));

            // Alle Spiele auf einmal einfügen
            db.insertSpiele(neueSpiele);

        // Beispiel: Update Preis
        System.out.println("\n---Preis Update---");
        db.updateSpielPreis(2, 59.99);

        // Beispiel: Spiel löschen
        System.out.println("\n---Spiel löschen---");
        db.deleteSpiel(16);

        // Beispiel: ORM - Spiele nach Genre abfragen
        System.out.println("\n---Spiele nach Genre abfragen---");
        ArrayList<Spiel> racingGames = db.getSpieleByGenre("Fantasy");
        System.out.println("Gefundene Spiele:");
        for (Spiel sp : racingGames) {
            System.out.println(sp);
        }

        // Beispiel: Aggregatfunktion
        System.out.println("\n---Durchschnittspreis aller Spiele---");
        double avgPreis = db.getDurchschnittspreisAllerSpiele(); //Für den Fall, dass der Wert weiterverwendet wird
        System.out.println("Durchschnittspreis aller Spiele: " + avgPreis);

        //Beispiel: Aggregatfunktion 2
        System.out.println("\n---Kunde mit den meisten Käufen---");
        TopKundeUmsatz top = db.getKundeMitHoechstemUmsatz();
        if (top != null) {
            System.out.printf("Top-Kunde (Umsatz): %s – %.2f €%n",
                    top.getKunde().getName(), top.getUmsatz());
        } else {
            System.out.println("Keine Käufe vorhanden.");
        }



        //Beispiel: Transaktion
        System.out.println("\n---Transaktionen---");
        System.out.println("Vor der Transaktion:");
        System.out.println(db.getKundeById(1));
        System.out.println(db.getKundeById(2));

        // 🔄 Beispiel: 30 Punkte von Ventus (ID 2) → Roxas (ID 1)
        db.transferBonuspunkte(2, 1, 30);

        System.out.println("\nNach der Transaktion:");
        System.out.println(db.getKundeById(1));
        System.out.println(db.getKundeById(2));

        // Beispiel: Metadaten
        System.out.println("\n---Metadaten ausgeben---");
        db.printMetaDaten();

        // Verbindung schließen
        System.out.println("\n---Verbindung schließen---");
        db.disconnect();
    }
}
