# Prüfungsprojekt SS 2025 – Datenbankprogrammierung

Dieses Projekt implementiert die Prüfungsaufgaben am Beispiel eines **Videospielgeschäfts**.  
Datenbank: SQLite (`DBPExam.db`)  
Programmiersprache: Java

## 1. Verbindung zur Datenbank
- `DBHelper.connect()` / `DBHelper.disconnect()`
- Stellt die Verbindung zur SQLite-DB her und schließt sie wieder sauber.

## 2. Tabellen erstellen
- Beispiel: `DBHelper.createTableKunden()`
- Erzeugt die Tabelle **Kunden** (ID, Name, Email, Bonuspunkte).
- Prüft, ob die Tabelle bereits existiert → gibt Warnung aus.

## 3. CRUD-Operationen
- **Insert:** `DBHelper.insertNeuesSpiel(Spiel spiel)`  
- **Update:** `DBHelper.updateSpielPreis(int id, double preis)`  
- **Delete:** `DBHelper.deleteSpiel(int id)`  

## 4. ORM (Object Relational Mapping)
- `DBHelper.getSpieleByGenre(String genre)` → gibt `ArrayList<Spiel>` zurück.  
- `DBHelper.insertSpiele(ArrayList<Spiel> spiele)` → fügt mehrere Spiele ein (mit Sammelmeldung).

## 5. Aggregatfunktionen
- `DBHelper.getDurchschnittspreisAllerSpiele()` → nutzt `AVG()`.  
- `DBHelper.getKundeMitHoechstemUmsatz()` → nutzt `SUM()`, liefert Kunde + Umsatz.

## 6. Transaktionen
- `DBHelper.transferBonuspunkte(int vonId, int zuId, int punkte)`  
  - Abbuchung & Gutschrift in einer Transaktion.  
  - Rollback bei Fehler.  

**Beispiel in Main:**
db.transferBonuspunkte(2, 1, 30);

---

## 7. Metadaten
- `DBHelper.printMetaDaten()` → gibt DB-Name, Treiber, URL aus.

**Erklärung:**  
Metadaten sind *Daten über Daten*. Sie beschreiben die Struktur der DB:  
- Welche Tabellen & Spalten existieren?  
- Welche Datentypen?  
- Welcher Treiber wird genutzt?  

**Nutzen im Projekt:**  
- Anzeige von DB-Infos.  
- Wird in diesem Projekt auch verwendet, um zu prüfen, ob Tabellen existieren, bevor man sie erstellt.

## 8. Constraints
Regeln, die Datenqualität sichern.  

**Beispiele:**  
- `PRIMARY KEY AUTOINCREMENT` → eindeutige IDs.  
- `UNIQUE` → Email-Adressen der Kunden dürfen nicht doppelt sein.  
- `CHECK(Bonuspunkte >= 0)` → keine negativen Punkte.  
- `FOREIGN KEY` → sichert Beziehung: Kauf ↔ Kunde, Kauf ↔ Spiel.  

**Nutzen im Projekt:**  
- verhindert fehlerhafte Daten (z. B. doppelte Emails).  
- garantiert Konsistenz zwischen Tabellen.

## 9. Klassen
- **Kunde**: id, name, email, bonuspunkte.  
- **Spiel**: id, titel, genre, preis.  
- **Kauf**: id, Kunde, Spiel, kaufdatum.  
- **TopKundeUmsatz**: Kunde + Gesamtumsatz.  

Alle Klassen überschreiben `toString()` für kompakte Ausgabe.  

## 10. Main
Zeigt die Nutzung aller Methoden:  
- Tabelle erstellen  
- CRUD-Operationen  
- ORM mit ArrayList  
- Aggregatfunktionen  
- Transaktionen  
- Metadaten  
