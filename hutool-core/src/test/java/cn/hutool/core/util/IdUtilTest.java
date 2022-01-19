package cn.hutool.core.util;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * {@link IdUtil} 单元测试
 *
 * @author looly
 *
 */
public class IdUtilTest {

	@Test
	public void randomUUIDTest() {
		String simpleUUID = IdUtil.simpleUUID();
		Assert.assertEquals(32, simpleUUID.length());

		String randomUUID = IdUtil.randomUUID();
		Assert.assertEquals(36, randomUUID.length());
	}

	@Test
	public void fastUUIDTest() {
		String simpleUUID = IdUtil.fastSimpleUUID();
		Assert.assertEquals(32, simpleUUID.length());

		String randomUUID = IdUtil.fastUUID();
		Assert.assertEquals(36, randomUUID.length());
	}

	/**
	 * UUID的性能测试
	 */
	@Test
	@Disabled
	public void benchTest() {
		TimeInterval timer = DateUtil.timer();
		for (int i = 0; i < 1000000; i++) {
			IdUtil.simpleUUID();
		}
		Console.log(timer.interval());

		timer.restart();
		for (int i = 0; i < 1000000; i++) {
			//noinspection ResultOfMethodCallIgnored
			UUID.randomUUID().toString().replace("-", "");
		}
		Console.log(timer.interval());
	}

	@Test
	public void objectIdTest() {
		String id = IdUtil.objectId();
		Assert.assertEquals(24, id.length());
	}

	@Test
	public void getSnowflakeTest() {
		Snowflake snowflake = IdUtil.getSnowflake(1, 1);
		long id = snowflake.nextId();
		Assert.assertTrue(id > 0);
	}

	@Test
	@Disabled
	public void snowflakeBenchTest() {
		final Set<Long> set = new ConcurrentHashSet<>();
		final Snowflake snowflake = IdUtil.getSnowflake(1, 1);

		//线程数
		int threadCount = 100;
		//每个线程生成的ID数
		final int idCountPerThread = 10000;
		final CountDownLatch latch = new CountDownLatch(threadCount);
		for(int i =0; i < threadCount; i++) {
			ThreadUtil.execute(() -> {
				for(int i1 = 0; i1 < idCountPerThread; i1++) {
					long id = snowflake.nextId();
					set.add(id);
//						Console.log("Add new id: {}", id);
				}
				latch.countDown();
			});
		}

		//等待全部线程结束
		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new UtilException(e);
		}
		Assert.assertEquals(threadCount * idCountPerThread, set.size());
	}

	@Test
	@Disabled
	public void snowflakeBenchTest2() {
		final Set<Long> set = new ConcurrentHashSet<>();

		//线程数
		int threadCount = 100;
		//每个线程生成的ID数
		final int idCountPerThread = 10000;
		final CountDownLatch latch = new CountDownLatch(threadCount);
		for(int i =0; i < threadCount; i++) {
			ThreadUtil.execute(() -> {
				for(int i1 = 0; i1 < idCountPerThread; i1++) {
					long id = IdUtil.getSnowflake(1, 1).nextId();
					set.add(id);
//						Console.log("Add new id: {}", id);
				}
				latch.countDown();
			});
		}

		//等待全部线程结束
		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new UtilException(e);
		}
		Assert.assertEquals(threadCount * idCountPerThread, set.size());
	}

	@Test
	public void getDataCenterIdTest(){
		//final long dataCenterId = IdUtil.getDataCenterId(Long.MAX_VALUE);
		//Assert.assertTrue(dataCenterId > 1);

		long dataCenterId = 1L;
		InetAddress host = NetUtil.getLocalhost();
		final byte[] mac = NetUtil.getHardwareAddress(host);
		if (null != mac) {
			dataCenterId = ((0x000000FF & (long) mac[mac.length - 2])
					| (0x0000FF00 & (((long) mac[mac.length - 1]) << 8))) >> 6;
			dataCenterId = dataCenterId % ((Long.MAX_VALUE) + 1);
		}

		System.out.println("dataCenterId = " + dataCenterId + ", host = " + host + ", mac = " + toString(mac));

		Assert.assertTrue("校验 `dataCenterId > 1` 不通过：dataCenterId = " + dataCenterId + ", host = " + host + ", mac = " + toString(mac), dataCenterId > 1);
	}

	@Test
	public void getDataCenterIdTest_additional(){
		//final long dataCenterId = IdUtil.getDataCenterId(Long.MAX_VALUE);
		//Assert.assertTrue(dataCenterId > 1);

		long dataCenterId = 1L;
		InetAddress host = NetUtil.getLocalhost();
		final byte[] mac = NetUtil.getHardwareAddress(host);
		if (null != mac) {
			dataCenterId = ((0x000000FF & (long) mac[mac.length - 2])
					| (0x0000FF00 & (((long) mac[mac.length - 1]) << 8))) >> 6;
			dataCenterId = dataCenterId % ((Long.MAX_VALUE - 1) + 1);
		}

		System.out.println("dataCenterId = " + dataCenterId + ", host = " + host + ", mac = " + toString(mac));

		Assert.assertTrue("校验 `dataCenterId > 1` 不通过：dataCenterId = " + dataCenterId + ", host = " + host + ", mac = " + toString(mac), dataCenterId > 1);
	}


	private String toString(byte[] mac) {
		if (mac == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();
		for (byte b : mac) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(b);
		}
		return "[" + sb + "]";
	}
}
