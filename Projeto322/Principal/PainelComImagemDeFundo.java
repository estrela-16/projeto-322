package Principal;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PainelComImagemDeFundo extends JPanel {

    private Image imagemDeFundo;

    public PainelComImagemDeFundo(String caminhoDaImagem) {
        try {
            // Carrega a imagem a partir do caminho fornecido
            ImageIcon icon = new ImageIcon(getClass().getResource(caminhoDaImagem));
            this.imagemDeFundo = icon.getImage();
        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem de fundo: " + caminhoDaImagem);
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagemDeFundo != null) {
            // Desenha a imagem para preencher todo o painel
            // Isso faz com que a imagem se redimensione junto com a janela
            g.drawImage(imagemDeFundo, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}