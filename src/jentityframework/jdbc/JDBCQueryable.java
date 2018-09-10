package jentityframework.jdbc;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JDBCQueryable<T extends Object> {

	@SuppressWarnings("unchecked")
	public List<T> findAll(String query, Class<T> clazz) {
		List<T> result = new ArrayList<T>();
		try {
			PreparedStatement preparedStatement = ConnectToDatabase.getConnection().prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Object objectInstance = clazz.newInstance();
				ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
				int countColumns = resultSetMetaData.getColumnCount();
				for (int i = 1; i <= countColumns; i++) {
					String columnName = resultSetMetaData.getColumnName(i);
					String columnType = resultSetMetaData.getColumnTypeName(i);
					Field field = clazz.getDeclaredField(columnName);
					field.setAccessible(true);
					Object value = resultSet.getObject(i);
					if (columnType.equalsIgnoreCase("datetime") || columnType.equalsIgnoreCase("date")) {
						LocalDate localDate = new java.sql.Date(((java.util.Date)value).getTime()).toLocalDate();
						field.set(objectInstance, localDate);
					} else {
						field.set(objectInstance, value);
					}
				}
				result.add((T) objectInstance);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			result = null;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public T find(String query, Class<T> clazz) {
		List<T> result = new ArrayList<T>();
		try {
			PreparedStatement preparedStatement = ConnectToDatabase.getConnection().prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Object objectInstance = clazz.newInstance();
				ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
				int countColumns = resultSetMetaData.getColumnCount();
				for (int i = 1; i <= countColumns; i++) {
					String columnName = resultSetMetaData.getColumnName(i);
					Field field = clazz.getDeclaredField(columnName);
					field.setAccessible(true);
					Object value = resultSet.getObject(i);
					field.set(objectInstance, value);
				}
				result.add((T) objectInstance);
			}
		} catch (Exception e) {
			result = null;
		}
		return result.isEmpty() ? null : result.get(0);
	}

	public Object create(String query) {
		Object generatedKey = null;
		try {
			PreparedStatement preparedStatement = ConnectToDatabase.getConnection().prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			int affectedRows = preparedStatement.executeUpdate();
			if (affectedRows > 0) {
				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				resultSet.next();
				generatedKey = resultSet.getObject(1);
			}
		} catch (Exception e) {
			generatedKey = null;
		}
		return generatedKey;
	}

	public int delete(String query) {
		int affectedRows = 0;
		try {
			PreparedStatement preparedStatement = ConnectToDatabase.getConnection().prepareStatement(query);
			affectedRows = preparedStatement.executeUpdate();
		} catch (Exception e) {
			affectedRows = 0;
		}
		return affectedRows;
	}

	public int update(String query) {
		int affectedRows = 0;
		try {
			PreparedStatement preparedStatement = ConnectToDatabase.getConnection().prepareStatement(query);
			affectedRows = preparedStatement.executeUpdate();
		} catch (Exception e) {
			affectedRows = 0;
		}
		return affectedRows;
	}

	public long count(String query) {
		long result = 0;
		try {
			PreparedStatement preparedStatement = ConnectToDatabase.getConnection().prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			result = resultSet.getInt(1);
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}

	public double sumOrMinOrMaxOrAvg(String query) {
		double result = 0;
		try {
			PreparedStatement preparedStatement = ConnectToDatabase.getConnection().prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			result = resultSet.getInt(1);
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}

}
