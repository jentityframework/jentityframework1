package jentityframework;

import java.util.List;

import javax.persistence.Table;

import jentityframework.jdbc.JDBCQueryable;
import jentityframework.lamdasql.Lambda2Sql;
import jentityframework.lamdasql.SqlPredicate;

public class QueryPredicate<TEntity extends Object> {

	private SqlPredicate<TEntity> sqlPredicate;
	private Class<TEntity> tEntity = null;
	private String sortFieldName = "";
	private String countSQL = "";
	private String sumFieldName = "";
	private String maxFieldName = "";
	private String minFieldName = "";
	private String avgFieldName = "";
	private OrderBy orderBy = OrderBy.ASC;
	private int limit = -1;
	private int start = -1;

	public QueryPredicate() {
	}

	public QueryPredicate(Class<TEntity> tEntity) {
		this.tEntity = tEntity;
	}

	public QueryPredicate<TEntity> orderBy(String sortFieldName, OrderBy OrderBy) {
		this.sortFieldName = sortFieldName;
		this.orderBy = OrderBy;
		return this;
	}

	public QueryPredicate<TEntity> orderBy(String sortFieldName, OrderBy OrderBy, int limit) {
		this.sortFieldName = sortFieldName;
		this.orderBy = OrderBy;
		this.start = 0;
		this.limit = limit;
		return this;
	}

	public QueryPredicate<TEntity> orderBy(String sortFieldName, OrderBy OrderBy, int start, int limit) {
		this.sortFieldName = sortFieldName;
		this.orderBy = OrderBy;
		this.start = start;
		this.limit = limit;
		return this;
	}

	public QueryPredicate<TEntity> skip(int n) {
		this.setStart(n);
		return this;
	}

	public QueryPredicate<TEntity> take(int n) {
		this.setLimit(n);
		return this;
	}

	public long count() {
		JDBCQueryable<TEntity> jdbcQueryable = new JDBCQueryable<TEntity>();
		return jdbcQueryable.count(this.buildSQL());
	}

	public double sum(String fieldName) {
		JDBCQueryable<TEntity> jdbcQueryable = new JDBCQueryable<TEntity>();
		this.maxFieldName = fieldName;
		return jdbcQueryable.sumOrMinOrMaxOrAvg(this.buildSQL());
	}

	public double max(String fieldName) {
		JDBCQueryable<TEntity> jdbcQueryable = new JDBCQueryable<TEntity>();
		this.maxFieldName = fieldName;
		return jdbcQueryable.sumOrMinOrMaxOrAvg(this.buildSQL());
	}

	public double min(String fieldName) {
		JDBCQueryable<TEntity> jdbcQueryable = new JDBCQueryable<TEntity>();
		this.minFieldName = fieldName;
		return jdbcQueryable.sumOrMinOrMaxOrAvg(this.buildSQL());
	}

	public double avg(String fieldName) {
		JDBCQueryable<TEntity> jdbcQueryable = new JDBCQueryable<TEntity>();
		this.avgFieldName = fieldName;
		return jdbcQueryable.sumOrMinOrMaxOrAvg(this.buildSQL());
	}

	public TEntity singleOrDefault() {
		JDBCQueryable<TEntity> jdbcQueryable = new JDBCQueryable<TEntity>();
		return jdbcQueryable.find(this.buildSQL(), tEntity);
	}

	public List<TEntity> toList() {
		JDBCQueryable<TEntity> jdbcQueryable = new JDBCQueryable<TEntity>();
		return jdbcQueryable.findAll(this.buildSQL(), tEntity);
	}

	private String buildSQL() {
		String tableName = tEntity.getSimpleName();
		if (tEntity.isAnnotationPresent(Table.class)) {
			tableName = tEntity.getAnnotation(Table.class).name();
		}
		String query = "select * ";
		if (countSQL.equalsIgnoreCase("count")) {
			query = "select count(*) ";
		}
		if (!this.sumFieldName.isEmpty()) {
			query = "select sum(" + this.sumFieldName + ") ";
		}
		if (!this.minFieldName.isEmpty()) {
			query = "select min(" + this.minFieldName + ") ";
		}
		if (!this.maxFieldName.isEmpty()) {
			query = "select max(" + this.maxFieldName + ") ";
		}
		if (!this.avgFieldName.isEmpty()) {
			query = "select avg(" + this.avgFieldName + ") ";
		}
		query += " from " + tableName;
		if (sqlPredicate != null) {
			String whereClause = Lambda2Sql.toSql(sqlPredicate);
			query += " where " + whereClause;
		}
		if (!sortFieldName.isEmpty()) {
			query += " order by " + sortFieldName;
			query += orderBy == OrderBy.ASC ? " asc " : " desc ";
		}
		if (limit != -1 && start == -1) {
			query += " limit " + limit;
		}
		if (limit != -1 && start != -1) {
			query += " limit " + start + ", " + limit;
		}
		System.out.println(query);
		return query;
	}

	public void setAvgFieldName(String avgFieldName) {
		this.avgFieldName = avgFieldName;
	}

	public void setMaxFieldName(String maxFieldName) {
		this.maxFieldName = maxFieldName;
	}

	public void setMinFieldName(String minFieldName) {
		this.minFieldName = minFieldName;
	}

	public void setSqlPredicate(SqlPredicate<TEntity> sqlPredicate) {
		this.sqlPredicate = sqlPredicate;
	}

	public void settEntity(Class<TEntity> tEntity) {
		this.tEntity = tEntity;
	}

	public void setSortFieldName(String sortFieldName) {
		this.sortFieldName = sortFieldName;
	}

	public void setCountSQL(String countSQL) {
		this.countSQL = countSQL;
	}

	public void setSumFieldName(String sumFieldName) {
		this.sumFieldName = sumFieldName;
	}

	public void setOrderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setStart(int start) {
		this.start = start;
	}

}
