package jentityframework;

public class DbSet<TEntity extends Object> extends Queryable<TEntity> {

	public DbSet(Class<TEntity> tEntity) {
		super(tEntity);
	}

}
