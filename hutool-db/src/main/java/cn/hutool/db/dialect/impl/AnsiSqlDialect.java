package cn.hutool.db.dialect.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.Entity;
import cn.hutool.db.Page;
import cn.hutool.db.StatementUtil;
import cn.hutool.db.dialect.Dialect;
import cn.hutool.db.dialect.DialectName;
import cn.hutool.db.sql.Condition;
import cn.hutool.db.sql.Query;
import cn.hutool.db.sql.SqlBuilder;
import cn.hutool.db.sql.QuoteWrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

/**
 * ANSI SQL 方言
 *
 * @author loolly
 *
 */
public class AnsiSqlDialect implements Dialect {
	private static final long serialVersionUID = 2088101129774974580L;

	protected QuoteWrapper quoteWrapper = new QuoteWrapper();

	@Override
	public QuoteWrapper getWrapper() {
		return this.quoteWrapper;
	}

	@Override
	public void setWrapper(final QuoteWrapper quoteWrapper) {
		this.quoteWrapper = quoteWrapper;
	}

	@Override
	public PreparedStatement psForInsert(final Connection conn, final Entity entity) throws SQLException {
		final SqlBuilder insert = SqlBuilder.of(quoteWrapper).insert(entity, this.dialectName());

		return StatementUtil.prepareStatement(conn, insert);
	}

	@Override
	public PreparedStatement psForInsertBatch(final Connection conn, final Entity... entities) throws SQLException {
		if (ArrayUtil.isEmpty(entities)) {
			throw new DbRuntimeException("Entities for batch insert is empty !");
		}
		// 批量，根据第一行数据结构生成SQL占位符
		final SqlBuilder insert = SqlBuilder.of(quoteWrapper).insert(entities[0], this.dialectName());
		final Set<String> fields = CollUtil.filter(entities[0].keySet(), StrUtil::isNotBlank);
		return StatementUtil.prepareStatementForBatch(conn, insert.build(), fields, entities);
	}

	@Override
	public PreparedStatement psForDelete(final Connection conn, final Query query) throws SQLException {
		Assert.notNull(query, "query must be not null !");

		final Condition[] where = query.getWhere();
		if (ArrayUtil.isEmpty(where)) {
			// 对于无条件删除语句直接抛出异常禁止，防止误删除
			throw new SQLException("No 'WHERE' condition, we can't prepared statement for delete everything.");
		}
		final SqlBuilder delete = SqlBuilder.of(quoteWrapper).delete(query.getFirstTableName()).where(where);

		return StatementUtil.prepareStatement(conn, delete);
	}

	@Override
	public PreparedStatement psForUpdate(final Connection conn, final Entity entity, final Query query) throws SQLException {
		Assert.notNull(query, "query must be not null !");

		final Condition[] where = query.getWhere();
		if (ArrayUtil.isEmpty(where)) {
			// 对于无条件地删除语句直接抛出异常禁止，防止误删除
			throw new SQLException("No 'WHERE' condition, we can't prepare statement for update everything.");
		}

		final SqlBuilder update = SqlBuilder.of(quoteWrapper).update(entity).where(where);

		return StatementUtil.prepareStatement(conn, update);
	}

	@Override
	public PreparedStatement psForFind(final Connection conn, final Query query) throws SQLException {
		return psForPage(conn, query);
	}

	@Override
	public PreparedStatement psForPage(final Connection conn, final Query query) throws SQLException {
		Assert.notNull(query, "query must be not null !");
		if (StrUtil.hasBlank(query.getTableNames())) {
			throw new DbRuntimeException("Table name must be not empty !");
		}

		final SqlBuilder find = SqlBuilder.of(quoteWrapper).query(query);
		return psForPage(conn, find, query.getPage());
	}

	@Override
	public PreparedStatement psForPage(final Connection conn, SqlBuilder sqlBuilder, final Page page) throws SQLException {
		// 根据不同数据库在查询SQL语句基础上包装其分页的语句
		if(null != page){
			sqlBuilder = wrapPageSql(sqlBuilder.orderBy(page.getOrders()), page);
		}
		return StatementUtil.prepareStatement(conn, sqlBuilder);
	}

	/**
	 * 根据不同数据库在查询SQL语句基础上包装其分页的语句<br>
	 * 各自数据库通过重写此方法实现最小改动情况下修改分页语句
	 *
	 * @param find 标准查询语句
	 * @param page 分页对象
	 * @return 分页语句
	 * @since 3.2.3
	 */
	protected SqlBuilder wrapPageSql(final SqlBuilder find, final Page page) {
		// limit A offset B 表示：A就是你需要多少行，B就是查询的起点位置。
		return find
				.append(" limit ")
				.append(page.getPageSize())
				.append(" offset ")
				.append(page.getStartPosition());
	}

	@Override
	public String dialectName() {
		return DialectName.ANSI.name();
	}

	// ---------------------------------------------------------------------------- Protected method start
	// ---------------------------------------------------------------------------- Protected method end
}
