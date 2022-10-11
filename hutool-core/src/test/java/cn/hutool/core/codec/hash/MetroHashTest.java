package cn.hutool.core.codec.hash;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.codec.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.text.StrUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * https://gitee.com/dromara/hutool/pulls/532
 */
public class MetroHashTest {

	@Test
	public void testEmpty() {
		Assert.assertEquals("31290877cceaea29", HexUtil.toHex(MetroHash.INSTANCE.hash64(StrUtil.utf8Bytes(""), 0)));
	}

	@Test
	public void metroHash64Test() {
		final byte[] str = "我是一段测试123".getBytes(CharsetUtil.UTF_8);
		final long hash64 = MetroHash.INSTANCE.hash64(str);
		Assert.assertEquals(62920234463891865L, hash64);
	}

	@Test
	public void metroHash128Test() {
		final byte[] str = "我是一段测试123".getBytes(CharsetUtil.UTF_8);
		final long[] hash128 = MetroHash.INSTANCE.hash128(str).getLongArray();
		Assert.assertEquals(4956592424592439349L, hash128[0]);
		Assert.assertEquals(6301214698325086246L, hash128[1]);
	}

	/**
	 * 数据量越大 MetroHash 优势越明显，
	 */
	@Test
	@Ignore
	public void bulkHashing64Test() {
		final String[] strArray = getRandomStringArray();
		final long startCity = System.currentTimeMillis();
		for (final String s : strArray) {
			CityHash.INSTANCE.hash64(s.getBytes());
		}
		final long endCity = System.currentTimeMillis();

		final long startMetro = System.currentTimeMillis();
		for (final String s : strArray) {
			MetroHash.INSTANCE.hash64(StrUtil.utf8Bytes(s));
		}
		final long endMetro = System.currentTimeMillis();

		System.out.println("metroHash =============" + (endMetro - startMetro));
		System.out.println("cityHash =============" + (endCity - startCity));
	}


	/**
	 * 数据量越大 MetroHash 优势越明显，
	 */
	@Test
	@Ignore
	public void bulkHashing128Test() {
		final String[] strArray = getRandomStringArray();
		final long startCity = System.currentTimeMillis();
		for (final String s : strArray) {
			CityHash.INSTANCE.hash128(s.getBytes());
		}
		final long endCity = System.currentTimeMillis();

		final long startMetro = System.currentTimeMillis();
		for (final String s : strArray) {
			MetroHash.INSTANCE.hash128(StrUtil.utf8Bytes(s));
		}
		final long endMetro = System.currentTimeMillis();

		System.out.println("metroHash =============" + (endMetro - startMetro));
		System.out.println("cityHash =============" + (endCity - startCity));
	}


	private static String[] getRandomStringArray() {
		final String[] result = new String[10000000];
		int index = 0;
		while (index < 10000000) {
			result[index++] = RandomUtil.randomString(RandomUtil.randomInt(64));
		}
		return result;
	}
}
