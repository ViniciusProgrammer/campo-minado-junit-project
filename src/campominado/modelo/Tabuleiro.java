package campominado.modelo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import campominado.excecao.ExplosaoException;

public class Tabuleiro {
	private int linhas;
	private int colunas;
	private int minas;
	
	private final List<Campo> campos = new ArrayList<Campo>();
	
	public Tabuleiro(int linhas, int colulas, int minas) {
		this.linhas = linhas;
		this.colunas = colulas;
		this.minas = minas;
		
		gerarCampos();
		associarVizinhos();
		sortearAsMinas();
	}

	public void abrir(int linha, int coluna) {
		try {
			campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst().ifPresent(c -> c.abrir());
		} catch (ExplosaoException e) {
			campos.forEach(c -> c.setAberto(true));
			throw e;
		}
	}

	public void alterarMarcacao(int linha, int coluna) {
		campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst().ifPresent(c -> c.alterarMarcacao());
	}
	
	private void gerarCampos() {
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				campos.add(new Campo(i, j));
			}
		}
	}
	
	private void associarVizinhos() {
		for (Campo c1 : campos) {
			for (Campo c2 : campos) {
					c1.adicionarVizinho(c2);
			}
		}
	}
	
	private void sortearAsMinas() {
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		
		do {	
			int aleatorio = (int) (Math.random() * campos.size());
			
			campos.get(aleatorio).minar();
			
			minasArmadas = campos.stream().filter(minado).count();
		} while(minasArmadas < minas);
	}
	
	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	
	public void reiniciarJogo() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearAsMinas();
	}
	
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("  ");
		for (int i = 0; i < colunas; i++) {
			stringBuilder.append(" ");
			stringBuilder.append(i);
			stringBuilder.append(" ");
		}
		
		stringBuilder.append("\n");
		
		int indice = 0;
		
		for (int i = 0; i < linhas; i++) {
			stringBuilder.append(i);
			stringBuilder.append(" ");			
			for (int j = 0; j < colunas; j++) {
				stringBuilder.append(" ");
				stringBuilder.append(campos.get(indice));
				stringBuilder.append(" ");
				indice++;
			}
			stringBuilder.append("\n");
		}
		
		return stringBuilder.toString();
	}
}
