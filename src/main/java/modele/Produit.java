package modele;

public abstract class Produit {

    private int code;
    private String type;
    private String nom;
    private Double prix;
    private int quantite;


    public Produit(int code, String type, String nom, double prix, int quantite) {
        this.code = code;
        this.type = type;
        this.nom = nom;
        this.prix = prix;
        this.quantite = quantite;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" n°").append(code).append(" ").append(nom)
                .append(" (").append(type).append(") ")
                .append(String.format("%.2f", prix))
                .append("€ x").append(quantite);
        return sb.toString();
    }
}
