package cn.hutool.core.compress;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.FileResource;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ZipWriterTest {

	@Test
	@Disabled
	public void zipDirTest() {
		ZipUtil.zip(new File("d:/test"));
	}

	@Test
	@Disabled
	public void addTest(){
		final ZipWriter writer = ZipWriter.of(FileUtil.file("d:/test/test.zip"), CharsetUtil.CHARSET_UTF_8);
		writer.add(new FileResource("d:/test/qr_c.png"));
		writer.close();
	}
}
