import javax.swing.SwingUtilities; // Importa SwingUtilities

public class Main {
    public static void main(String[] args) {
        // Interface gráfica deve rodar na "thread de eventos"
        SwingUtilities.invokeLater(() -> new JanelaPrincipal());
    }
}