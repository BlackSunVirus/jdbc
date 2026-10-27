package modele;

public class ProduitElectronique extends Produit {
    private int dureeGarantie;

    public ProduitElectronique(int code, String type, String nom, double prix, int quantite, int dureeGarantie) {
        super(code, "pe", nom, prix, quantite);
        this.dureeGarantie = dureeGarantie;
    }

    public int getDureeGarantie() {
        return dureeGarantie;
    }

    public void setDureeGarantie(int dureeGarantie) {
        this.dureeGarantie = dureeGarantie;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Produit électronique ").append(super.toString())
                .append(" durée de garantie : ").append(dureeGarantie).append("jours");
        return sb.toString();
    }
}
