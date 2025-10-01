import campominado.modelo.Tabuleiro;
import campominado.visao.TabuleiroConsole;

public class App {
	public static void main(String[] args) {
		Tabuleiro tabuleiro = new Tabuleiro(6, 6, 3);
		
		new TabuleiroConsole(tabuleiro);
	}
}