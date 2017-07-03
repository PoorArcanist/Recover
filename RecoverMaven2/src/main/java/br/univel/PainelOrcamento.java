package br.univel;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class PainelOrcamento extends PainelOrcamentoBase{
	

	OrcamentoDAO odao = new OrcamentoDAO();
	JTabbedPane tabbedPane;
	List<Produto> orcamento;
	private static final String JASPER_REPORT = "C:\\Users\\seven\\JaspersoftWorkspace\\MyReports\\RecoverReport.jasper";
	public PainelOrcamento(JTabbedPane tabbedPane) {
		super();
		this.tabbedPane = tabbedPane;
		configuraTabela();
		configuraBotoes();
		
	}

	

	private void configuraBotoes() {
		super.btnAdicionarProduto.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				adicionarProduto();
			}
		});
		super.btnX.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				removerAba();
			}
		});
		super.btnImprimir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				imprimir();
			}
		});
	}

	public void imprimir() {
		
		JasperPrint jasperPrintPDF = getPrint();
		Locale locale = Locale.getDefault();
		JasperViewer.viewReport(jasperPrintPDF,false,locale);
		
		
		
	}

	private JasperPrint getPrint() {
		Connection con = ConexaoDB.getInstance().getConexao();
		try {
			return JasperFillManager.fillReport(JASPER_REPORT, null,con);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	
	}

	protected void removerAba() {
		tabbedPane.remove(this);
	}

	protected void adicionarProduto() {
		if(super.txtIdProduto.getText().equals("")){
			JOptionPane.showMessageDialog(null,"Por favor, informe um ID ou selecione um produto utilizando o F2");
		}
		else{
			int id = Integer.parseInt(super.txtIdProduto.getText());
			int idx = -1;
			
			ProdutoDAO pdao = new ProdutoDAO();
			
			List<Produto> listaProdutos = pdao.getTodos();
			for(int i =0;i<listaProdutos.size();i++){
				if(listaProdutos.get(i).getId()==id){
					idx = i;
					System.out.println("Achou!");
					break;
				}
			}
			if(idx == -1){
				JOptionPane.showMessageDialog(null,"Produto não encontrado!");
			}
			else{
				odao.inserir(listaProdutos.get(idx));
				configuraTabela();
			}
			txtIdProduto.setText("");
		}
	}

	private void configuraTabela() {
		orcamento = odao.getTodos();
		super.table.setModel(new ModeloTabelaOrcamento(orcamento));
	}

	public void setAcaoCliente(KeyListener key) {
		txtIdCliente.addKeyListener(key);
	}

	public void setAcaoProduto(KeyListener key) {
		txtIdProduto.addKeyListener(key);
	}

}
