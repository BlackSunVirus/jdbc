package modele;

import java.util.ArrayList;
import java.util.List;

public class Stock {
    private List<Produit> lProduit;

    public Stock(List<Produit> lProduit) {
        this.lProduit = lProduit;
    }

    public List<Produit> getlProduit() {
        return lProduit;
    }

    public void setlProduit(List<Produit> lProduit) {
        this.lProduit = lProduit;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "lProduit=" + lProduit +
                '}';
    }
}
