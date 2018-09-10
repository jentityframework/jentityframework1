package jentityframework.jdbc;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class ConnectToDatabase {

	public static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Gson gson = new Gson();
			JsonReader jsonReader = new JsonReader(new FileReader("src\\appsettings.json"));
			ConnectionInfo connectionInfo = gson.fromJson(jsonReader, ConnectionInfo.class);
			String url = connectionInfo.getConnectionString().getUrl().trim();
			String username = connectionInfo.getConnectionString().getUsername().trim();
			String password = connectionInfo.getConnectionString().getPassword().trim();
			connection = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			connection = null;
		}
		return connection;
	}

}
