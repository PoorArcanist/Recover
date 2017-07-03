package br.univel;

import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.util.List;

public class PainelProdutoBusca extends PainelProdutoBuscaBase{
	
	public PainelProdutoBusca() {
		configuraTabela();
		
	}

	private void configuraTabela() {
		ProdutoDAO pd = new ProdutoDAO();
		List<Produto> lista = pd.getTodos();
		table.setModel(new ModeloTabelaProduto(lista));
		
	}

	public void setAcaoFecharKey(KeyListener key) {
		table.addKeyListener(key);
	}

	public void setAcaoFecharMouse(MouseAdapter mouse) {
		table.addMouseListener(mouse);
	}
	

}
