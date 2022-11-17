package cn.hutool.core.net;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class Ipv4UtilTest {

	@Test
	public void getMaskBitByMaskTest(){
		final int maskBitByMask = Ipv4Util.getMaskBitByMask("255.255.255.0");
		Assert.assertEquals(24, maskBitByMask);
	}

	@Test
	public void getMaskBitByIllegalMaskTest() {
		try {
			Ipv4Util.getMaskBitByMask("255.255.0.255");
		} catch (Exception e) {
			Assert.assertEquals(IllegalArgumentException.class, e.getClass());
		}
		throw new RuntimeException("未抛出异常");
	}

	@Test
	public void getMaskByMaskBitTest(){
		final String mask = Ipv4Util.getMaskByMaskBit(24);
		Assert.assertEquals("255.255.255.0", mask);
	}

	@Test
	public void longToIpTest() {
		final String ip = "192.168.1.255";
		final long ipLong = Ipv4Util.ipv4ToLong(ip);
		final String ipv4 = Ipv4Util.longToIpv4(ipLong);
		Assert.assertEquals(ip, ipv4);
	}

	@Test
	public void getEndIpStrTest(){
		final String ip = "192.168.1.1";
		final int maskBitByMask = Ipv4Util.getMaskBitByMask("255.255.255.0");
		final String endIpStr = Ipv4Util.getEndIpStr(ip, maskBitByMask);
		Assert.assertEquals("192.168.1.255", endIpStr);
	}

	@Test
	public void listTest(){
		final int maskBit = Ipv4Util.getMaskBitByMask("255.255.255.0");
		final List<String> list = Ipv4Util.list("192.168.100.2", maskBit, false);
		Assert.assertEquals(254, list.size());
	}

	@Test
	public void isMaskValidTest() {
		final boolean maskValid = Ipv4Util.isMaskValid("255.255.255.0");
		Assert.assertTrue("掩码合法检验", maskValid);
	}

	@Test
	public void isMaskInvalidTest() {
		Assert.assertFalse("掩码非法检验 - 255.255.0.255", Ipv4Util.isMaskValid("255.255.0.255"));
		Assert.assertFalse("掩码非法检验 - null值", Ipv4Util.isMaskValid(null));
		Assert.assertFalse("掩码非法检验 - 空字符串", Ipv4Util.isMaskValid(""));
		Assert.assertFalse("掩码非法检验 - 空白字符串", Ipv4Util.isMaskValid(" "));
	}

	@Test
	public void isMaskBitValidTest() {
		final boolean maskBitValid = Ipv4Util.isMaskBitValid(32);
		Assert.assertTrue("掩码位合法检验", maskBitValid);
	}

	@Test
	public void isMaskBitInvalidTest() {
		final boolean maskBitValid = Ipv4Util.isMaskBitValid(33);
		Assert.assertFalse("掩码位非法检验", maskBitValid);
	}

	@Test
	public void ipv4ToLongTest(){
		long l = Ipv4Util.ipv4ToLong("127.0.0.1");
		Assert.assertEquals(2130706433L, l);
		l = Ipv4Util.ipv4ToLong("114.114.114.114");
		Assert.assertEquals(1920103026L, l);
		l = Ipv4Util.ipv4ToLong("0.0.0.0");
		Assert.assertEquals(0L, l);
		l = Ipv4Util.ipv4ToLong("255.255.255.255");
		Assert.assertEquals(4294967295L, l);
	}
}
