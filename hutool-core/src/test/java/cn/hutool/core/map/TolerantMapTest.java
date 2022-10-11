package cn.hutool.core.map;

import cn.hutool.core.io.SerializeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class TolerantMapTest {

	private final TolerantMap<String, String> map = TolerantMap.of(new HashMap<>(), "default");

	@Before
	public void before() {
		map.put("monday", "星期一");
		map.put("tuesday", "星期二");
	}

	@Test
	public void testSerialize() {
		final byte[] bytes = SerializeUtil.serialize(map);
		final TolerantMap<String, String> serializedMap = SerializeUtil.deserialize(bytes);
		assert serializedMap != map;
		assert map.equals(serializedMap);
	}

	@Test
	public void testClone() {
		final TolerantMap<String, String> clonedMap = ObjUtil.clone(map);
		assert clonedMap != map;
		assert map.equals(clonedMap);
	}

	@Test
	public void testGet() {
		assert "星期二".equals(map.get("tuesday"));
		assert "default".equals(map.get(RandomUtil.randomString(6)));
	}
}
