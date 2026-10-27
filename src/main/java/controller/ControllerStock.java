package controller;

import exceptions.SaisieInvalideException;
import modele.Produit;
import modele.ProduitAlimentaire;
import modele.ProduitElectronique;
import modele.Stock;
import service.GestionStock;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ControllerStock {

    public static Scanner scan = new Scanner(System.in);
    private GestionStock gestionStock = new GestionStock();
    private int codeProvisoire = 0;


    public ControllerStock() throws SQLException, ClassNotFoundException {
    }

    // Affiche le menu principal
    public void afficherMenu() {
        System.out.println("===== GESTION DE LA VIDÉOTHÈQUE =====");
        System.out.println("1. Ajouter un produit");
        System.out.println("2. Lister le stock");
        System.out.println("3. Rechercher un produit");
        System.out.println("4. Altérer un produit");
        System.out.println("5. Supprimer un produit");
        System.out.println("6. Sauvegarder le stock");
        System.out.println("7. Charger le stock depuis BDD");
        System.out.println("0. Quitter");
        System.out.println("=====================================");
    }

    public void ajouterProduit() {
        try {
            int typeProduit = saisieInt("Type de produit (1 = Alimentaire, 2 = Electronique) : ");
            if(typeProduit < 1 || typeProduit > 2)
                throw new SaisieInvalideException("Saisie invalide !");


            String type = saisieString("Type de produit : ");
            String nom = saisieString("Nom : ");
            double prix = saisieDouble("Prix : ");
            int quantite = saisieInt("Quantite : ");

            Produit produit = null;
            if(typeProduit == 1) {
                LocalDate datePeremption = saisieDate("Date : (jj/mm/aaaa) ");
                produit = new ProduitAlimentaire(codeProvisoire, type, nom, prix, quantite, datePeremption);
            } else if(typeProduit == 2) {
                int dureeGarantie = saisieInt("Durée Garantie : (jours)");
                produit = new ProduitElectronique(codeProvisoire,  type, nom, prix, quantite, dureeGarantie);
            }

            if(produit != null) {
                codeProvisoire++;
                gestionStock.ajouterProduit(produit);
                System.out.println("Produit ajouté !");

            }

        } catch (SaisieInvalideException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

    }

    public void afficherProduit() {
        int code = saisieInt("Entrez le code du produit à afficher : ");
        Produit produit = gestionStock.obtenirProduit(code);
        if (produit != null) {
            System.out.println(produit);
        } else {
            System.out.println("\u001B[31mAucun produit trouvé avec le code " + code + "\u001B[0m");
        }
    }

    public void altererProduit() {
        try {
            int code = saisieInt("Entrez le code du produit à modifier : ");
            Produit produitExistant = gestionStock.obtenirProduit(code);

            if (produitExistant == null) {
                System.out.println("\u001B[31mProduit introuvable.\u001B[0m");
                return;
            }

            System.out.println("Modification du produit : ");
            System.out.println(produitExistant);
            String type = saisieString("Nouveau type de produit : ");
            String nom = saisieString("Nouveau nom : ");
            double prix = saisieDouble("Nouveau prix : ");
            int quantite = saisieInt("Nouvelle quantite : ");
            Produit nouveauProduit = null;

            if (produitExistant instanceof ProduitAlimentaire) {
                LocalDate datePeremption = saisieDate("Nouvelle date de péremption (jj/mm/aaaa) : ");
                nouveauProduit = new ProduitAlimentaire(code, type, nom, prix, quantite, datePeremption);
            } else if (produitExistant instanceof ProduitElectronique) {
                int dureeGarantie = saisieInt("Nouvelle durée de garantie : ");
                nouveauProduit = new ProduitElectronique(code, type, nom, prix, quantite, dureeGarantie);
            }

            if (nouveauProduit != null) {
                gestionStock.modifierProduit(nouveauProduit);
                System.out.println("Produit mis à jour avec succès !");
            }
        } catch (SaisieInvalideException e) {
            System.err.println(e.getMessage());
        }
    }

    public void supprimerProduit() {
        int code = saisieInt("Entrez le code du produit à supprimer : ");
        try {

            Produit produitExistant = gestionStock.obtenirProduit(code);

            if (produitExistant == null) {
                System.out.println("\u001B[31mProduit introuvable.\u001B[0m");
                return;
            }
            gestionStock.supprimerProduit(code);
            System.out.println("\u001B[32mProduit supprimé avec succès !\u001B[0m");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public void afficherStock() {
        Stock stockActuel = gestionStock.obtenirStockActuel();

        if (stockActuel == null || stockActuel.getlProduit().isEmpty()) {
            System.out.println("Le stock est actuellement vide en mémoire.");
        } else {
            System.out.println("--- LISTE DES PRODUITS EN STOCK (MÉMOIRE) ---");
            for (Produit p : stockActuel.getlProduit()) {
                System.out.println(p);
            }
        }
    }

    public void sauvegarderStock() {
        try {
            gestionStock.sauvegarderStockEnBase();
            System.out.println("\u001B[32mStock local sauvegardé avec succès en base de données !\u001B[0m");
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }
    }

    public void chargerStock() {
        Stock stockCharge = gestionStock.chargerToutLeStock();
        if(stockCharge.getlProduit().isEmpty()) {
            System.out.println("Impossible de charger le stock depuis la base de données");
        } else {
            System.out.println("\u001B[32mStock rechargé depuis la base de données ("
                    + stockCharge.getlProduit().size() + " produits synchronisés).\u001B[0m");
            this.codeProvisoire = 0;
            for(Produit p : stockCharge.getlProduit()) {
                if(p.getCode() > this.codeProvisoire){
                    this.codeProvisoire = p.getCode()+1;
                    System.out.println("code provisoire : " + this.codeProvisoire
                    );
                }
            }
        }
    }

    public String saisieString(String msg) throws SaisieInvalideException {
        System.out.println(msg);
        String nom = scan.nextLine();
        if (nom.isEmpty()) {
            throw new SaisieInvalideException("Saisie invalide");
        }
        return nom;
    }

    public LocalDate saisieDate(String msg) throws SaisieInvalideException {
        while(true) {
            System.out.print(msg);
            String saisie = scan.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            try {
                LocalDate date = LocalDate.parse(saisie, formatter);
                if (date.isAfter(LocalDate.of(2100, 12, 31)) || date.isBefore(LocalDate.of(1886, 01, 01))) {
                    throw new SaisieInvalideException("Veuillez saisir une année de sortie valide (1886-aujourd'hui)");
                }
                return date;
            } catch (SaisieInvalideException | DateTimeParseException e) {
                System.out.println("\u001B[31mDate non valide --> jj/mm/aaaa (1886-aujourd'hui)\u001B[0m");
            }
        }
    }

    public static int saisieInt(String msg) {
        while (true) {
            System.out.print(msg);
            String saisie = scan.nextLine();
            try {
                return Integer.parseInt(saisie);
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31mVeuillez entrer un nombre entier valide\u001B[0m");
            }
        }
    }

    public static double saisieDouble(String msg) {
        while (true) {
            System.out.print(msg);
            String saisie = scan.nextLine();
            try {
                return Double.parseDouble(saisie);
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31mVeuillez entrer un nombre entier valide\u001B[0m");
            }
        }
    }
}
