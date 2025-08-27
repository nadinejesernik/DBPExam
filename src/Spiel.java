// Repräsentiert ein Spiel im Shop
public class Spiel {
    private int id;
    private String titel;
    private String genre;
    private double preis;

    // Konstruktor für neue Spiele (ID wird von DB vergeben)
    public Spiel(String titel, String genre, double preis) {
        this.id = -1;
        this.titel = titel;
        this.genre = genre;
        this.preis = preis;
    }

    // Konstruktor beim Laden aus DB
    public Spiel(int id, String titel, String genre, double preis) {
        this.id = id;
        this.titel = titel;
        this.genre = genre;
        this.preis = preis;
    }

    // Getter / Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitel() { return titel; }
    public void setTitel(String titel) { this.titel = titel; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public double getPreis() { return preis; }
    public void setPreis(double preis) { this.preis = preis; }

    @Override
    public String toString() {
        return String.format("%-20s | Genre: %-10s | Preis: %.2f € (ID: %d)",
                titel, genre, preis, id);
    }

}
