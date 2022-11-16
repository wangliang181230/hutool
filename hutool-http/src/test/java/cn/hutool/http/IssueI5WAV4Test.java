package cn.hutool.http;

import cn.hutool.http.client.engine.jdk.HttpRequest;
import cn.hutool.json.JSONUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class IssueI5WAV4Test {

	@Test
	@Ignore
	public void getTest(){
		//测试代码
		final Map<String, Object> map = new HashMap<>();
		map.put("taskID", 370);
		map.put("flightID", 2879);


		final String body = HttpRequest.get("http://localhost:8884/api/test/testHttpUtilGetWithBody").body(JSONUtil.toJsonStr(map)).execute().bodyStr();
		System.out.println("使用hutool返回结果:" + body);
	}
}
