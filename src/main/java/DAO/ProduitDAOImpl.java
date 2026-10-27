package DAO;

import modele.Produit;
import modele.ProduitAlimentaire;
import modele.ProduitElectronique;
import modele.Stock;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProduitDAOImpl extends DAO implements ProduitDAO {
    public enum ProduitTable {
        // nom table
        TABLE("produit"),

        // attributs
        CODE("code"),
        TYPE("type"),
        NOM("nom"),
        PRIX("prix"),
        QUANTITE("quantite"),
        DATE_PEREMPTION("date_peremption"),
        DUREE_GARANTIE("duree_garantie");

        private final String label;

        ProduitTable(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public ProduitDAOImpl(DAOFactory daoFactory) throws SQLException {
        super(daoFactory);
    }

    @Override
    public void create(Produit p) {
        String query = "insert into " + ProduitTable.TABLE.getLabel() +
                " (" + ProduitTable.TYPE.getLabel() + ", "
                + ProduitTable.NOM.getLabel() + ", "
                + ProduitTable.PRIX.getLabel() + ", "
                + ProduitTable.QUANTITE.getLabel() + ", "
                + ProduitTable.DATE_PEREMPTION.getLabel() + ", "
                + ProduitTable.DUREE_GARANTIE.getLabel() +
                ") values (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps =  DAOFactory.getInstance().getConnection().prepareStatement(query)) {

            ps.setString(1, p.getType());
            ps.setString(2, p.getNom());
            ps.setDouble(3, p.getPrix());
            ps.setInt(4, p.getQuantite());
            if(p instanceof ProduitAlimentaire) {
                ProduitAlimentaire pa = (ProduitAlimentaire) p;
                ps.setDate(5, Date.valueOf(pa.getDatePeremption()));
                ps.setInt(6, 0);
            } else if(p instanceof ProduitElectronique) {
                ProduitElectronique pe = (ProduitElectronique) p;
                ps.setDate(5, null);
                ps.setInt(6, pe.getDureeGarantie());
            }
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Produit read(int id) {
        Produit produit =  null;
        String  query = "select * from " + ProduitTable.TABLE.getLabel() + " where code = ?";
        try (PreparedStatement ps =  DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            ps.setInt(1, id);

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    int code = resultSet.getInt(ProduitTable.CODE.getLabel());
                    String type = resultSet.getString(ProduitTable.TYPE.getLabel());
                    String nom = resultSet.getString(ProduitTable.NOM.getLabel());
                    float prix = resultSet.getFloat(ProduitTable.PRIX.getLabel());
                    int quantite = resultSet.getInt(ProduitTable.QUANTITE.getLabel());
                    Date datePeremptionSql = resultSet.getDate(ProduitTable.DATE_PEREMPTION.getLabel());

                    LocalDate datePeremption = (datePeremptionSql != null) ? datePeremptionSql.toLocalDate() : null;
                    int dureeGarantie = resultSet.getInt(ProduitTable.DUREE_GARANTIE.getLabel());
                    if(datePeremption == null) {
                        produit = new ProduitElectronique(code, type, nom, prix, quantite, dureeGarantie);
                    } else if(dureeGarantie == 0) {
                        produit = new ProduitAlimentaire(code, type, nom, prix, quantite, datePeremption);
                    } else {
                        throw new SQLException("La durée garantie et date de péremption n'existent pas ");
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return produit;
    }

    @Override
    public void update(Produit p) {
        String query = "update " + ProduitTable.TABLE.getLabel() +
                " SET " + ProduitTable.TYPE.getLabel() + " = ?, "
                + ProduitTable.NOM.getLabel() + " = ?, "
                + ProduitTable.PRIX.getLabel() + " = ?, "
                + ProduitTable.QUANTITE.getLabel() + " = ?, "
                + ProduitTable.DATE_PEREMPTION.getLabel() + " = ?, "
                + ProduitTable.DUREE_GARANTIE.getLabel() + " = ? " +
                "where code = ?";
        try (PreparedStatement ps =  DAOFactory.getInstance().getConnection().prepareStatement(query)) {

            ps.setString(1, p.getType());
            ps.setString(2, p.getNom());
            ps.setDouble(3, p.getPrix());
            ps.setInt(4, p.getQuantite());
            if(p instanceof ProduitAlimentaire) {
                ProduitAlimentaire pa = (ProduitAlimentaire) p;
                ps.setDate(5, Date.valueOf(pa.getDatePeremption()));
                ps.setInt(6, 0);
            } else if(p instanceof ProduitElectronique) {
                ProduitElectronique pe = (ProduitElectronique) p;
                ps.setDate(5, null);
                ps.setInt(6, pe.getDureeGarantie());
            }
            ps.setInt(7, p.getCode());
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Produit p) {
        String  query = "delete from " + ProduitTable.TABLE.getLabel() + " where code = ?";
        try (PreparedStatement ps =  DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            ps.setInt(1, p.getCode());

            int lignesSupprimees = ps.executeUpdate();

            if(lignesSupprimees == 0) {
                throw new SQLException("Le produits n'existe pas");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Produit> readAll() {
        List<Produit> lProduits = new ArrayList<>();
        String  query = "select * from " + ProduitTable.TABLE.getLabel();
        try (PreparedStatement ps =  DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    int code = resultSet.getInt(ProduitTable.CODE.getLabel());
                    String type = resultSet.getString(ProduitTable.TYPE.getLabel());
                    String nom = resultSet.getString(ProduitTable.NOM.getLabel());
                    float prix = resultSet.getFloat(ProduitTable.PRIX.getLabel());
                    int quantite = resultSet.getInt(ProduitTable.QUANTITE.getLabel());
                    Date datePeremptionSql = resultSet.getDate(ProduitTable.DATE_PEREMPTION.getLabel());

                    LocalDate datePeremption = (datePeremptionSql != null) ? datePeremptionSql.toLocalDate() : null;
                    int dureeGarantie = resultSet.getInt(ProduitTable.DUREE_GARANTIE.getLabel());
                    if(datePeremption == null) {
                        lProduits.add(new ProduitElectronique(code, type, nom, prix, quantite, dureeGarantie));
                    } else if(dureeGarantie == 0) {
                        lProduits.add(new ProduitAlimentaire(code, type, nom, prix, quantite, datePeremption));
                    } else {
                        throw new SQLException("La durée garantie et date de péremption n'existent pas ");
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return lProduits;
    }

    @Override
    public void sauvegarder(Stock stock) {
        for (Produit p : stock.getlProduit()) {
            create(p);
        }
    }

    @Override
    public Stock charger() {
        Stock stock = new Stock(readAll());
        return stock;
    }
}
