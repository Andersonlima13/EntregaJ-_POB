package appswing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import model.Entrega;
import model.Entregador;
import model.Pedido;
import requisito.Fachada;

public class TelaConsulta {

    private JDialog frame;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton button;
    private JLabel label;
    private JLabel label_4;
    private JComboBox<String> comboBox;

    public TelaConsulta() {
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JDialog();
        frame.setModal(true);
        frame.setResizable(false);
        frame.setTitle("Consultas - Sistema de Entregas");
        frame.setBounds(100, 100, 850, 450);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(21, 43, 800, 250);
        frame.getContentPane().add(scrollPane);

        table = new JTable() {
            public boolean isCellEditable(int rowIndex, int vColIndex) {
                return false;
            }
        };

        table.setGridColor(Color.BLACK);
        table.setFocusable(false);
        table.setBackground(Color.LIGHT_GRAY);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        scrollPane.setViewportView(table);
        table.setBorder(new LineBorder(Color.BLACK));

        label = new JLabel("");
        label.setForeground(Color.BLUE);
        label.setBounds(21, 370, 800, 14);
        frame.getContentPane().add(label);

        label_4 = new JLabel("Resultados:");
        label_4.setBounds(21, 300, 800, 14);
        frame.getContentPane().add(label_4);

        button = new JButton("Consultar");
        button.setFont(new Font("Tahoma", Font.PLAIN, 12));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = comboBox.getSelectedIndex();
                label.setText("");
                try {
                    switch (index) {
                        case 0:
                            consultarPedidosSemEntrega();
                            break;
                        case 1:
                            String nome = JOptionPane.showInputDialog("Nome do entregador:");
                            if (nome != null && !nome.isBlank())
                                consultarPedidosPorEntregador(nome);
                            break;
                        case 2:
                            String n = JOptionPane.showInputDialog("Digite N:");
                            if (n != null && !n.isBlank())
                                consultarEntregadoresComMaisDeN(Integer.parseInt(n));
                            break;
                    }
                } catch (Exception ex) {
                    label.setText("Erro: " + ex.getMessage());
                }
            }
        });

        button.setBounds(720, 10, 100, 23);
        frame.getContentPane().add(button);

        comboBox = new JComboBox<>();
        comboBox.setModel(new DefaultComboBoxModel<>(new String[]{
                "1. Pedidos sem entrega",
                "2. Pedidos entregues por entregador X",
                "3. Entregadores com mais de N entregas"
        }));
        comboBox.setBounds(21, 10, 680, 22);
        frame.getContentPane().add(comboBox);
    }

    // CONSULTA 1
    private void consultarPedidosSemEntrega() {
        DefaultTableModel model = new DefaultTableModel();
        table.setModel(model);

        model.addColumn("ID");
        model.addColumn("Data");
        model.addColumn("Valor");
        model.addColumn("Descrição");
        model.addColumn("Status");

        int count = 0;
        for (Pedido p : Fachada.listarPedidos()) {
            if (p.getEntrega() == null) {
                count++;
                model.addRow(new Object[]{
                        p.getId(),
                        p.getData(),
                        p.getValor(),
                        p.getDescricao(),
                        "SEM ENTREGA"
                });
            }
        }
        label_4.setText("Resultados: " + count + " pedido(s) sem entrega");
    }

    // CONSULTA 2 (Set de Entregas)
    private void consultarPedidosPorEntregador(String nomeEntregador) {
        Entregador ent = Fachada.localizarEntregadorPorNome(nomeEntregador);
        if (ent == null) {
            label.setText("Entregador não encontrado: " + nomeEntregador);
            return;
        }

        DefaultTableModel model = new DefaultTableModel();
        table.setModel(model);

        model.addColumn("ID Pedido");
        model.addColumn("Data Pedido");
        model.addColumn("Valor");
        model.addColumn("Descrição");
        model.addColumn("ID Entrega");
        model.addColumn("Data Entrega");

        int total = 0;
        Set<Entrega> entregas = ent.getListaDeEntrega();

        for (Entrega e : entregas) {
            for (Pedido p : e.getPedidos()) {
                total++;
                model.addRow(new Object[]{
                        p.getId(),
                        p.getData(),
                        p.getValor(),
                        p.getDescricao(),
                        e.getId(),
                        e.getData()
                });
            }
        }

        label_4.setText("Resultados: " + total +
                " pedido(s) entregue(s) por " + nomeEntregador);
    }

    // CONSULTA 3 (Set)
    private void consultarEntregadoresComMaisDeN(int n) {
        DefaultTableModel model = new DefaultTableModel();
        table.setModel(model);

        model.addColumn("ID");
        model.addColumn("Nome");
        model.addColumn("Total Entregas");
        model.addColumn("Detalhes");

        int count = 0;
        for (Entregador ent : Fachada.listarEntregadores()) {
            int qtd = ent.getListaDeEntrega().size();
            if (qtd > n) {
                count++;
                StringBuilder detalhes = new StringBuilder();
                for (Entrega e : ent.getListaDeEntrega()) {
                    detalhes.append("Entrega #")
                            .append(e.getId())
                            .append(" (")
                            .append(e.getData())
                            .append(") ");
                }

                model.addRow(new Object[]{
                        ent.getId(),
                        ent.getNome(),
                        qtd,
                        detalhes.toString()
                });
            }
        }

        label_4.setText("Resultados: " + count +
                " entregador(es) com mais de " + n + " entrega(s)");
    }
}
