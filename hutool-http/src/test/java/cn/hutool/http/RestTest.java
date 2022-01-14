package cn.hutool.http;

import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Rest类型请求单元测试
 *
 * @author looly
 *
 */
public class RestTest {

	@Test
	public void contentTypeTest() {
		HttpRequest request = HttpRequest.post("http://localhost:8090/rest/restTest/")//
				.body(JSONUtil.createObj()
						.set("aaa", "aaaValue")
						.set("键2", "值2").toString());
		Assert.assertEquals("application/json;charset=UTF-8", request.header("Content-Type"));
	}

	@Test
	@Disabled
	public void postTest() {
		HttpRequest request = HttpRequest.post("http://localhost:8090/rest/restTest/")//
				.body(JSONUtil.createObj()
						.set("aaa", "aaaValue")
						.set("键2", "值2").toString());
		Console.log(request.execute().body());
	}

	@Test
	@Disabled
	public void postTest2() {
		String result = HttpUtil.post("http://localhost:8090/rest/restTest/", JSONUtil.createObj()//
				.set("aaa", "aaaValue")
				.set("键2", "值2").toString());
		Console.log(result);
	}

	@Test
	@Disabled
	public void getWithBodyTest() {
		HttpRequest request = HttpRequest.get("http://localhost:8888/restTest")//
				.header(Header.CONTENT_TYPE, "application/json")
				.body(JSONUtil.createObj()
						.set("aaa", "aaaValue")
						.set("键2", "值2").toString());
		Console.log(request.execute().body());
	}

	@Test
	@Disabled
	public void getWithBodyTest2() {
		HttpRequest request = HttpRequest.get("https://ad.oceanengine.com/open_api/2/advertiser/info/")//
				// Charles代理
				.setHttpProxy("localhost", 8888)
				.header("Access-Token","")
				.body(JSONUtil.createObj()
						.set("advertiser_ids", new Long[] {1690657248243790L})
						.set("fields", new String[] {"id", "name", "status"}).toString());
		Console.log(request.execute().body());
	}
}
