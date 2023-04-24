package org.dromara.hutool.swing;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.swing.clipboard.ClipboardUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ClipboardMonitorTest {

	@Test
	@Disabled
	public void monitorTest() {
		// 第一个监听
		ClipboardUtil.listen((clipboard, contents) -> {
			final Object object = ClipboardUtil.getStr(contents);
			Console.log("1# {}", object);
			return contents;
		}, false);

		// 第二个监听
		ClipboardUtil.listen((clipboard, contents) -> {
			final Object object = ClipboardUtil.getStr(contents);
			Console.log("2# {}", object);
			return contents;
		});

	}
}
