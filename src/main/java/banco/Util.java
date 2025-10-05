package banco;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import model.Pedido;


public class Util {
    private static ObjectContainer manager;

    public static ObjectContainer conectarBanco(){
        if (manager != null)
            return manager;


        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        config.common().messageLevel(0);
        config.common().objectClass(Pedido.class).cascadeOnActivate(true);
        config.common().objectClass(Pedido.class).cascadeOnUpdate(true);
        config.common().objectClass(Pedido.class).cascadeOnDelete(true);

        manager = Db4oEmbedded.openFile(config,  "EntregaJa");
        return manager;
    }

    public static void desconectar(){
        if(manager!=null){
            manager.close();
            manager = null;
        }
    }








}