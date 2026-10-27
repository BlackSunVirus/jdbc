package modele;

import java.io.Serializable;
import java.time.LocalDate;

public class ProduitAlimentaire extends Produit implements Perissable {
    private LocalDate datePeremption;

    public ProduitAlimentaire(int code, String type, String nom, double prix, int quantite, LocalDate datePeremption) {
        super(code, "pa", nom, prix, quantite);
        this.datePeremption = datePeremption;
    }

    @Override
    public boolean estPerime() {
        if(datePeremption.isBefore(LocalDate.now())) return true;
        return false;
    }

    public LocalDate getDatePeremption() {
        return datePeremption;
    }

    public void setDatePeremption(LocalDate datePeremption) {
        this.datePeremption = datePeremption;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Produit Alimentaire").append(super.toString()).append(" date de péremption : ").append(datePeremption);
        if(estPerime())
            sb.append(" ⚠\uFE0Fexpiré⚠");
        else {
            sb.append(" (valide)");
        }
        return sb.toString();
    }
}
