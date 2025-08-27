import java.time.LocalDate;

// Repräsentiert den Kauf eines Spiels durch einen Kunden
public class Kauf {
    private int id;
    private Kunde kunde;
    private Spiel spiel;
    private LocalDate kaufdatum;

    public Kauf(Kunde kunde, Spiel spiel, LocalDate kaufdatum) {
        this.id = -1;
        this.kunde = kunde;
        this.spiel = spiel;
        this.kaufdatum = kaufdatum;
    }

    public Kauf(int id, Kunde kunde, Spiel spiel, LocalDate kaufdatum) {
        this.id = id;
        this.kunde = kunde;
        this.spiel = spiel;
        this.kaufdatum = kaufdatum;
    }

    // Getter / Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Kunde getKunde() { return kunde; }
    public void setKunde(Kunde kunde) { this.kunde = kunde; }

    public Spiel getSpiel() { return spiel; }
    public void setSpiel(Spiel spiel) { this.spiel = spiel; }

    public LocalDate getKaufdatum() { return kaufdatum; }
    public void setKaufdatum(LocalDate kaufdatum) { this.kaufdatum = kaufdatum; }

    @Override
    public String toString() {
        return "Kauf{" +
                "id=" + id +
                ", kunde=" + kunde.getName() +
                ", spiel=" + spiel.getTitel() +
                ", kaufdatum=" + kaufdatum +
                '}';
    }
}
