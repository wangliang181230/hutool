package cn.hutool.core.net;

import cn.hutool.core.net.url.URLDecoder;
import cn.hutool.core.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

public class UrlDecoderTest {
	@Test
	public void decodeForPathTest(){
		Assert.assertEquals("+", URLDecoder.decodeForPath("+", CharsetUtil.UTF_8));
	}
}
