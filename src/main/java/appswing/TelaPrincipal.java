package appswing;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import model.Entregador;
import requisito.Fachada;

public class TelaPrincipal {

    private JFrame frame;
    private JLabel ImagemLabel;
    private JMenuBar menuBar;
    private JMenu mnEntregador;
    private JMenu mnPedido;
    private JMenu mnEntrega;
    private JMenu mnConsulta;


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TelaPrincipal window = new TelaPrincipal();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Erro ao iniciar aplicação: " + e.getMessage());
                }
            }
        });
    }

    public TelaPrincipal() {
        initialize();

        List<Entregador> entregadores = Fachada.listarEntregadores();
        if (entregadores.isEmpty()) {
            System.out.println("Cadastrando dados iniciais...");
            new appconsole.Cadastrar();
        }
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Sistema de Entregas - EntregaJá");
        frame.setBounds(100, 100, 500, 350);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setResizable(false);

        ImagemLabel = new JLabel("Sistema de Entregas");
        ImagemLabel.setHorizontalAlignment(JLabel.CENTER);
        ImagemLabel.setBounds(0, 0, 500, 320);

        try {
            java.net.URL url = getClass().getResource("/imagens/logo.png");
            BufferedImage img = null;
            if (url != null) {
                img = ImageIO.read(url);
            }
            

            Image scaled = getScaledImageProportional(img, 500, 320);
            ImagemLabel.setIcon(new ImageIcon(scaled));
            ImagemLabel.setText("");

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Não foi possível carregar imagem: " + ex.getMessage());
        }

        frame.getContentPane().add(ImagemLabel);

        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        mnEntregador = new JMenu("Entregador");
        mnEntregador.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new TelaEntregador();
            }
        });

        mnPedido = new JMenu("Pedido");
        mnPedido.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new TelaPedido();
            }
        });

        mnEntrega = new JMenu("Entrega");
        mnEntrega.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new TelaEntrega();
            }
        });

        mnConsulta = new JMenu("Consulta");
        mnConsulta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new TelaConsulta();
            }
        });

        menuBar.add(mnEntregador);
        menuBar.add(mnPedido);
        menuBar.add(mnEntrega);
        menuBar.add(mnConsulta);
    }

    private Image getScaledImageProportional(BufferedImage srcImg, int maxW, int maxH) {
        int w = srcImg.getWidth();
        int h = srcImg.getHeight();
        double scale = Math.min((double) maxW / w, (double) maxH / h);
        int newW = (int) (w * scale);
        int newH = (int) (h * scale);
        Image tmp = srcImg.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resized.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(tmp, 0, 0, null);
        g2.dispose();
        return resized;
    }
}