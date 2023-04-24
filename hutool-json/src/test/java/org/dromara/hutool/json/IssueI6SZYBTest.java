package org.dromara.hutool.json;

import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.reflect.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;

public class IssueI6SZYBTest {

	@SuppressWarnings("unchecked")
	@Test
	void mutableEntryTest() {
		final MutableEntry<String, String> entry = MutableEntry.of("a", "b");
		final String jsonStr = JSONUtil.toJsonStr(entry);
		final MutableEntry<String, String> entry2 = JSONUtil.toBean(jsonStr, MutableEntry.class);

		Assertions.assertEquals(entry, entry2);
	}

	@Test
	void mutableEntryTest2() {
		final MutableEntry<Integer, Integer> entry = MutableEntry.of(1, 2);
		final String jsonStr = JSONUtil.toJsonStr(entry);
		final MutableEntry<Integer, Integer> entry2 = JSONUtil.toBean(jsonStr,
			new TypeReference<MutableEntry<Integer, Integer>>() {});

		Assertions.assertEquals(entry, entry2);
	}

	@SuppressWarnings("unchecked")
	@Test
	void simpleEntryTest() {
		final AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("a", "b");
		final String jsonStr = JSONUtil.toJsonStr(entry);
		final AbstractMap.SimpleEntry<String, String> entry2 = JSONUtil.toBean(jsonStr, AbstractMap.SimpleEntry.class);

		Assertions.assertEquals(entry, entry2);
	}

	@SuppressWarnings("unchecked")
	@Test
	void simpleEntryTest2() {
		final AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("a", "b");
		final String jsonStr = JSONUtil.toJsonStr(entry);
		final MutableEntry<String, String> entry2 = JSONUtil.toBean(jsonStr, MutableEntry.class);

		Assertions.assertEquals(entry, entry2);
	}
}
