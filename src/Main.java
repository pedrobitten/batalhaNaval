import Model.*;
import View.*;

import javax.swing.*;

public class Main {
	public static void main(String[] args) { 
		/*
		// Criação do tabuleiro e embarcações
		Tabuleiro tabuleiro = new Tabuleiro();
		tabuleiro.criacaoTabuleiros();
		Embarcacao submarino = new Submarino('H'); 
		Embarcacao couracado = new Couracado('H'); 
		Embarcacao cruzador = new Cruzador('H'); 
		Embarcacao destroyer = new Destroyer('H');
		Embarcacao hidroaviao = new Hidroaviao(); // Direção 'H' já está predefinida
			
		// Posicionamento das embarcações
		tabuleiro.insereEmbarcacao(submarino, 'C', 2, '1');
		tabuleiro.insereEmbarcacao(couracado, 'E', 2, '1');
		tabuleiro.insereEmbarcacao(cruzador, 'G', 2, '1');
		tabuleiro.insereEmbarcacao(destroyer, 'I', 2, '1');
		tabuleiro.insereEmbarcacao(hidroaviao, 'K', 2, '1');
		
		// Imprimir o tabuleiro após o posicionamento
		System.out.println("Tabuleiro após posicionamento das embarcações:");
		tabuleiro.imprimeTabuleiroP1(); // Simulação de um ataque
		String resultadoAtaque = tabuleiro.atacar('C', 2, '2'); // Jogador 2 ataca a posição (2, 2)
		System.out.println("Resultado do ataque na posição (C, 2): " + resultadoAtaque); // Verificação de derrota
		boolean derrota = tabuleiro.verificarDerrota('1');
		System.out.println("Jogador 1 foi derrotado: " + derrota);
		
		
		// Salvando o estado do jogo
		tabuleiro.salvarEstado("jogo_salvo.txt");
		System.out.println("Estado do jogo salvo.");
		// Carregar estado do jogo
		tabuleiro.carregarEstado("jogo_salvo.txt");
		System.out.println("Estado do jogo carregado.");
		tabuleiro.imprimeTabuleiroP1();
		
		*/
		
		new TelaMenu();
		
	}
}