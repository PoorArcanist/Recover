package br.univel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.table.TableModel;

public class PainelCliente extends PainelClienteBase{
	JTabbedPane tabbedPane;
	Connection con = ConexaoDB.getInstance().getConexao();
	ModeloTabelaCliente model;
	private int selecionado = -1;
	
	public PainelCliente(JTabbedPane tabbedPane) {
		super();
		configuraTabela();
		this.tabbedPane = tabbedPane;
		configuraBotoes();
		
	}
		

	private void configuraTabela() {
		
		String sql_todos = "SELECT * FROM cliente ORDER BY id_cliente";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql_todos);
			ResultSet rs = ps.executeQuery();
			List<Cliente> lista = new ArrayList<>();
			
			while(rs.next()){
				Cliente c = new Cliente();
				c.setId(rs.getInt(1));
				c.setNome(rs.getString(2));
				c.setTelefone(rs.getString(3));
				lista.add(c);
			}
			model = new ModeloTabelaCliente(lista);
			super.table.setModel(model);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.table.addMouseListener(new MouseAdapter() {
			
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2){
					int idx = table.getSelectedRow();
					Cliente c = model.lista.get(idx);
					carregarLinha(c);
					selecionado = idx;
					btnExcluir.setEnabled(true);
				}
				
			}
		});
		
		
		
	}

	protected void carregarLinha(Cliente c) {
		super.txtId.setText(String.valueOf(c.getId()));
		super.txtNome.setText(c.getNome());
		super.txtTelefone.setText(c.getTelefone());
	}

	private void configuraBotoes() {
		super.btnExcluir.setEnabled(false);
		super.btnX.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fecharAba();
			}
		});
		super.btnSalvar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				salvar();
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
					excluirCliente();
				}
			}
		});
	}

	protected void excluirCliente() {
		int idx = table.getSelectedRow();
		Cliente c = model.lista.get(idx);
		
		String sql_delete = "Delete from Cliente where id_cliente=" + c.getId();
		
		try {
			PreparedStatement ps = con.prepareStatement(sql_delete);
			ps.executeQuery();
		} catch (SQLException e) {
			if(e.getMessage().contains("Nenhum resultado foi retornado pela consulta")){
				JOptionPane.showMessageDialog(null,"Produto excluído com sucesso!");
			}
			else{
				JOptionPane.showMessageDialog(null, "Algo deu errado");
				e.printStackTrace();
			}
		}
		selecionado = -1;
		limparCampos();
		super.btnExcluir.setEnabled(false);
		configuraTabela();
	}

	protected void salvar() {
		
		if(selecionado==-1){
			System.out.println("entrou");
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO Cliente VALUES(");
			sb.append(Integer.parseInt(super.txtId.getText()));
			sb.append(",'");
			sb.append(super.txtNome.getText());
			sb.append("','");
			sb.append(super.txtTelefone.getText());
			sb.append("');");
			
			try {
				PreparedStatement ps = con.prepareStatement(sb.toString());
				ps.executeQuery();
			} catch (SQLException e) {
				if(e.getMessage().contains("Nenhum resultado foi retornado pela consulta")){
					JOptionPane.showMessageDialog(null,"Cliente cadastrado com sucesso!");
				}
				else{
					JOptionPane.showMessageDialog(null, "Algo deu errado");
					e.printStackTrace();
				}
			}
		}
		else{
			String delete = "DELETE FROM cliente WHERE id_cliente=" + this.model.lista.get(selecionado).getId();
			System.out.println(delete);
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO cliente VALUES(");
			sb.append(Integer.parseInt(super.txtId.getText()));
			sb.append(",'");
			sb.append(super.txtNome.getText());
			sb.append("','");
			sb.append(super.txtTelefone.getText());
			sb.append("');");
			
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
						JOptionPane.showMessageDialog(null,"Cliente alterado com sucesso!");
					}
					else{
						JOptionPane.showMessageDialog(null, "Algo deu errado");
					}
				}

			}
			
		}
		selecionado = -1;
		super.btnExcluir.setEnabled(false);
		limparCampos();
		configuraTabela();
	}

	private void limparCampos() {
		selecionado = -1;
		super.txtId.setText("");
		super.txtNome.setText("");
		super.txtTelefone.setText("");
	}

	protected void fecharAba() {
		tabbedPane.remove(this);
	}
}
