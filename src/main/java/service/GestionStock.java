package service;

import DAO.DAOFactory;
import DAO.ProduitDAO;
import DAO.ProduitDAOImpl;
import modele.Produit;
import modele.ProduitAlimentaire;
import modele.ProduitElectronique;
import modele.Stock;
import java.sql.SQLException;
import java.util.ArrayList;

public class GestionStock {
    // On utilise l'INTERFACE ProduitDAO pour plus de souplesse
    private ProduitDAO produitDAO;
    private Stock stock;

    public GestionStock() throws SQLException, ClassNotFoundException {
        this.produitDAO = new ProduitDAOImpl(DAOFactory.getInstance());
        this.stock = new Stock(new ArrayList<>());

    }

    public void ajouterProduit(Produit p) {
//        if (produitDAO.read(p.getCode()) != null) {
//            throw new IllegalArgumentException("Erreur métier : Le produit existe déjà.");
//        }
        produitDAO.create(p);
        stock.getlProduit().add(p); // Synchro mémoire
    }

    public Produit obtenirProduit(int code) {
        return produitDAO.read(code);
    }

    public void supprimerProduit(int code) {
        Produit p = produitDAO.read(code);
        if (p != null) {
            produitDAO.delete(p);
            stock.getlProduit().removeIf(prod -> prod.getCode() == code);
        }
    }

    public Stock chargerToutLeStock() {
        this.stock = produitDAO.charger();
        return this.stock;
    }

    public void modifierProduit(Produit p) {
        produitDAO.update(p);
        Produit tempProduit = null;
        for(Produit pr : stock.getlProduit()) {
            if(p.getCode() == pr.getCode()) {
                int index = stock.getlProduit().indexOf(pr);
                stock.getlProduit().set(index, p);

                break;
            }
        }
//        tempProduit.setNom(p.getNom());
//        tempProduit.setPrix(p.getPrix());
//        tempProduit.setQuantite(p.getQuantite());
//        if(tempProduit instanceof ProduitAlimentaire) {
//            ((ProduitAlimentaire) tempProduit).setDatePeremption(((ProduitAlimentaire) p).getDatePeremption());
//        } else if (tempProduit instanceof ProduitElectronique) {
//            ((ProduitElectronique) tempProduit).setDureeGarantie(((ProduitElectronique) p).getDureeGarantie());
//
//        }


//        stock.getlProduit().removeIf(prod -> prod.getCode() == p.getCode());
//        stock.getlProduit().add(p);
    }

    public Stock obtenirStockActuel() {
        return this.stock;
    }

    public void sauvegarderStockEnBase() {
        produitDAO.sauvegarder(this.stock);
    }

}
