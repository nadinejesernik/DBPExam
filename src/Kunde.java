// Repräsentiert einen Kunden im Videospielgeschäft
public class Kunde {
    private int id;
    private String name;
    private String email;
    private int bonuspunkte;

    // Konstruktor für neuen Kunden ohne ID (DB vergibt ID automatisch)
    public Kunde(String name, String email) {
        this.id = -1; // Platzhalter, bis die DB eine echte ID vergibt
        this.name = name;
        this.email = email;
        this.bonuspunkte = 0;
    }

    // Konstruktor mit ID (z. B. beim Laden aus der DB)
    public Kunde(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Konstruktor mit allen Feldern
    public Kunde(int id, String name, String email, int bonuspunkte) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.bonuspunkte = bonuspunkte;
    }

    // Getter und Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getBonuspunkte() { return bonuspunkte; }
    public void setBonuspunkte(int bonuspunkte) { this.bonuspunkte = bonuspunkte; }

    @Override
    public String toString() {
        return "Kunde #" + id + " | " + name + " (" + email + "), Bonuspunkte: " + bonuspunkte;
    }

}

