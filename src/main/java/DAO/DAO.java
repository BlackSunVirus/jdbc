package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DAO {
    protected Connection connection;

    public DAO(DAOFactory daoFactory) throws SQLException {
        this.connection = daoFactory.getConnection();
    }
}
