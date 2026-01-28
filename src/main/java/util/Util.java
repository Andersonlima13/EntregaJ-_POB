package util;

import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Util {
    private static EntityManager manager;
    private static EntityManagerFactory factory;
    private static final Logger logger = LogManager.getLogger(Util.class);

    public static EntityManager conectar() {
        if (manager == null) {
            try {
                logger.info("----conectar banco");

                Properties dados = new Properties();
                dados.load(Util.class.getResourceAsStream("/util/util.properties"));

                String sgbd = dados.getProperty("sgbd");
                String banco = dados.getProperty("banco");
                String ip = dados.getProperty("ipatual");
                String usuario = dados.getProperty("usuario");
                String senha = dados.getProperty("senha");

                Properties props = new Properties();

                if (sgbd.equals("postgresql")) {
                    props.setProperty("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");
                    props.setProperty("jakarta.persistence.jdbc.url",
                            "jdbc:postgresql://" + ip + ":5432/" + banco);
                    props.setProperty("jakarta.persistence.jdbc.user", usuario);
                    props.setProperty("jakarta.persistence.jdbc.password", senha);
                }

                if (sgbd.equals("mysql")) {
                    props.setProperty("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
                    props.setProperty("jakarta.persistence.jdbc.url",
                            "jdbc:mysql://" + ip + ":3306/" + banco + "?createDatabaseIfNotExist=true");
                    props.setProperty("jakarta.persistence.jdbc.user", usuario);
                    props.setProperty("jakarta.persistence.jdbc.password", senha);
                }

                String unit = "hibernate-" + sgbd;
                factory = Persistence.createEntityManagerFactory(unit, props);
                manager = factory.createEntityManager();

            } catch (Exception e) {
                throw new RuntimeException("Erro ao conectar no banco", e);
            }
        }
        return manager;
    }

    public static void desconectar() {
        if (manager != null && manager.isOpen()) {
            manager.close();
            factory.close();
            manager = null;
        }
    }

    public static EntityManager getManager() {
        return manager;
    }
}
