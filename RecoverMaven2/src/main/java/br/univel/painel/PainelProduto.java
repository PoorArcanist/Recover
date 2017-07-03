package br.univel.painel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import br.univel.Produto;
import br.univel.base.PainelProdutoBase;
import br.univel.conexaoDB.ConexaoDB;
import br.univel.dao.ProdutoDAO;
import br.univel.modeloTabela.ModeloTabelaProduto;

public class PainelProduto extends PainelProdutoBase{
	
	Connection con = ConexaoDB.getInstance().getConexao();
	ModeloTabelaProduto model;
	JTabbedPane tabbedPane;
	int selecionado = -1;
	
	public PainelProduto(JTabbedPane tabbedPane) {
		super();
		configuraTabela();
		this.tabbedPane = tabbedPane;
		configuraBotoes();
		
	}
	

	private void configuraBotoes() {
		super.btnExcluir.setEnabled(false);
		
		super.btnSalvar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				salvar();
			}
		});
		super.btnX.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fecharAba();
			}
		});
		super.btnNovo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				limparCampos();
			}
		});
		super.btnExcluir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int sure = JOptionPane.showConfirmDialog(null, "Deseja excluir este Produto?");
				if(sure == 0){
					excluirProduto();
				}
			}
		});
		
	}

	protected void excluirProduto() {
		int idx = table.getSelectedRow();
		Produto p = model.lista.get(idx);
		
		String sql_delete = "Delete from Produto where id_produto=" + p.getId();
		
		try {
			PreparedStatement ps = con.prepareStatement(sql_delete);
			ps.executeQuery();
		} catch (SQLException e) {
			if(e.getMessage().contains("Nenhum resultado foi retornado pela consulta")){
				JOptionPane.showMessageDialog(null,"Produto excluído com sucesso!");
			}
			else{
				JOptionPane.showMessageDialog(null, "Algo deu errado");
			}
		}
		selecionado = -1;
		limparCampos();
		super.btnExcluir.setEnabled(false);
		configuraTabela();
	}


	protected void limparCampos() {
		selecionado = -1;
		super.txtId.setText("");
		super.txtNome.setText("");
		super.txtValor.setText("");
		super.btnExcluir.setEnabled(false);
	}


	protected void fecharAba() {
		tabbedPane.remove(this);
	}

	protected void salvar() {
		if(selecionado==-1){
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO produto VALUES(");
			sb.append(Integer.parseInt(super.txtId.getText()));
			sb.append(",'");
			sb.append(super.txtNome.getText());
			sb.append("',");
			sb.append(super.txtValor.getText());
			sb.append(");");
			try {
				PreparedStatement ps = con.prepareStatement(sb.toString());
				ps.executeQuery();
			} catch (SQLException e) {
				if(e.getMessage().contains("Nenhum resultado foi retornado pela consulta")){
					JOptionPane.showMessageDialog(null,"Produto cadastrado com sucesso!");
				}
				else{
					JOptionPane.showMessageDialog(null, "Algo deu errado");
				}
			}
		}
		else{
			String delete = "DELETE FROM produto WHERE id_produto=" + this.model.lista.get(selecionado).getId();
			System.out.println(delete);
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO produto VALUES(");
			sb.append(Integer.parseInt(super.txtId.getText()));
			sb.append(",'");
			sb.append(super.txtNome.getText());
			sb.append("',");
			sb.append(super.txtValor.getText());
			sb.append(");");
			
			try {
				
				PreparedStatement ps = con.prepareStatement(delete);
				ps.executeQuery();

				
			} catch (SQLException e) {
				
			}finally {
				
			
				try {
					PreparedStatement ps = con.prepareStatement(sb.toString());
					ps.executeQuery();
				} catch (SQLException e) {
					if(e.getMessage().contains("Nenhum resultado foi retornado pela consulta")){
						JOptionPane.showMessageDialog(null,"Produto alterado com sucesso!");
					}
					else{
						JOptionPane.showMessageDialog(null, "Algo deu errado");
					}
				}

			}
			
		}
		super.btnExcluir.setEnabled(false);
		limparCampos();
		configuraTabela();
	}

	private void configuraTabela() {
		ProdutoDAO pr = new ProdutoDAO();
		List<Produto> lista = pr.getTodos();
		model = new ModeloTabelaProduto(lista);
		super.table.setModel(model);


		super.table.addMouseListener(new MouseAdapter() {
			
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2){
					int idx = table.getSelectedRow();
					Produto p = model.lista.get(idx);
					carregarLinha(p);
					selecionado = idx;

					btnExcluir.setEnabled(true);
				}
				
			}
		});
		
		
		
	}


	protected void carregarLinha(Produto p) {
		super.txtId.setText(String.valueOf(p.getId()));
		super.txtNome.setText(p.getNome());
		super.txtValor.setText(String.valueOf(p.getValor()));
	}

}
