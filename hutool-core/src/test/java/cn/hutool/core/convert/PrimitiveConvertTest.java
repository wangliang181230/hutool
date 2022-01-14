package cn.hutool.core.convert;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class PrimitiveConvertTest {

	@Test
	public void toIntTest(){
		final int convert = Convert.convert(int.class, "123");
		Assert.assertEquals(123, convert);
	}

	@Test
	public void toIntErrorTest(){
		Assert.assertThrows(NumberFormatException.class, () -> {
			Convert.convert(int.class, "aaaa");
		});
	}
}
