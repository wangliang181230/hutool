package cn.hutool.core.lang.tree;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TreeBuilderTest {

	@Test
	public void checkIsBuiltTest(){
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			final TreeBuilder<Integer> of = TreeBuilder.of(0);
			of.build();
			of.append(new ArrayList<>());
		});
	}
}
