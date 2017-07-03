package br.univel;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

import br.univel.base.TelaPrincipalBase;
import br.univel.conexaoDB.ConexaoDB;
import br.univel.dao.ContatoDAO;
import br.univel.dao.ProdutoDAO;
import br.univel.painel.PainelCliente;
import br.univel.painel.PainelClienteBusca;
import br.univel.painel.PainelOrcamento;
import br.univel.painel.PainelProduto;
import br.univel.painel.PainelProdutoBusca;

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
		//	    ChangeListener changeListener = new ChangeListener() {
//	        public void stateChanged(ChangeEvent changeEvent) {
//	          JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
//	          int index = sourceTabbedPane.getSelectedIndex();
//	          System.out.println("Tab changed to: " + sourceTabbedPane.getTitleAt(index));
//	        }
//	      };
//	      tabbedPane.addChangeListener(changeListener);

		
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
				configuraKeys();

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

	protected void configuraKeys() {
		PainelOrcamento po = new PainelOrcamento(tabbedPane);
		tabbedPane.addTab("Orcamento",po);
		//Cliente
		po.setAcaoCliente(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {	
			}
			@Override
			public void keyPressed(KeyEvent e) {

				PainelClienteBusca pc = new PainelClienteBusca();
				if(e.getKeyCode() == KeyEvent.VK_F2){
					setGlassPane(pc);
					getGlassPane().setVisible(true);
					pc.setAcaoFecharKey(new KeyListener() {
						@Override
						public void keyTyped(KeyEvent e) {
						}
						@Override
						public void keyReleased(KeyEvent e) {
						}
						@Override
						public void keyPressed(KeyEvent e) {
							if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
								getGlassPane().setVisible(false);
								System.out.println("deu certo");
							}
						}
					});
					pc.setAcaoFecharMouse(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							if(e.getClickCount() == 2){
								getGlassPane().setVisible(false);
								int idx = pc.table.getSelectedRow();
								ContatoDAO cdao = new ContatoDAO();
								po.lblNomeCliente.setText(cdao.getTodos().get(idx).getNome() + ""
										+ "     Telefone: " + cdao.getTodos().get(idx).getTelefone());
							}
						}
					});
				}
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
					getGlassPane().setVisible(false);
					System.out.println("foi esse");
				}
			}
		});
		//Produto
		po.setAcaoProduto(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {	
			}
			@Override
			public void keyPressed(KeyEvent e) {

				PainelProdutoBusca pp = new PainelProdutoBusca();
				if(e.getKeyCode() == KeyEvent.VK_F2){
					setGlassPane(pp);
					getGlassPane().setVisible(true);
					pp.setAcaoFecharKey(new KeyListener() {
						@Override
						public void keyTyped(KeyEvent e) {
						}
						@Override
						public void keyReleased(KeyEvent e) {
						}
						@Override
						public void keyPressed(KeyEvent e) {
							if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
								getGlassPane().setVisible(false);
								System.out.println("deu certo");
							}
						}
					});
					pp.setAcaoFecharMouse(new MouseAdapter() {
						
						public void mouseClicked(MouseEvent e) {
							if(e.getClickCount()==2){
								getGlassPane().setVisible(false);
								int idx = pp.table.getSelectedRow();
								ProdutoDAO p = new ProdutoDAO();
								int id = p.getTodos().get(idx).getId();
								po.txtIdProduto.setText(String.valueOf(id));
								po.adicionarProduto();
							}
							
						}
					});
				}
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
					getGlassPane().setVisible(false);
					System.out.println("foi esse");
				}
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
