package br.univel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {
	
	private static ConexaoDB self;
	
	private Connection con;
	
	private ConexaoDB() {
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/recover", "postgres", "univel");
			Runtime.getRuntime()
			.addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						ConexaoDB.this.con.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}));
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public final static synchronized ConexaoDB getInstance(){
		if(self == null){
			self = new ConexaoDB();
		}
		return self;
	}
	
	public Connection getConexao(){
		
		return con;
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("NAO VAI CLONAR NAO");
	}
}
