package appconsole;

import requisito.Fachada;

public class Apagar {

    public Apagar(int idEntrega) {
        try {
            System.out.println("=== APAGANDO OBJETO (via Fachada) ===\n");
            Fachada.apagarEntrega(idEntrega);
            System.out.println("Entrega #" + idEntrega + " apagada com sucesso (via Fachada)");
        } catch (Exception e) {
            System.err.println("Erro ao apagar via Fachada: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Apagar(1);
    }
}