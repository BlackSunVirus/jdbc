import DAO.DAOFactory;
import DAO.ProduitDAO;
import DAO.ProduitDAOImpl;
import controller.ControllerStock;
import exceptions.SaisieInvalideException;
import modele.Produit;
import modele.ProduitAlimentaire;
import modele.Stock;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ControllerStock c = new ControllerStock();

        try {
            c.chargerStock();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int choix = -1;
        while (true) {
            try {
                c.afficherStock();
                c.afficherMenu();
                System.out.print("Choix : ");
                choix = ControllerStock.scan.nextInt();
                ControllerStock.scan.nextLine();
                switch (choix) {
                    case 1:
                        c.ajouterProduit();
                        break;
                    case 2:
                        c.afficherStock();
                        break;
                    case 3:
                        c.afficherProduit();
                        break;
                    case 4:
                        c.altererProduit();
                        break;
                    case 5:
                        c.supprimerProduit();
                        break;
                    case 6:
                        c.sauvegarderStock();
                        break;
                    case 7:
                        c.chargerStock();
                        break;
                    case 0:
                        System.out.println("Au revoir !");
                        ControllerStock.scan.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("\u001B[31mChoix invalide, veuillez réessayer.\u001B[0m");
                }
            } catch (SaisieInvalideException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException ime) {
                ControllerStock.scan.nextLine();
                System.out.println("\u001B[31mSaisie non valide\u001B[0m");
            }
        }
    }

    private Stock testStock() throws ClassNotFoundException, SQLException {
        DAOFactory factory = DAOFactory.getInstance();
        ProduitDAO produitDAO = new ProduitDAOImpl(factory);

        Stock stock = produitDAO.charger();
        if(stock.getlProduit().isEmpty()) {
            System.out.println("Aucun stock trouvé !");
            System.out.println("Ajout de nouveau produits...");
            List<Produit> lProduits = new ArrayList<>();
            List<String> fruits = Arrays.asList("Pomme", "Banane", "Orange", "Fraise", "Mangue");
            int randomFruitIndex;
            String randomFruit;
            int randomInt;
            float randomFloat;
            for(int i = 0; i<6; i++) {
                randomInt = ThreadLocalRandom.current().nextInt(1, 10);
                randomFloat = ThreadLocalRandom.current().nextFloat(1, 10);
                randomFruitIndex = ThreadLocalRandom.current().nextInt(0, fruits.size());
                randomFruit =  fruits.get(randomFruitIndex);
                lProduits.add(new ProduitAlimentaire(i, "fruit", randomFruit,
                        randomFloat, randomInt, LocalDate.now()));
            }
            stock = new Stock(lProduits);
        } else {
            System.out.println("Stock trouvé ! ");
        }

        System.out.println("Voici le stock : ");
        System.out.println(stock.toString());

        System.out.println("Sauvegarde du stock...");
        produitDAO.sauvegarder(stock);

        return stock;
    }
}
