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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
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
import javax.swing.ImageIcon;
import java.awt.Image;

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
    private JLabel lblFotoPreview;
    private JButton btnCarregarFoto;

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
                    // colunas: 0=ID, 1=Foto, 2=Nome, 3=Total Entregas
                    String nome = (String) table.getValueAt(table.getSelectedRow(), 2);
                    Integer totalEntregas = (Integer) table.getValueAt(table.getSelectedRow(), 3);
                    
                    textFieldId.setText(id.toString());
                    textFieldNome.setText(nome);
                    labelInfo.setText("Selecionado - Total de entregas: " + totalEntregas);

                    // carrega foto do entregador selecionado  
                    try {
                        Entregador eobj = Fachada.localizarEntregadorPorId(id);
                        if (eobj != null && eobj.getFoto() != null && eobj.getFoto().length > 0) {
                            showFotoPreview(eobj.getFoto());
                        } else {
                            clearFotoPreview();
                        }
                    } catch (Exception ex) {
                        clearFotoPreview();
                        label.setText(ex.getMessage());
                    }
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
                clearFotoPreview();
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

        // foto preview 
        lblFotoPreview = new JLabel();
        lblFotoPreview.setHorizontalAlignment(SwingConstants.CENTER);
        lblFotoPreview.setBorder(new LineBorder(Color.GRAY));
        lblFotoPreview.setBounds(520, 220, 106, 106); 
        frame.getContentPane().add(lblFotoPreview);

        // botão para carregar foto
        btnCarregarFoto = new JButton("Carregar Foto");
        btnCarregarFoto.setBackground(SystemColor.control);
        btnCarregarFoto.setBounds(520, 330, 106, 25);
        btnCarregarFoto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (textFieldId.getText().isEmpty())
                        throw new Exception("Selecione um entregador!");

                    int id = Integer.parseInt(textFieldId.getText());

                    JFileChooser chooser = new JFileChooser();
                    int ret = chooser.showOpenDialog(frame);
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        File f = chooser.getSelectedFile();
                        byte[] bytes = readFileToBytes(f);
                        if (bytes == null || bytes.length == 0)
                            throw new Exception("Arquivo vazio ou inválido");

                        // chama Fachada pra salvar foto
                        Fachada.atualizarFotoEntregador(id, bytes);

                        label.setText("Foto atualizada para entregador #" + id);
                        listagem();

                        showFotoPreview(bytes);
                    }

                } catch (NumberFormatException ex) {
                    label.setText("ID inválido!");
                } catch (Exception ex) {
                    label.setText(ex.getMessage());
                }
            }
        });
        frame.getContentPane().add(btnCarregarFoto);
    }

    private void showFotoPreview(byte[] fotoBytes) {
        try {
            if (fotoBytes == null || fotoBytes.length == 0) {
                clearFotoPreview();
                return;
            }
            Image img = ImageIO.read(new ByteArrayInputStream(fotoBytes));
            if (img == null) {
                clearFotoPreview();
                return;
            }
            Image scaled = img.getScaledInstance(lblFotoPreview.getWidth(), lblFotoPreview.getHeight(), Image.SCALE_SMOOTH);
            lblFotoPreview.setIcon(new ImageIcon(scaled));
        } catch (IOException ex) {
            clearFotoPreview();
            label.setText("Erro ao carregar imagem: " + ex.getMessage());
        }
    }

    private void clearFotoPreview() {
        lblFotoPreview.setIcon(null);
        lblFotoPreview.setText("Sem foto");
    }

    private byte[] readFileToBytes(File f) throws IOException {
        try (FileInputStream fis = new FileInputStream(f)) {
            byte[] data = new byte[(int) f.length()];
            int read = fis.read(data);
            if (read != data.length) {
                int offset = read;
                while (offset < data.length) {
                    int r = fis.read(data, offset, data.length - offset);
                    if (r < 0) break;
                    offset += r;
                }
            }
            return data;
        }
    }

    public void listagem() {
        try {
            List<Entregador> lista = Fachada.listarEntregadores();

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    switch (columnIndex) {
                        case 0: return Integer.class; // ID
                        case 1: return ImageIcon.class; // Foto
                        case 2: return String.class; // Nome
                        case 3: return Integer.class; // Total Entregas
                        default: return Object.class;
                    }
                }

                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            table.setModel(model);

            // Colunas
            model.addColumn("ID");
            model.addColumn("Foto");
            model.addColumn("Nome");
            model.addColumn("Total Entregas");

            // Ajustes visuais
            table.setRowHeight(60);

            // Linhas
            for (Entregador ent : lista) {
                int totalEntregas = ent.getListaDeEntrega() != null ? 
                    ent.getListaDeEntrega().size() : 0;

                ImageIcon icon = null;
                try {
                    if (ent.getFoto() != null && ent.getFoto().length > 0) {
                        Image img = ImageIO.read(new ByteArrayInputStream(ent.getFoto()));
                        if (img != null) {
                            Image scaled = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(scaled);
                        }
                    }
                } catch (IOException ex) {
                }

                model.addRow(new Object[] {
                    ent.getId(),
                    icon,
                    ent.getNome(),
                    totalEntregas
                });
            }

            // Ajuste de largura das colunas 
            if (table.getColumnModel().getColumnCount() >= 4) {
                table.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
                table.getColumnModel().getColumn(1).setPreferredWidth(60); // Foto
                table.getColumnModel().getColumn(2).setPreferredWidth(350); // Nome
                table.getColumnModel().getColumn(3).setPreferredWidth(120); // Total Entregas
            }

            labelInfo.setText("Resultados: " + lista.size() + " entregador(es)");

            if (table.getRowCount() > 0) {
                table.setRowSelectionInterval(0, 0);
                Object idObj = table.getValueAt(0, 0);
                if (idObj instanceof Integer) {
                    int firstId = (Integer) idObj;
                    Object nomeObj = table.getValueAt(0, 2);
                    if (nomeObj instanceof String) {
                        textFieldId.setText(Integer.toString(firstId));
                        textFieldNome.setText((String) nomeObj);
                    }
                    try {
                        Entregador first = Fachada.localizarEntregadorPorId(firstId);
                        if (first != null && first.getFoto() != null && first.getFoto().length > 0) {
                            showFotoPreview(first.getFoto());
                            labelInfo.setText("Selecionado - Total de entregas: " + (first.getListaDeEntrega() != null ? first.getListaDeEntrega().size() : 0));
                        } else {
                            clearFotoPreview();
                        }
                    } catch (Exception ex) {
                        clearFotoPreview();
                    }
                }
            }

            if (table.getSelectedRow() < 0) {
                clearFotoPreview();
            }

        } catch (Exception ex) {
            label.setText(ex.getMessage());
        }
    }
}