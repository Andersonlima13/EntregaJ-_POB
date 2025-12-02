package appswing;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
        table.setRequestFocusEnabled(false);
        table.setFocusable(false);
        table.setBackground(Color.LIGHT_GRAY);
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(true);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        scrollPane.setViewportView(table);
        table.setBorder(new LineBorder(new Color(0, 0, 0)));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

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
                if (index < 0) {
                    label_4.setText("Consulta não selecionada");
                } else {
                    label_4.setText("");
                    label.setText("");
                    try {
                        switch (index) {
                            case 0: // Pedidos sem entrega
                                consultarPedidosSemEntrega();
                                break;
                            case 1: // Pedidos por entregador
                                String nome = JOptionPane.showInputDialog("Nome do entregador:");
                                if (nome != null && !nome.isEmpty())
                                    consultarPedidosPorEntregador(nome);
                                break;
                            case 2: // Entregadores com mais de N entregas
                                String n = JOptionPane.showInputDialog("Digite N:");
                                if (n != null && !n.isEmpty())
                                    consultarEntregadoresComMaisDeN(Integer.parseInt(n));
                                break;
                        }
                    } catch (Exception ex) {
                        label.setText("Erro: " + ex.getMessage());
                    }
                }
            }
        });
        button.setBounds(720, 10, 100, 23);
        frame.getContentPane().add(button);

        comboBox = new JComboBox<String>();
        comboBox.setToolTipText("Selecione a consulta");
        comboBox.setModel(new DefaultComboBoxModel(new String[] {
            "1. Pedidos sem entrega",
            "2. Pedidos entregues por entregador X",
            "3. Entregadores com mais de N entregas"
        }));
        comboBox.setSelectedIndex(0);
        comboBox.setBounds(21, 10, 680, 22);
        frame.getContentPane().add(comboBox);
    }

    // CONSULTA 1: Pedidos sem entrega via Fachada
    private void consultarPedidosSemEntrega() {
        try {
            List<Pedido> lista = Fachada.listarPedidos();

            DefaultTableModel model = new DefaultTableModel();
            table.setModel(model);

            model.addColumn("ID");
            model.addColumn("Data");
            model.addColumn("Valor");
            model.addColumn("Descrição");
            model.addColumn("Status");

            int count = 0;
            for (Pedido p : lista) {
                if (p.getEntrega() == null) {
                    count++;
                    model.addRow(new Object[] {
                        p.getId(),
                        p.getData(),
                        p.getValor(),
                        p.getDescricao(),
                        "SEM ENTREGA"
                    });
                }
            }

            label_4.setText("Resultados: " + count + " pedido(s) sem entrega");

        } catch (Exception erro) {
            label.setText(erro.getMessage());
        }
    }

    // CONSULTA 2: Pedidos por entregador via Fachada
    private void consultarPedidosPorEntregador(String nomeEntregador) {
        try {
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

            int totalPedidos = 0;
            List<Entrega> entregas = ent.getListaDeEntrega();
            if (entregas == null) entregas = java.util.Collections.emptyList();

            for (Entrega e : entregas) {
                for (Pedido p : e.getPedidos()) {
                    totalPedidos++;
                    model.addRow(new Object[] {
                        p.getId(),
                        p.getData(),
                        p.getValor(),
                        p.getDescricao(),
                        e.getId(),
                        e.getData()
                    });
                }
            }

            label_4.setText("Resultados: " + totalPedidos + " pedido(s) entregue(s) por " + nomeEntregador);

        } catch (Exception erro) {
            label.setText(erro.getMessage());
        }
    }

    // CONSULTA 3: Entregadores com mais de N entregas via Fachada
    private void consultarEntregadoresComMaisDeN(int n) {
        try {
            List<Entregador> entregadores = Fachada.listarEntregadores();

            DefaultTableModel model = new DefaultTableModel();
            table.setModel(model);

            model.addColumn("ID");
            model.addColumn("Nome");
            model.addColumn("Total Entregas");
            model.addColumn("Detalhes");

            int count = 0;
            for (Entregador ent : entregadores) {
                int qtd = ent.getListaDeEntrega() != null ? ent.getListaDeEntrega().size() : 0;
                if (qtd > n) {
                    count++;
                    StringBuilder detalhes = new StringBuilder();
                    if (ent.getListaDeEntrega() != null) {
                        for (Entrega e : ent.getListaDeEntrega()) {
                            detalhes.append("Entrega #").append(e.getId())
                                   .append(" (").append(e.getData()).append(") ");
                        }
                    }

                    model.addRow(new Object[] {
                        ent.getId(),
                        ent.getNome(),
                        qtd,
                        detalhes.toString()
                    });
                }
            }

            label_4.setText("Resultados: " + count + " entregador(es) com mais de " + n + " entrega(s)");

        } catch (Exception erro) {
            label.setText(erro.getMessage());
        }
    }
}