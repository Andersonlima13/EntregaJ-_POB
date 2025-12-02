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
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import model.Pedido;
import requisito.Fachada;

public class TelaPedido {
    private JDialog frame;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton btnCriar;
    private JButton btnLimpar;
    private JTextField textFieldId;
    private JTextField textFieldData;
    private JTextField textFieldValor;
    private JTextField textFieldDescricao;
    private JLabel label;
    private JLabel labelInfo;

    public TelaPedido() {
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JDialog();
        frame.getContentPane().setBackground(SystemColor.inactiveCaption);
        frame.setModal(true);
        frame.setTitle("Gerenciar Pedidos");
        frame.setBounds(100, 100, 750, 450);
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
        scrollPane.setBounds(26, 27, 680, 200);
        frame.getContentPane().add(scrollPane);

        table = new JTable();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                label.setText("");
                if (table.getSelectedRow() >= 0) {
                    Integer id = (Integer) table.getValueAt(table.getSelectedRow(), 0);
                    String data = (String) table.getValueAt(table.getSelectedRow(), 1);
                    Double valor = (Double) table.getValueAt(table.getSelectedRow(), 2);
                    String descricao = (String) table.getValueAt(table.getSelectedRow(), 3);
                    
                    textFieldId.setText(id.toString());
                    textFieldData.setText(data);
                    textFieldValor.setText(valor.toString());
                    textFieldDescricao.setText(descricao);
                    labelInfo.setText("Pedido selecionado: #" + id);
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

       
/**
        textFieldId = new JTextField();
        textFieldId.setEditable(false);
        textFieldId.setBounds(60, 250, 50, 20);
        frame.getContentPane().add(textFieldId);**/

        JLabel lblData = new JLabel("Data:");
        lblData.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblData.setBounds(130, 250, 50, 20);
        frame.getContentPane().add(lblData);

        textFieldData = new JTextField();
        textFieldData.setBounds(180, 250, 100, 20);
        frame.getContentPane().add(textFieldData);

        JLabel lblValor = new JLabel("Valor:");
        lblValor.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblValor.setBounds(300, 250, 50, 20);
        frame.getContentPane().add(lblValor);

        textFieldValor = new JTextField();
        textFieldValor.setBounds(350, 250, 80, 20);
        frame.getContentPane().add(textFieldValor);

        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblDescricao.setBounds(26, 285, 80, 20);
        frame.getContentPane().add(lblDescricao);

        textFieldDescricao = new JTextField();
        textFieldDescricao.setBounds(110, 285, 320, 20);
        frame.getContentPane().add(textFieldDescricao);

        btnCriar = new JButton("Criar Pedido");
        btnCriar.setBackground(SystemColor.control);
        btnCriar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    label.setText("");
                    if (textFieldData.getText().isEmpty() || 
                        textFieldValor.getText().isEmpty() ||
                        textFieldDescricao.getText().isEmpty())
                        throw new Exception("Preencha todos os campos!");

                    Pedido p = new Pedido();
                    p.setData(textFieldData.getText());
                    p.setValor(Double.parseDouble(textFieldValor.getText()));
                    p.setDescricao(textFieldDescricao.getText());

                    Fachada.criarPedido(p);

                    label.setText("Pedido criado com sucesso!");
                    listagem();
                    btnLimpar.doClick();

                } catch (NumberFormatException ex) {
                    label.setText("Valor inválido!");
                } catch (Exception ex) {
                    label.setText(ex.getMessage());
                }
            }
        });
        btnCriar.setBounds(26, 325, 130, 25);
        frame.getContentPane().add(btnCriar);

        btnLimpar = new JButton("Limpar");
        btnLimpar.setBackground(SystemColor.control);
        btnLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textFieldId.setText("");
                textFieldData.setText("");
                textFieldValor.setText("");
                textFieldDescricao.setText("");
                labelInfo.setText("Selecione um pedido");
            }
        });
        btnLimpar.setBounds(170, 325, 100, 25);
        frame.getContentPane().add(btnLimpar);

        labelInfo = new JLabel("Selecione um pedido");
        labelInfo.setBounds(26, 230, 680, 14);
        frame.getContentPane().add(labelInfo);

        label = new JLabel("");
        label.setForeground(Color.BLUE);
        label.setBounds(26, 370, 680, 14);
        frame.getContentPane().add(label);
    }

    public void listagem() {
        try {
            List<Pedido> lista = Fachada.listarPedidos();

            DefaultTableModel model = new DefaultTableModel();
            table.setModel(model);

            // Colunas
            model.addColumn("ID");
            model.addColumn("Data");
            model.addColumn("Valor");
            model.addColumn("Descrição");
            model.addColumn("Status Entrega");

            // Linhas
            for (Pedido p : lista) {
                String status = p.getEntrega() != null ? 
                    "Entrega #" + p.getEntrega().getId() : "Sem entrega";
                model.addRow(new Object[] {
                    p.getId(),
                    p.getData(),
                    p.getValor(),
                    p.getDescricao(),
                    status
                });
            }

            labelInfo.setText("Resultados: " + lista.size() + " pedido(s)");

        } catch (Exception ex) {
            label.setText(ex.getMessage());
        }
    }
}