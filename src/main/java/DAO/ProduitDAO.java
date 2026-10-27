package DAO;

import modele.Produit;
import modele.Stock;

import java.util.List;

public interface ProduitDAO {

    void create(Produit p);
    Produit read(int id);
    void update(Produit p);
    void delete(Produit p);
    List<Produit> readAll();
    void sauvegarder(Stock stock);
    Stock charger();
}
