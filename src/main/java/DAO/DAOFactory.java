package DAO;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DAOFactory {
    private String url;
    private String login;
    private String password;
    private static DAOFactory instance;

    public DAOFactory(String url, String login, String password) {
        this.url = url;
        this.login = login;
        this.password = password;
    }

    public static DAOFactory getInstance() throws ClassNotFoundException {
        if (instance == null) {
            final String CONSTANTES_PROPERTIES = "constantes.properties";
            Properties props = new Properties();
            //chargement du fichier
            try (InputStream in = DAOFactory.class.getClassLoader()
                    .getResourceAsStream(CONSTANTES_PROPERTIES)) {
                //fichier non présent, on leve une exception
                if (in == null) {
                    throw new IOException();
                }
                //chargement des proprriétes
                props.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //récuperation des parametres
            String url = props.getProperty("url");
            String driver = props.getProperty("driver");
            String user = props.getProperty("user");
            String password = props.getProperty("password", "");
            Class.forName(driver);
            instance=new DAOFactory(url,user,password);
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, login, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

}
