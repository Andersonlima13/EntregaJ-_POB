package util;

import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Util {

    private static EntityManagerFactory factory;
    private static final Logger logger = LogManager.getLogger(Util.class);

    private static void initFactory() {
        if (factory == null) {
            try {
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

                String unitName = "hibernate-" + sgbd;
                factory = Persistence.createEntityManagerFactory(unitName, props);

                logger.info("EntityManagerFactory inicializada: " + unitName);

            } catch (Exception e) {
                throw new RuntimeException("Erro ao inicializar JPA", e);
            }
        }
    }

    public static EntityManager getEntityManager() {
        initFactory();
        return factory.createEntityManager(); // 🔥 sempre NOVO
    }

    public static void closeFactory() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}
