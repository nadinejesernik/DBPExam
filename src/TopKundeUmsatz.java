// Packt Kunde + Umsatz zusammen
public class TopKundeUmsatz {
    private final Kunde kunde;
    private final double umsatz;

    public TopKundeUmsatz(Kunde kunde, double umsatz) {
        this.kunde = kunde;
        this.umsatz = umsatz;
    }

    public Kunde getKunde() { return kunde; }
    public double getUmsatz() { return umsatz; }

    @Override
    public String toString() {
        return String.format("%s | Gesamtumsatz: %.2f €", kunde.toString(), umsatz);
    }
}

