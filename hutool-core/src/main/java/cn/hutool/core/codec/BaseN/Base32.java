package cn.hutool.core.codec.BaseN;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.text.StrUtil;

import java.nio.charset.Charset;

/**
 * Base32 - encodes and decodes RFC4648 Base32<br>
 *  (see <a href="https://datatracker.ietf.org/doc/html/rfc4648#section-6">https://datatracker.ietf.org/doc/html/rfc4648#section-6</a> )<br>
 * base32就是用32（2的5次方）个特定ASCII码来表示256个ASCII码。<br>
 * 所以，5个ASCII字符经过base32编码后会变为8个字符（公约数为40），长度增加3/5.不足8n用“=”补足。<br>
 * 根据RFC4648 Base32规范，支持两种模式：
 * <ul>
 *     <li>Base 32 Alphabet                 (ABCDEFGHIJKLMNOPQRSTUVWXYZ234567)</li>
 *     <li>"Extended Hex" Base 32 Alphabet  (0123456789ABCDEFGHIJKLMNOPQRSTUV)</li>
 * </ul>
 *
 * @author Looly
 */
public class Base32 {
	//----------------------------------------------------------------------------------------- encode

	/**
	 * 编码
	 *
	 * @param bytes 数据
	 * @return base32
	 */
	public static String encode(final byte[] bytes) {
		return Base32Codec.INSTANCE.encode(bytes);
	}

	/**
	 * base32编码
	 *
	 * @param source 被编码的base32字符串
	 * @return 被加密后的字符串
	 */
	public static String encode(final String source) {
		return encode(source, CharsetUtil.UTF_8);
	}

	/**
	 * base32编码
	 *
	 * @param source  被编码的base32字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String encode(final String source, final Charset charset) {
		return encode(StrUtil.bytes(source, charset));
	}

	/**
	 * 编码
	 *
	 * @param bytes 数据（Hex模式）
	 * @return base32
	 */
	public static String encodeHex(final byte[] bytes) {
		return Base32Codec.INSTANCE.encode(bytes, true);
	}

	/**
	 * base32编码（Hex模式）
	 *
	 * @param source 被编码的base32字符串
	 * @return 被加密后的字符串
	 */
	public static String encodeHex(final String source) {
		return encodeHex(source, CharsetUtil.UTF_8);
	}

	/**
	 * base32编码（Hex模式）
	 *
	 * @param source  被编码的base32字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String encodeHex(final String source, final Charset charset) {
		return encodeHex(StrUtil.bytes(source, charset));
	}

	//----------------------------------------------------------------------------------------- decode

	/**
	 * 解码
	 *
	 * @param base32 base32编码
	 * @return 数据
	 */
	public static byte[] decode(final String base32) {
		return Base32Codec.INSTANCE.decode(base32);
	}

	/**
	 * base32解码
	 *
	 * @param source 被解码的base32字符串
	 * @return 被加密后的字符串
	 */
	public static String decodeStr(final String source) {
		return decodeStr(source, CharsetUtil.UTF_8);
	}

	/**
	 * base32解码
	 *
	 * @param source  被解码的base32字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String decodeStr(final String source, final Charset charset) {
		return StrUtil.str(decode(source), charset);
	}

	/**
	 * 解码
	 *
	 * @param base32 base32编码
	 * @return 数据
	 */
	public static byte[] decodeHex(final String base32) {
		return Base32Codec.INSTANCE.decode(base32, true);
	}

	/**
	 * base32解码
	 *
	 * @param source 被解码的base32字符串
	 * @return 被加密后的字符串
	 */
	public static String decodeStrHex(final String source) {
		return decodeStrHex(source, CharsetUtil.UTF_8);
	}

	/**
	 * base32解码
	 *
	 * @param source  被解码的base32字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String decodeStrHex(final String source, final Charset charset) {
		return StrUtil.str(decodeHex(source), charset);
	}
}
