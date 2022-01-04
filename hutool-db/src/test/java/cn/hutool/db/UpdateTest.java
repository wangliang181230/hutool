package cn.hutool.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

@Disabled
public class UpdateTest {

	static Db db;

	@BeforeAll
	public static void init() {
		db = Db.use("test");
	}

	/**
	 * 对更新做单元测试
	 *
	 * @throws SQLException SQL异常
	 */
	@Test
	public void updateTest() throws SQLException {

		// 改
		int update = db.update(Entity.create("user").set("age", 88), Entity.create().set("name", "unitTestUser"));
		Assertions.assertTrue(update > 0);
		Entity result2 = db.get("user", "name", "unitTestUser");
		Assertions.assertSame(88, result2.getInt("age"));
	}
}
