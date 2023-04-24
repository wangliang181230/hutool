package org.dromara.hutool.core.io.file;

import org.dromara.hutool.core.array.ArrayUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class PathUtilTest {

	@Test
	@Disabled
	public void copyFileTest(){
		PathUtil.copy(
				Paths.get("d:/test/1595232240113.jpg"),
				Paths.get("d:/test/1595232240113_copy.jpg"),
				StandardCopyOption.COPY_ATTRIBUTES,
				StandardCopyOption.REPLACE_EXISTING
				);
	}

	@Test
	@Disabled
	public void copyTest(){
		PathUtil.copy(
				Paths.get("d:/Red2_LYY"),
				Paths.get("d:/test/aaa/aaa.txt")
		);
	}

	@Test
	@Disabled
	public void copyContentTest(){
		PathUtil.copyContent(
				Paths.get("d:/Red2_LYY"),
				Paths.get("d:/test/aaa/")
		);
	}

	@Test
	@Disabled
	public void moveTest(){
		PathUtil.move(Paths.get("d:/lombok.jar"), Paths.get("d:/test/"), false);
	}

	@Test
	@Disabled
	public void moveDirTest(){
		PathUtil.move(Paths.get("c:\\aaa"), Paths.get("d:/test/looly"), false);
	}

	@Test
	@Disabled
	public void delDirTest(){
		PathUtil.del(Paths.get("d:/test/looly"));
	}

	@Test
	@Disabled
	public void getMimeTypeTest(){
		String mimeType = PathUtil.getMimeType(Paths.get("d:/test/test.jpg"));
		Assertions.assertEquals("image/jpeg", mimeType);

		mimeType = PathUtil.getMimeType(Paths.get("d:/test/test.mov"));
		Assertions.assertEquals("video/quicktime", mimeType);
	}

	@Test
	public void getMimeOfRarTest(){
		final String contentType = FileUtil.getMimeType("a001.rar");
		Assertions.assertTrue(
			ArrayUtil.contains(
				new String[]{
					"application/x-rar-compressed",
					// JDK9+修改为此
					"application/vnd.rar"},
				contentType));
	}

	@Test
	public void getMimeOf7zTest(){
		final String contentType = FileUtil.getMimeType("a001.7z");
		Assertions.assertEquals("application/x-7z-compressed", contentType);
	}

	/**
	 * issue#2893 target不存在空导致异常
	 */
	@Test
	@Disabled
	public void moveTest2(){
		PathUtil.move(Paths.get("D:\\project\\test1.txt"), Paths.get("D:\\project\\test2.txt"), false);
	}
}
