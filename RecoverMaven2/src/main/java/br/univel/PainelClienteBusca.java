package br.univel;

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

public class PainelClienteBusca extends PainelClienteBuscaBase{
	ModeloTabelaCliente model;
	public PainelClienteBusca() {
		configuraTabela();
	}


	private void configuraTabela() {
		Connection con = ConexaoDB.getInstance().getConexao();
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
	}

	public void setAcaoFecharKey(KeyListener key) {
		super.table.addKeyListener(key);	
	}


	public void setAcaoFecharMouse(MouseAdapter mouse) {
		super.table.addMouseListener(mouse);
		
	}

}
