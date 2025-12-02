package appswing;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import model.Entrega;
import model.Entregador;
import model.Pedido;
import requisito.Fachada;

public class TelaEntrega {
    private JDialog frame;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton btnCriar;
    private JButton btnApagar;
    private JButton btnLimpar;
    private JTextField textFieldId;
    private JTextField textFieldData;
    private JTextField textFieldLatitude;
    private JTextField textFieldLongitude;
    private JTextField textFieldEntregador;
    private JLabel label;
    private JLabel labelInfo;

    public TelaEntrega() {
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JDialog();
        frame.getContentPane().setBackground(SystemColor.inactiveCaption);
        frame.setModal(true);
        frame.setTitle("Gerenciar Entregas");
        frame.setBounds(100, 100, 800, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                listagem();
            }
        });

        scrollPane = new JScrollPane();
        scrollPane.setBounds(26, 27, 740, 220);
        frame.getContentPane().add(scrollPane);

        table = new JTable();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                label.setText("");
                if (table.getSelectedRow() >= 0) {
                    Integer id = (Integer) table.getValueAt(table.getSelectedRow(), 0);
                    String data = (String) table.getValueAt(table.getSelectedRow(), 1);
                    Double lat = (Double) table.getValueAt(table.getSelectedRow(), 2);
                    Double lng = (Double) table.getValueAt(table.getSelectedRow(), 3);
                    String entregador = (String) table.getValueAt(table.getSelectedRow(), 4);
                    
                    textFieldId.setText(id.toString());
                    textFieldData.setText(data);
                    textFieldLatitude.setText(lat.toString());
                    textFieldLongitude.setText(lng.toString());
                    textFieldEntregador.setText(entregador);
                    labelInfo.setText("Entrega selecionada: #" + id);
                }
            }
        });
        table.setGridColor(Color.BLACK);
        table.setRequestFocusEnabled(false);
        table.setFocusable(false);
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(true);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        scrollPane.setViewportView(table);
        table.setBorder(new LineBorder(new Color(0, 0, 0)));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JLabel lblId = new JLabel("ID:");
        lblId.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblId.setBounds(26, 270, 30, 20);
        frame.getContentPane().add(lblId);

        textFieldId = new JTextField();
        textFieldId.setEditable(false);
        textFieldId.setBounds(60, 270, 50, 20);
        frame.getContentPane().add(textFieldId);

        JLabel lblData = new JLabel("Data:");
        lblData.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblData.setBounds(130, 270, 50, 20);
        frame.getContentPane().add(lblData);

        textFieldData = new JTextField();
        textFieldData.setBounds(180, 270, 100, 20);
        frame.getContentPane().add(textFieldData);

        JLabel lblLat = new JLabel("Latitude:");
        lblLat.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblLat.setBounds(300, 270, 70, 20);
        frame.getContentPane().add(lblLat);

        textFieldLatitude = new JTextField();
        textFieldLatitude.setBounds(370, 270, 100, 20);
        frame.getContentPane().add(textFieldLatitude);

        JLabel lblLng = new JLabel("Longitude:");
        lblLng.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblLng.setBounds(490, 270, 80, 20);
        frame.getContentPane().add(lblLng);

        textFieldLongitude = new JTextField();
        textFieldLongitude.setBounds(570, 270, 100, 20);
        frame.getContentPane().add(textFieldLongitude);

        JLabel lblEntregador = new JLabel("Entregador:");
        lblEntregador.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblEntregador.setBounds(26, 305, 90, 20);
        frame.getContentPane().add(lblEntregador);

        textFieldEntregador = new JTextField();
        textFieldEntregador.setBounds(120, 305, 150, 20);
        frame.getContentPane().add(textFieldEntregador);

        btnCriar = new JButton("Criar Entrega");
        btnCriar.setBackground(SystemColor.control);
        btnCriar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    label.setText("");
                    if (textFieldData.getText().isEmpty() || 
                        textFieldLatitude.getText().isEmpty() ||
                        textFieldLongitude.getText().isEmpty() ||
                        textFieldEntregador.getText().isEmpty())
                        throw new Exception("Preencha todos os campos!");

                    String pedidosStr = JOptionPane.showInputDialog(frame, 
                        "Informe os IDs dos pedidos separados por vírgula:\n(ex: 1,2)");
                    
                    if (pedidosStr == null || pedidosStr.isEmpty())
                        throw new Exception("Pedidos não informados!");

                    Entregador ent = Fachada.localizarEntregadorPorNome(
                        textFieldEntregador.getText());
                    if (ent == null)
                        throw new Exception("Entregador não encontrado!");

                    Entrega entrega = new Entrega();
                    entrega.setData(textFieldData.getText());
                    entrega.setLatitude(Double.parseDouble(textFieldLatitude.getText()));
                    entrega.setLongitude(Double.parseDouble(textFieldLongitude.getText()));
                    entrega.setEntregador(ent);

                    String[] ids = pedidosStr.split(",");
                    for (String idStr : ids) {
                        int pedidoId = Integer.parseInt(idStr.trim());
                        Pedido pedido = Fachada.localizarPedidoPorId(pedidoId);
                        if (pedido == null)
                            throw new Exception("Pedido #" + pedidoId + " não encontrado!");
                        entrega.adicionarPedido(pedido);
                    }

                    Fachada.criarEntrega(entrega);

                    label.setText("Entrega criada com sucesso!");
                    listagem();
                    btnLimpar.doClick();

                } catch (NumberFormatException ex) {
                    label.setText("Formato numérico inválido!");
                } catch (Exception ex) {
                    label.setText(ex.getMessage());
                }
            }
        });
        btnCriar.setBounds(26, 345, 130, 25);
        frame.getContentPane().add(btnCriar);

        btnApagar = new JButton("Apagar");
        btnApagar.setBackground(SystemColor.control);
        btnApagar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    label.setText("");
                    if (textFieldId.getText().isEmpty())
                        throw new Exception("Selecione uma entrega!");

                    int id = Integer.parseInt(textFieldId.getText());
                    int resposta = JOptionPane.showConfirmDialog(frame,
                        "Confirma exclusão da entrega #" + id + "?",
                        "Confirmação", JOptionPane.YES_NO_OPTION);

                    if (resposta == JOptionPane.YES_OPTION) {
                        Fachada.apagarEntrega(id);
                        label.setText("Entrega #" + id + " apagada!");
                        listagem();
                        btnLimpar.doClick();
                    }

                } catch (Exception ex) {
                    label.setText(ex.getMessage());
                }
            }
        });
        btnApagar.setBounds(170, 345, 100, 25);
        frame.getContentPane().add(btnApagar);

        btnLimpar = new JButton("Limpar");
        btnLimpar.setBackground(SystemColor.control);
        btnLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textFieldId.setText("");
                textFieldData.setText("");
                textFieldLatitude.setText("");
                textFieldLongitude.setText("");
                textFieldEntregador.setText("");
                labelInfo.setText("Selecione uma entrega");
            }
        });
        btnLimpar.setBounds(285, 345, 100, 25);
        frame.getContentPane().add(btnLimpar);

        labelInfo = new JLabel("Selecione uma entrega");
        labelInfo.setBounds(26, 250, 740, 14);
        frame.getContentPane().add(labelInfo);

        label = new JLabel("");
        label.setForeground(Color.BLUE);
        label.setBounds(26, 400, 740, 14);
        frame.getContentPane().add(label);
    }

    public void listagem() {
        try {
            List<Entrega> lista = Fachada.listarEntregas();

            DefaultTableModel model = new DefaultTableModel();
            table.setModel(model);

            // Colunas
            model.addColumn("ID");
            model.addColumn("Data");
            model.addColumn("Latitude");
            model.addColumn("Longitude");
            model.addColumn("Entregador");
            model.addColumn("Qtd Pedidos");

            // Linhas
            for (Entrega e : lista) {
                model.addRow(new Object[] {
                    e.getId(),
                    e.getData(),
                    e.getLatitude(),
                    e.getLongitude(),
                    e.getEntregador() != null ? e.getEntregador().getNome() : "N/A",
                    e.getPedidos().size()
                });
            }

            labelInfo.setText("Resultados: " + lista.size() + " entrega(s)");

        } catch (Exception ex) {
            label.setText(ex.getMessage());
        }
    }
}