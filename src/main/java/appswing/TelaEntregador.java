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
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import model.Entregador;
import model.Entrega;
import requisito.Fachada;

public class TelaEntregador {
    private JDialog frame;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton btnCriar;
    private JButton btnAlterar;
    private JButton btnLimpar;
    private JButton btnApagar;
    private JTextField textFieldId;
    private JTextField textFieldNome;
    private JLabel label;
    private JLabel labelInfo;

    public TelaEntregador() {
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JDialog();
        frame.getContentPane().setBackground(SystemColor.inactiveCaption);
        frame.setModal(true);
        frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 12));
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                listagem();
            }
        });
        frame.setTitle("Gerenciar Entregadores");
        frame.setBounds(100, 100, 680, 400);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(26, 27, 600, 180);
        frame.getContentPane().add(scrollPane);

        table = new JTable();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                label.setText("");
                if (table.getSelectedRow() >= 0) {
                    Integer id = (Integer) table.getValueAt(table.getSelectedRow(), 0);
                    String nome = (String) table.getValueAt(table.getSelectedRow(), 1);
                    Integer totalEntregas = (Integer) table.getValueAt(table.getSelectedRow(), 2);
                    
                    textFieldId.setText(id.toString());
                    textFieldNome.setText(nome);
                    labelInfo.setText("Selecionado - Total de entregas: " + totalEntregas);
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
        JLabel lblId = new JLabel("ID:");
        lblId.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblId.setBounds(26, 230, 30, 20);
        frame.getContentPane().add(lblId);**/

        textFieldId = new JTextField();
        textFieldId.setEditable(false);
        textFieldId.setBounds(60, 230, 50, 20);
        frame.getContentPane().add(textFieldId);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblNome.setBounds(130, 230, 50, 20);
        frame.getContentPane().add(lblNome);

        textFieldNome = new JTextField();
        textFieldNome.setBounds(185, 230, 200, 20);
        frame.getContentPane().add(textFieldNome);

        btnCriar = new JButton("Criar");
        btnCriar.setBackground(SystemColor.control);
        btnCriar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    label.setText("");
                    if (textFieldNome.getText().isEmpty())
                        throw new Exception("Nome vazio!");

                    String nome = textFieldNome.getText();
                    Fachada.criarEntregador(nome);

                    label.setText("Entregador criado: " + nome);
                    listagem();
                    btnLimpar.doClick();

                } catch (Exception ex) {
                    label.setText(ex.getMessage());
                }
            }
        });
        btnCriar.setBounds(26, 270, 100, 25);
        frame.getContentPane().add(btnCriar);

        btnAlterar = new JButton("Alterar Nome");
        btnAlterar.setBackground(SystemColor.control);
        btnAlterar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    label.setText("");
                    if (textFieldId.getText().isEmpty())
                        throw new Exception("Selecione um entregador!");

                    int id = Integer.parseInt(textFieldId.getText());
                    String novoNome = JOptionPane.showInputDialog(frame, "Novo nome:", textFieldNome.getText());
                    
                    if (novoNome == null || novoNome.isEmpty())
                        throw new Exception("Nome vazio!");

                    // chama a Fachada para atualizar
                    Fachada.atualizarEntregador(id, novoNome);

                    label.setText("Entregador atualizado: " + novoNome);
                    listagem();
                    btnLimpar.doClick();

                } catch (NumberFormatException ex) {
                    label.setText("ID inválido!");
                } catch (Exception ex) {
                    label.setText(ex.getMessage());
                }
            }
        });
        btnAlterar.setBounds(140, 270, 130, 25);
        frame.getContentPane().add(btnAlterar);

        btnApagar = new JButton("Apagar");
        btnApagar.setBackground(SystemColor.control);
        btnApagar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    label.setText("");
                    if (textFieldId.getText().isEmpty())
                        throw new Exception("Selecione um entregador!");

                    int id = Integer.parseInt(textFieldId.getText());
                    int resposta = JOptionPane.showConfirmDialog(frame,
                        "Confirma exclusão do entregador #" + id + "?",
                        "Confirmação", JOptionPane.YES_NO_OPTION);

                    if (resposta == JOptionPane.YES_OPTION) {
                        Fachada.apagarEntregador(id);
                        label.setText("Entregador #" + id + " apagado!");
                        listagem();
                        btnLimpar.doClick();
                    }

                } catch (NumberFormatException ex) {
                    label.setText("ID inválido!");
                } catch (Exception ex) {
                    label.setText(ex.getMessage());
                }
            }
        });
        btnApagar.setBounds(285, 270, 100, 25);
        frame.getContentPane().add(btnApagar);

        btnLimpar = new JButton("Limpar");
        btnLimpar.setBackground(SystemColor.control);
        btnLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textFieldId.setText("");
                textFieldNome.setText("");
                labelInfo.setText("Selecione um entregador");
            }
        });
        btnLimpar.setBounds(400, 270, 100, 25);
        frame.getContentPane().add(btnLimpar);

        labelInfo = new JLabel("Selecione um entregador");
        labelInfo.setBounds(26, 207, 600, 14);
        frame.getContentPane().add(labelInfo);

        label = new JLabel("");
        label.setForeground(Color.BLUE);
        label.setBounds(26, 320, 600, 14);
        frame.getContentPane().add(label);
    }

    public void listagem() {
        try {
            List<Entregador> lista = Fachada.listarEntregadores();

            DefaultTableModel model = new DefaultTableModel();
            table.setModel(model);

            // Colunas
            model.addColumn("ID");
            model.addColumn("Nome");
            model.addColumn("Total Entregas");

            // Linhas
            for (Entregador ent : lista) {
                int totalEntregas = ent.getListaDeEntrega() != null ? 
                    ent.getListaDeEntrega().size() : 0;
                model.addRow(new Object[] {
                    ent.getId(), 
                    ent.getNome(), 
                    totalEntregas
                });
            }

            labelInfo.setText("Resultados: " + lista.size() + " entregador(es)");

        } catch (Exception ex) {
            label.setText(ex.getMessage());
        }
    }
}