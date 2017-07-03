package br.univel;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrcamentoDAO {

	Connection con = ConexaoDB.getInstance().getConexao();
	public List<Produto> getTodos(){
		List<Produto> lista = new ArrayList<>();
		String sql_todos = "SELECT * FROM orcamento";
		
		try {
			PreparedStatement ps = con.prepareStatement(sql_todos);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Produto p = new Produto();
				p.setId(rs.getInt(1));
				p.setNome(rs.getString(2));
				p.setValor(BigDecimal.valueOf(rs.getDouble(3)));
				lista.add(p);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return lista;
	}
	public void inserir(Produto produto) {
		StringBuilder inserir = new StringBuilder("INSERT INTO orcamento VALUES(");
		inserir.append(produto.getId());
		inserir.append(",'");
		inserir.append(produto.getNome());
		inserir.append("',");
		inserir.append(produto.getValor());
		inserir.append(");");
		
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(inserir.toString());
			ps.executeQuery();
		} catch (SQLException e) {
		}
	}

}
