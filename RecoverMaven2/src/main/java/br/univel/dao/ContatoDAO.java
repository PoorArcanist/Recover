package br.univel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.univel.Cliente;
import br.univel.conexaoDB.ConexaoDB;
import br.univel.modeloTabela.ModeloTabelaCliente;

public class ContatoDAO {
	Connection con = ConexaoDB.getInstance().getConexao();
	List<Cliente> lista;
	
	public ContatoDAO() {
		
	}

	public List<Cliente> getTodos() {
		String sql_todos = "SELECT * FROM cliente;";
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement(sql_todos);
			ResultSet rs = ps.executeQuery();
			lista = new ArrayList<>();
			
			while(rs.next()){
				Cliente c = new Cliente();
				c.setId(rs.getInt(1));
				c.setNome(rs.getString(2));
				c.setTelefone(rs.getString(3));
				lista.add(c);
			}
		} catch (SQLException e) {
		}
		return lista;
	}
	

}
