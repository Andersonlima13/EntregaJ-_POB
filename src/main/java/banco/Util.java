package banco;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.events.EventRegistry;
import com.db4o.events.EventRegistryFactory;
import com.db4o.query.Query;
import model.Pedido;
import model.Entrega;
import model.Entregador;

import java.lang.reflect.Field;
import java.util.List;
import java.util.TreeMap;

public class Util {
    private static ObjectContainer manager;
    private static ObjectContainer sequencia; // banco de sequencia de IDs
    private static TreeMap<String, RegistroID> registros = new TreeMap<>();
    private static boolean salvar = false;

    // Conectar ao banco principal
    public static ObjectContainer conectarBanco() {
        if (manager != null)
            return manager;

        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        config.common().messageLevel(0);

        // Configura classes e cascata
        config.common().objectClass(Pedido.class).cascadeOnActivate(true).cascadeOnUpdate(true).cascadeOnDelete(true);
        config.common().objectClass(Entrega.class).cascadeOnActivate(true).cascadeOnUpdate(true).cascadeOnDelete(true);
        config.common().objectClass(Entregador.class).cascadeOnActivate(true).cascadeOnUpdate(true).cascadeOnDelete(true);

        manager = Db4oEmbedded.openFile(config, "EntregaJa");

        // Ativar controle de IDs
        ativarControleID(manager);

        return manager;
    }

    // Desconectar banco
    public static void desconectar() {
        if (manager != null) {
            manager.close();
            manager = null;
        }
        if (sequencia != null && !sequencia.ext().isClosed()) {
            sequencia.close();
            sequencia = null;
        }
    }

    // --------------------- Controle de IDs ---------------------
    private static void ativarControleID(ObjectContainer manager) {
        // Banco auxiliar para sequencias
        sequencia = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "sequencia.db4o");
        lerRegistrosID();

        EventRegistry eventRegistry = EventRegistryFactory.forObjectContainer(manager);

        // Trigger antes de persistir objeto
        eventRegistry.creating().addListener((event, args) -> {
            try {
                Object objeto = args.object();
                Field field = objeto.getClass().getDeclaredField("id");
                if (field != null && field.getType() == int.class) {
                    String nomeClasse = objeto.getClass().getName();
                    RegistroID registro = registros.getOrDefault(nomeClasse, new RegistroID(nomeClasse));
                    registro.incrementarID();
                    field.setAccessible(true);
                    field.setInt(objeto, registro.getId());
                    registros.put(nomeClasse, registro);
                    salvar = true;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Se não houver campo id, ignora
            }
        });

        // Trigger após commit: salva sequencias
        eventRegistry.created().addListener((event, args) -> {
            if (salvar) salvarRegistrosID();
        });

        // Trigger antes de fechar
        eventRegistry.closing().addListener((event, args) -> {
            if (sequencia != null && !sequencia.ext().isClosed()) {
                sequencia.close();
            }
        });
    }

    private static void lerRegistrosID() {
        Query q = sequencia.query();
        q.constrain(RegistroID.class);
        List<RegistroID> resultados = q.execute();
        for (RegistroID reg : resultados) {
            registros.put(reg.getNomeClasse(), reg);
        }
        salvar = false;
    }

    private static void salvarRegistrosID() {
        if (!salvar) return;
        for (RegistroID reg : registros.values()) {
            if (reg.isModificado()) {
                sequencia.store(reg);
                sequencia.commit();
                reg.setModificado(false);
            }
        }
        salvar = false;
    }

    // --------------------- RegistroID ---------------------
    private static class RegistroID {
        private String nomeClasse;
        private int ultimoId;
        transient private boolean modificado = false;

        public RegistroID(String nomeClasse) {
            this.nomeClasse = nomeClasse;
            this.ultimoId = 0;
        }

        public String getNomeClasse() {
            return nomeClasse;
        }

        public int getId() {
            return ultimoId;
        }

        public boolean isModificado() {
            return modificado;
        }

        public void setModificado(boolean modificado) {
            this.modificado = modificado;
        }

        public void incrementarID() {
            ultimoId++;
            setModificado(true);
        }
    }
}
