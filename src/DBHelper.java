import java.sql.*;
import java.util.ArrayList;

// Diese Klasse kapselt ALLE Datenbankoperationen für das Videospielgeschäft
public class DBHelper {
    private Connection conn;

    // Verbindung zur SQLite-Datenbank herstellen
    public void connect() {
        String url = "jdbc:sqlite:/Users/nadinejesernik/Dropbox/Mac/Desktop/Databases/DBPExam.db";
        try {
            conn = DriverManager.getConnection(url);

            // Fremdschlüssel-Prüfung aktivieren (SQLite macht das nicht automatisch!)
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }

            System.out.println("✅ Verbindung hergestellt (Foreign Keys aktiviert).");
        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Verbinden: " + e.getMessage());
        }
    }


    // Verbindung sauber schließen
    public void disconnect() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("✅ Verbindung geschlossen.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Schließen: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------
    // Aufgabe 3: Tabelle per JDBC erstellen
    // ------------------------------------------------------------
    public void createTableKunden() {
        //Auf bereits vorhandene Tabelle anhand von Metadaten prüfen
        try {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet tables = meta.getTables(null, null, "Kunden", null)) {
                if (tables.next()) {
                    // Tabelle existiert schon
                    System.out.println("⚠️ Tabelle 'Kunden' existiert bereits!");
                    return;
                }
            }

            // Tabelle existiert nicht → jetzt anlegen
            String sql = "CREATE TABLE Kunden (" +
                    "KId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Name TEXT NOT NULL, " +
                    "Email TEXT UNIQUE, " +
                    "Bonuspunkte INTEGER DEFAULT 0 CHECK(Bonuspunkte >= 0))";

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sql);
                System.out.println("✅ Tabelle Kunden wurde neu erstellt.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler bei createTableKunden: " + e.getMessage());
        }
    }



    // ------------------------------------------------------------
    // Aufgabe 4: CRUD-Beispiele
    // ------------------------------------------------------------

    // INSERT: Neues Spiel hinzufügen
    // INSERT: Neues Spiel hinzufügen (mit Erfolgsmeldung)
    public int insertNeuesSpiel(Spiel spiel) {
        String sql = "INSERT INTO Spiele (Titel, Genre, Preis) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, spiel.getTitel());
            ps.setString(2, spiel.getGenre());
            ps.setDouble(3, spiel.getPreis());
            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        spiel.setId(id);
                        System.out.println("✅ Spiel erfolgreich eingefügt: " + spiel.getTitel() +
                                " (ID " + id + ")");
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Fehler Insert: " + e.getMessage());
        }
        return -1;
    }


    // UPDATE: Preis eines Spiels ändern
    public int updateSpielPreis(int spielId, double neuerPreis) {
        String sql = "UPDATE Spiele SET Preis=? WHERE SpielId=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, neuerPreis);
            ps.setInt(2, spielId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Preis Update erfolgreich für Spiel ID " + spielId +
                        ". Neuer Preis: " + neuerPreis + " €");
            } else {
                System.out.println("⚠️ Spiel ID " + spielId + " nicht gefunden. Preis Update nicht erfolgreich.");
            }
            return rows;
        } catch (SQLException e) {
            System.out.println("❌ Fehler Update: " + e.getMessage());
        }
        return 0;
    }


    // DELETE: Spiel löschen
    public int deleteSpiel(int spielId) {
        String sql = "DELETE FROM Spiele WHERE SpielId=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, spielId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Spiel mit ID " + spielId + " wurde gelöscht.");
            } else {
                System.out.println("⚠️ Spiel mit ID " + spielId + " nicht gefunden. Nichts gelöscht.");
            }
            return rows;
        } catch (SQLException e) {
            System.out.println("❌ Fehler Delete: " + e.getMessage());
        }
        return 0;
    }


    // ------------------------------------------------------------
    // Aufgabe 6: ORM-Beispiele (Arbeiten mit Listen von Objekten)
    // ------------------------------------------------------------
    public ArrayList<Spiel> getSpieleByGenre(String genre) {
        ArrayList<Spiel> result = new ArrayList<>();
        String sql = "SELECT * FROM Spiele WHERE Genre=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, genre);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Aus DB-Spalte -> Java-Objekt "Spiel"
                result.add(new Spiel(
                        rs.getInt("SpielId"),
                        rs.getString("Titel"),
                        rs.getString("Genre"),
                        rs.getDouble("Preis")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Fehler ORM: " + e.getMessage());
        }
        return result;
    }

    // Mehrere Spiele auf einmal einfügen

    public void insertSpiele(ArrayList<Spiel> spiele) {
        int erfolge = 0;
        int fehler = 0;

        for (Spiel s : spiele) {
            int id = insertNeuesSpiel(s); // gibt -1 zurück, wenn Insert fehlschlägt
            if (id != -1) {
                erfolge++;
            } else {
                fehler++;
            }
        }

        System.out.println("📊 Zusammenfassung: " + erfolge + " Spiele erfolgreich eingefügt, " + fehler + " fehlgeschlagen.");
    }


    // ------------------------------------------------------------
    // Aufgabe 7: Aggregatfunktionen
    // ------------------------------------------------------------
    public double getDurchschnittspreisAllerSpiele() {
        String sql = "SELECT AVG(Preis) as avgPreis FROM Spiele";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("avgPreis");
            }
        } catch (SQLException e) {
            System.out.println("❌ Fehler Aggregat: " + e.getMessage());
        }
        return 0;
    }

    // Kunde mit den meisten Käufen
    // Liefert den Kunden mit dem höchsten Umsatz (Summe der Preise seiner Käufe)
    public TopKundeUmsatz getKundeMitHoechstemUmsatz() {
        String sql = """
        SELECT k.KId, k.Name, k.Email, k.Bonuspunkte,
               SUM(s.Preis) AS Umsatz
        FROM "Käufe" ku
        JOIN Kunden k ON ku.KundeId = k.KId
        JOIN Spiele  s ON ku.SpielId = s.SpielId
        GROUP BY k.KId, k.Name, k.Email, k.Bonuspunkte
        ORDER BY Umsatz DESC, k.KId ASC
        LIMIT 1
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Kunde k = new Kunde(
                        rs.getInt("KId"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        rs.getInt("Bonuspunkte")
                );
                double umsatz = rs.getDouble("Umsatz");
                return new TopKundeUmsatz(k, umsatz);
            }
        } catch (SQLException e) {
            System.out.println("❌ getKundeMitHoechstemUmsatz: " + e.getMessage());
        }
        return null; // keine Käufe vorhanden
    }


    // ------------------------------------------------------------
    // Aufgabe 8: Transaktion – Bonuspunkte übertragen
    // ------------------------------------------------------------
    public void transferBonuspunkte(int vonId, int zuId, int punkte) {
        String abbuch = "UPDATE Kunden SET Bonuspunkte = Bonuspunkte - ? " +
                "WHERE KId = ? AND Bonuspunkte >= ?";
        String gutschrift = "UPDATE Kunden SET Bonuspunkte = Bonuspunkte + ? WHERE KId = ?";

        try {
            conn.setAutoCommit(false);

            int rowsAbbuch;
            try (PreparedStatement ps = conn.prepareStatement(abbuch)) {
                ps.setInt(1, punkte);
                ps.setInt(2, vonId);
                ps.setInt(3, punkte);           // nur abbuchen, wenn genug Punkte da sind
                rowsAbbuch = ps.executeUpdate();
            }

            if (rowsAbbuch != 1) {
                throw new SQLException("Abbuchung fehlgeschlagen (zu wenig Punkte oder Kunde nicht gefunden).");
            }

            int rowsGutschrift;
            try (PreparedStatement ps = conn.prepareStatement(gutschrift)) {
                ps.setInt(1, punkte);
                ps.setInt(2, zuId);
                rowsGutschrift = ps.executeUpdate();
            }

            if (rowsGutschrift != 1) {
                throw new SQLException("Gutschrift fehlgeschlagen (Zielkunde nicht gefunden).");
            }

            conn.commit();
            System.out.println("✅ Transaktion erfolgreich!");
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ignore) {}
            System.out.println("❌ Rollback wegen Fehler: " + e.getMessage());
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignore) {}
        }
    }


    // Einzelnen Kunden per ID laden (oder null, wenn es ihn nicht gibt)
    public Kunde getKundeById(int kundenId) {
        String sql = "SELECT KId, Name, Email, Bonuspunkte FROM Kunden WHERE KId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kundenId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Kunde(
                            rs.getInt("KId"),
                            rs.getString("Name"),
                            rs.getString("Email"),
                            rs.getInt("Bonuspunkte")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Fehler getKundeById: " + e.getMessage());
        }
        return null;
    }


    // ------------------------------------------------------------
    // Aufgabe 9: Metadaten
    // ------------------------------------------------------------
    public void printMetaDaten() {
        try {
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("DB Name: " + meta.getDatabaseProductName());
            System.out.println("Treiber: " + meta.getDriverName());
            System.out.println("URL: " + meta.getURL());
        } catch (SQLException e) {
            System.out.println("❌ Fehler Metadaten: " + e.getMessage());
        }
    }
}
