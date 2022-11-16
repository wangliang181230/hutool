package cn.hutool.http.client.engine.jdk;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.meta.Header;
import cn.hutool.http.client.HeaderOperation;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * http基类，提供请求和响应共用的属性和方法。
 *
 * @param <T> 子类类型，方便链式编程
 * @author Looly
 */
@SuppressWarnings("unchecked")
public abstract class HttpBase<T extends HttpBase<T>> implements HeaderOperation<T> {

	/**
	 * 默认的请求编码、URL的encode、decode编码
	 */
	protected static final Charset DEFAULT_CHARSET = CharsetUtil.UTF_8;

	/**
	 * HTTP/1.0
	 */
	public static final String HTTP_1_0 = "HTTP/1.0";
	/**
	 * HTTP/1.1
	 */
	public static final String HTTP_1_1 = "HTTP/1.1";

	/**
	 * 存储头信息
	 */
	protected Map<String, List<String>> headers = new HashMap<>();
	/**
	 * 编码
	 */
	protected Charset charset = DEFAULT_CHARSET;
	/**
	 * http版本
	 */
	protected String httpVersion = HTTP_1_1;
	/**
	 * 存储主体
	 */
	protected byte[] bodyBytes;

	// ---------------------------------------------------------------- Headers start

	/**
	 * 根据name获取头信息列表
	 *
	 * @param name Header名
	 * @return Header值
	 * @since 3.1.1
	 */
	public List<String> headerList(final String name) {
		if (StrUtil.isBlank(name)) {
			return null;
		}

		final CaseInsensitiveMap<String, List<String>> headersIgnoreCase = new CaseInsensitiveMap<>(this.headers);
		return headersIgnoreCase.get(name.trim());
	}

	/**
	 * 根据name获取头信息
	 *
	 * @param header Header名
	 * @return Header值
	 */
	public String header(final Header header) {
		if (null == header) {
			return null;
		}
		return header(header.toString());
	}

	/**
	 * 设置一个header<br>
	 * 如果覆盖模式，则替换之前的值，否则加入到值列表中
	 *
	 * @param name       Header名
	 * @param value      Header值
	 * @param isOverride 是否覆盖已有值
	 * @return T 本身
	 */
	@Override
	public T header(final String name, final String value, final boolean isOverride) {
		if (null != name && null != value) {
			final List<String> values = headers.get(name.trim());
			if (isOverride || CollUtil.isEmpty(values)) {
				final ArrayList<String> valueList = new ArrayList<>();
				valueList.add(value);
				headers.put(name.trim(), valueList);
			} else {
				values.add(value.trim());
			}
		}
		return (T) this;
	}

	/**
	 * 设置请求头
	 *
	 * @param headers    请求头
	 * @param isOverride 是否覆盖已有头信息
	 * @return this
	 * @since 4.6.3
	 */
	public T headerMap(final Map<String, String> headers, final boolean isOverride) {
		if (MapUtil.isEmpty(headers)) {
			return (T) this;
		}

		for (final Entry<String, String> entry : headers.entrySet()) {
			this.header(entry.getKey(), StrUtil.emptyIfNull(entry.getValue()), isOverride);
		}
		return (T) this;
	}

	/**
	 * 设置请求头<br>
	 * 不覆盖原有请求头
	 *
	 * @param headers 请求头
	 * @return this
	 */
	public T header(final Map<String, List<String>> headers) {
		return header(headers, false);
	}

	/**
	 * 设置请求头
	 *
	 * @param headers    请求头
	 * @param isOverride 是否覆盖已有头信息
	 * @return this
	 * @since 4.0.8
	 */
	public T header(final Map<String, List<String>> headers, final boolean isOverride) {
		if (MapUtil.isEmpty(headers)) {
			return (T) this;
		}

		String name;
		for (final Entry<String, List<String>> entry : headers.entrySet()) {
			name = entry.getKey();
			for (final String value : entry.getValue()) {
				this.header(name, StrUtil.emptyIfNull(value), isOverride);
			}
		}
		return (T) this;
	}

	/**
	 * 新增请求头<br>
	 * 不覆盖原有请求头
	 *
	 * @param headers 请求头
	 * @return this
	 * @since 4.0.3
	 */
	public T addHeaders(final Map<String, String> headers) {
		if (MapUtil.isEmpty(headers)) {
			return (T) this;
		}

		for (final Entry<String, String> entry : headers.entrySet()) {
			this.header(entry.getKey(), StrUtil.emptyIfNull(entry.getValue()), false);
		}
		return (T) this;
	}

	/**
	 * 移除一个头信息
	 *
	 * @param name Header名
	 * @return this
	 */
	public T removeHeader(final String name) {
		if (name != null) {
			headers.remove(name.trim());
		}
		return (T) this;
	}

	/**
	 * 移除一个头信息
	 *
	 * @param name Header名
	 * @return this
	 */
	public T removeHeader(final Header name) {
		return removeHeader(name.toString());
	}

	/**
	 * 获取headers
	 *
	 * @return Headers Map
	 */
	@Override
	public Map<String, List<String>> headers() {
		return Collections.unmodifiableMap(headers);
	}

	/**
	 * 清除所有头信息，包括全局头信息
	 *
	 * @return this
	 * @since 5.7.13
	 */
	public T clearHeaders() {
		this.headers.clear();
		return (T) this;
	}
	// ---------------------------------------------------------------- Headers end

	/**
	 * 返回http版本
	 *
	 * @return String
	 */
	public String httpVersion() {
		return httpVersion;
	}

	/**
	 * 设置http版本，此方法不会影响到实际请求的HTTP版本，只用于帮助判断是否connect:Keep-Alive
	 *
	 * @param httpVersion Http版本，{@link HttpBase#HTTP_1_0}，{@link HttpBase#HTTP_1_1}
	 * @return this
	 */
	public T httpVersion(final String httpVersion) {
		this.httpVersion = httpVersion;
		return (T) this;
	}

	/**
	 * 返回字符集
	 *
	 * @return 字符集
	 */
	public String charsetName() {
		return charset.name();
	}

	/**
	 * 返回字符集
	 *
	 * @return 字符集
	 */
	public Charset charset() {
		return this.charset;
	}

	/**
	 * 设置字符集
	 *
	 * @param charset 字符集
	 * @return T 自己
	 * @see CharsetUtil
	 */
	public T charset(final String charset) {
		if (StrUtil.isNotBlank(charset)) {
			charset(Charset.forName(charset));
		}
		return (T) this;
	}

	/**
	 * 设置字符集
	 *
	 * @param charset 字符集
	 * @return T 自己
	 * @see CharsetUtil
	 */
	public T charset(final Charset charset) {
		if (null != charset) {
			this.charset = charset;
		}
		return (T) this;
	}

	/**
	 * 获取bodyBytes存储字节码
	 *
	 * @return byte[]
	 */
	public byte[] bodyBytes() {
		return this.bodyBytes;
	}

	@Override
	public String toString() {
		final StringBuilder sb = StrUtil.builder();
		sb.append("Request Headers: ").append(StrUtil.CRLF);
		for (final Entry<String, List<String>> entry : this.headers.entrySet()) {
			sb.append("    ")
					.append(entry.getKey()).append(":").append(CollUtil.join(entry.getValue(), ","))
					.append(StrUtil.CRLF);
		}

		sb.append("Request Body: ").append(StrUtil.CRLF);
		sb.append("    ").append(StrUtil.str(this.bodyBytes, this.charset)).append(StrUtil.CRLF);

		return sb.toString();
	}
}
