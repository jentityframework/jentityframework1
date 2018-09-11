package jentityframework;

/**
 * @author learningprogramming.net
 */
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import jentityframework.jdbc.JDBCQueryable;
import jentityframework.lamdasql.SqlPredicate;

public class Queryable<TEntity extends Object> {

	private Class<TEntity> tEntity;

	public Queryable() {
	}

	public Queryable(Class<TEntity> tEntity) {
		this.tEntity = tEntity;
	}

	public List<TEntity> toList() {
		QueryPredicate<TEntity> queryPredicate = new QueryPredicate<TEntity>(tEntity);
		return queryPredicate.toList();
	}

	public QueryPredicate<TEntity> Where(SqlPredicate<TEntity> lambda) {
		QueryPredicate<TEntity> queryPredicate = new QueryPredicate<TEntity>(tEntity);
		queryPredicate.setSqlPredicate(lambda);
		return queryPredicate;
	}

	public QueryPredicate<TEntity> orderBy(String sortFieldName, OrderBy OrderBy) {
		QueryPredicate<TEntity> queryPredicate = new QueryPredicate<TEntity>(tEntity);
		queryPredicate.setSortFieldName(sortFieldName);
		queryPredicate.setOrderBy(OrderBy);
		return queryPredicate;
	}

	public QueryPredicate<TEntity> orderBy(String sortFieldName, OrderBy OrderBy, int limit) {
		QueryPredicate<TEntity> queryPredicate = new QueryPredicate<TEntity>(tEntity);
		queryPredicate.setSortFieldName(sortFieldName);
		queryPredicate.setOrderBy(OrderBy);
		queryPredicate.setStart(0);
		queryPredicate.setLimit(limit);
		return queryPredicate;
	}

	public QueryPredicate<TEntity> orderBy(String sortFieldName, OrderBy OrderBy, int start, int limit) {
		QueryPredicate<TEntity> queryPredicate = new QueryPredicate<TEntity>(tEntity);
		queryPredicate.setSortFieldName(sortFieldName);
		queryPredicate.setOrderBy(OrderBy);
		queryPredicate.setStart(start);
		queryPredicate.setLimit(limit);
		return queryPredicate;
	}

	public QueryPredicate<TEntity> skip(int n) {
		QueryPredicate<TEntity> queryPredicate = new QueryPredicate<TEntity>(tEntity);
		queryPredicate.setStart(n);
		return queryPredicate;
	}

	public QueryPredicate<TEntity> take(int n) {
		QueryPredicate<TEntity> queryPredicate = new QueryPredicate<TEntity>(tEntity);
		queryPredicate.setLimit(n);
		return queryPredicate;
	}

	public Object insert(TEntity entity) {
		JDBCQueryable<TEntity> jdbcQueryable = new JDBCQueryable<TEntity>();
		return jdbcQueryable.create(this.generateInsertSQL(entity));
	}

	public int delete(TEntity entity) {
		JDBCQueryable<TEntity> jdbcQueryable = new JDBCQueryable<TEntity>();
		return jdbcQueryable.delete(this.generateDeleteSQL(entity));
	}

	public int update(TEntity entity) {
		JDBCQueryable<TEntity> jdbcQueryable = new JDBCQueryable<TEntity>();
		return jdbcQueryable.update(this.generateUpdateSQL(entity));
	}

	private String generateInsertSQL(TEntity entity) {
		Field[] fields = entity.getClass().getDeclaredFields();
		String campos = "";
		String valores = "";
		boolean flag = false;
		for (int i = 0; i < fields.length; i++) {
			try {
				fields[i].setAccessible(true);
				String name = fields[i].getName();
				String value = fields[i].get(entity).toString();
				boolean isGeneratedValue = fields[i].isAnnotationPresent(GeneratedValue.class);
				if (isGeneratedValue) {
					flag = true;
				}
				if (i != 0) {
					campos = campos + ",";
					valores = valores + ",";
				}
				if (fields[i].get(entity) instanceof String || fields[i].get(entity) instanceof LocalDate) {
					valores = valores + "'" + value + "'";
				} else {
					if (!isGeneratedValue) {
						valores = valores + value;
					}
				}
				if (!isGeneratedValue) {
					campos = campos + name;
				}
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			}
		}
		if (flag) {
			StringBuilder sb = new StringBuilder(campos);
			campos = sb.deleteCharAt(0).toString();
			sb = new StringBuilder(valores);
			valores = sb.deleteCharAt(0).toString();
		}
		String sql = "insert into " + entity.getClass().getSimpleName().toLowerCase() + "(" + campos + ") values("
				+ valores + ")";
		return sql;
	}

	private String generateUpdateSQL(TEntity entity) {
		Field[] fields = entity.getClass().getDeclaredFields();
		String condition = "";
		String values = "";
		for (int i = 0; i < fields.length; i++) {
			try {
				fields[i].setAccessible(true);

				// Get Id Field
				if (fields[i].isAnnotationPresent(Id.class)) {
					String name = fields[i].getName();
					String value = fields[i].get(entity).toString();
					if (fields[i].get(entity) instanceof String) {
						condition = name + " = '" + value + "'";
					} else {
						condition = name + " = " + value;
					}
				} else {
					String name = fields[i].getName();
					String value = fields[i].get(entity).toString();
					if (fields[i].get(entity) instanceof String) {
						values += name + " = '" + value + "', ";
					} else {
						values += name + " = " + value + ", ";
					}
				}
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			}
		}
		StringBuilder sb = new StringBuilder(values.trim());
		values = sb.deleteCharAt(sb.length() - 1).toString();
		String sql = "update " + entity.getClass().getSimpleName().toLowerCase() + " set " + values + " where "
				+ condition;
		return sql;
	}

	private String generateDeleteSQL(TEntity entity) {
		String valores = "";
		String name = "";
		Field[] fields = entity.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				fields[i].setAccessible(true);
				if (fields[i].isAnnotationPresent(Id.class)) {
					name = fields[i].getName();
					String value = fields[i].get(entity).toString();
					if (fields[i].get(entity) instanceof String) {
						valores = valores + "'" + value + "'";
					} else {
						valores = valores + value;
					}
					break;
				}

			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			}
		}
		String sql = "delete from " + entity.getClass().getSimpleName().toLowerCase() + " where " + name + " = "
				+ valores;
		return sql;
	}

	public long count() {
		QueryPredicate<TEntity> queryPredicate = new QueryPredicate<TEntity>(tEntity);
		queryPredicate.setCountSQL("count");
		return queryPredicate.count();
	}

	public double sum(String fieldName) {
		QueryPredicate<TEntity> queryPredicate = new QueryPredicate<TEntity>(tEntity);
		return queryPredicate.sum(fieldName);
	}

	public double min(String fieldName) {
		QueryPredicate<TEntity> queryPredicate = new QueryPredicate<TEntity>(tEntity);
		return queryPredicate.min(fieldName);
	}

	public double max(String fieldName) {
		QueryPredicate<TEntity> queryPredicate = new QueryPredicate<TEntity>(tEntity);
		return queryPredicate.max(fieldName);
	}

	public double avg(String fieldName) {
		QueryPredicate<TEntity> queryPredicate = new QueryPredicate<TEntity>(tEntity);
		return queryPredicate.avg(fieldName);
	}

}
