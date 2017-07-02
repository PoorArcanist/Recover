package br.univel;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.postgresql.Driver;

public class TelaPrincipal extends TelaPrincipalBase{
	
	private final String SQL_CRIACAO_TABELAS = "CREATE TABLE cliente(id_cliente INTEGER,nome_cliente VARCHAR(255),telefone VARCHAR(25));"
			+ "CREATE TABLE produto(id_produto INTEGER,nome_produto VARCHAR(255),valor numeric(17,2));"
			+ "CREATE TABLE orcamento(id integer,nome varchar(255),valor numeric(17,2));";
	JTabbedPane tabbedPane = super.tabbedPane;
	List<Produto> listaMaster10;
	
	public TelaPrincipal() {
		super.setVisible(true);
		configuraMenu();
		configuraBotoes();
	    ChangeListener changeListener = new ChangeListener() {
	        public void stateChanged(ChangeEvent changeEvent) {
	          JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
	          int index = sourceTabbedPane.getSelectedIndex();
	          System.out.println("Tab changed to: " + sourceTabbedPane.getTitleAt(index));
	        }
	      };
	      tabbedPane.addChangeListener(changeListener);

		
	}
	
	private void configuraMenu() {
		super.mntmCriarTabelas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				criarTabelas();
			}
		});
		super.mntmInserirLista.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				inserirLista();
			}
		});
		
	}

	protected void inserirLista() {
		LeitorLista ll = new LeitorLista();
		try {
			listaMaster10 = ll.lerProdutos("http://www.master10.com.py/lista-txt/download");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		for(int i =1; i<listaMaster10.size();i++){
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO produto VALUES(");
			sb.append(listaMaster10.get(i).getId());
			sb.append(",'");
			sb.append(listaMaster10.get(i).getNome());
			sb.append("',");
			sb.append(listaMaster10.get(i).getValor());
			sb.append(")");
			
			Connection con = ConexaoDB.getInstance().getConexao();
			try {
				PreparedStatement ps = con.prepareStatement(sb.toString());
				ps.executeQuery();
			} catch (SQLException e) {
			}
		}
		
	}

	protected void criarTabelas() {
		Connection con = ConexaoDB.getInstance().getConexao();
		try {
			PreparedStatement ps = con.prepareStatement(SQL_CRIACAO_TABELAS);
			ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if(e.getMessage().contains("already exists")){
				JOptionPane.showMessageDialog(null,"As tabelas ja existem!");
			}
			else{
				JOptionPane.showMessageDialog(null, "Tabelas criadas com sucesso!");
			}
		}
	}

	private void configuraBotoes() {
		super.btnOrcamento.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.addTab("Orcamento",new PainelOrcamento(tabbedPane));
				
			}
		});
		
		super.btnCliente.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.addTab("Cliente", new PainelCliente(tabbedPane));
			}
		});
		super.btnProduto.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.addTab("Produto", new PainelProduto(tabbedPane));
			}
		});
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new TelaPrincipal();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

}
