package cn.hutool.core.date;

import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.date.format.parser.CSTDateParser;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * 日期格式化类，提供常用的日期格式化对象<br>
 * 参考：<a href="https://www.ietf.org/rfc/rfc3339.txt">rfc3339</a>
 *
 * <p>所有的jdk日期格式模式字符串
 * <a href="https://docs.oracle.com/en/java/javase/18/docs/api/java.base/java/time/format/DateTimeFormatter.html">
 * <i>jdk date format pattern （Pattern Letters and Symbols） 日期格式模式字符串</i>
 * </a>
 * </p>
 *
 * <p>工具类，提供格式化字符串很多，但是对于具体什么含义，不够清晰，这里进行说明：</p>
 * <b>常见日期格式模式字符串：</b>
 * <ul>
 *    <li>yyyy-MM-dd                   示例：2022-08-05</li>
 *    <li>yyyy年MM月dd日                示例：2022年08月05日</li>
 *    <li>yyyy-MM-dd HH:mm:ss          示例：2022-08-05 12:59:59</li>
 *    <li>yyyy-MM-dd HH:mm:ss.SSS      示例：2022-08-05 12:59:59.559</li>
 *    <li>yyyy-MM-dd HH:mm:ss.SSSZ     示例：2022-08-05 12:59:59.559+0800【东八区中国时区】、2022-08-05 04:59:59.559+0000【冰岛0时区】, 年月日 时分秒 毫秒 时区</li>
 *    <li>yyyy-MM-dd HH:mm:ss.SSSz     示例：2022-08-05 12:59:59.559UTC【世界标准时间=0时区】、2022-08-05T12:59:59.599GMT【冰岛0时区】、2022-08-05T12:59:59.599CST【东八区中国时区】、2022-08-23T03:45:00EDT【美国东北纽约时间，-0400】 ,年月日 时分秒 毫秒 时区</li>
 *    <li>yyyy-MM-dd'T'HH:mm:ss.SSS'Z' 示例：2022-08-05T12:59:59.559Z, 其中：''单引号表示转义字符，T:分隔符，Z:一般值UTC,0时区的时间含义</li>
 *    <li>yyyy-MM-dd'T'HH:mm:ss.SSSZ   示例：2022-08-05T11:59:59.559+0800, 其中：Z,表示时区</li>
 *    <li>yyyy-MM-dd'T'HH:mm:ss.SSSX   示例：2022-08-05T12:59:59.559+08, 其中：X:两位时区，+08表示：东8区，中国时区</li>
 *    <li>yyyy-MM-dd'T'HH:mm:ss.SSSXX  示例：2022-08-05T12:59:59.559+0800, 其中：XX:四位时区</li>
 *    <li>yyyy-MM-dd'T'HH:mm:ss.SSSXXX 示例：2022-08-05T12:59:59.559+08:00, 其中：XX:五位时区</li>
 *    <li>yyyy-MM-dd'T'HH:mm:ss        示例：2022-08-05T12:59:59+08</li>
 *    <li>yyyy-MM-dd'T'HH:mm:ssXXX     示例：2022-08-05T12:59:59+08:00</li>
 *    <li>yyyy-MM-dd'T'HH:mm:ssZ       示例：2022-08-05T12:59:59+0800</li>
 *    <li>yyyy-MM-dd'T'HH:mm:ss'Z'     示例：2022-08-05T12:59:59Z</li>
 *    <li>EEE MMM dd HH:mm:ss z yyyy   示例：周五 8月 05 12:59:00 UTC+08:00 2022</li>
 *    <li>EEE MMM dd HH:mm:ss zzz yyyy 示例：周五 8月 05 12:59:00 UTC+08:00 2022,其中z表示UTC时区，但：1~3个z没有任何区别</li>
 *    <li>EEE, dd MMM yyyy HH:mm:ss z  示例：周五, 05 8月 2022 12:59:59 UTC+08:00</li>
 * </ul>
 * <p>
 * 系统提供的，请查看，有大量定义好的格式化对象，可以直接使用，如：
 * {@link DateTimeFormatter#ISO_DATE}
 * {@link DateTimeFormatter#ISO_DATE_TIME}
 * 查看更多，请参阅上述官方文档
 * </p>
 *
 * <p>
 * 其中：CST格式，形如："Mon Aug 15 14:23:15 CST 2022",上面未包含
 * 参见：{@link CSTDateParser#parse(String)}、{@link DateUtil#parse(String, String...)}、{@link Date#toString()}进行处理
 *</p>
 *
 * <p>
 * 特殊说明：UTC时间，世界标准时间，0时区的时间，伦敦时间，可以直接加Z表示不加空格，
 * 如：“09:30 UTC”表示为“09:30Z”或“T0930Z”，其中：Z 是 +00:00 的缩写，意思是 UTC(零时分秒的偏移量).
 * </p>
 * <ul>
 *     <li>yyyy-MM-dd'T'HH:mm:ssZ</li>
 *     <li>2022-08-23T15:20:46UTC</li>
 *     <li>2022-08-23T15:20:46 UTC</li>
 *     <li>2022-08-23T15:20:46+0000</li>
 *     <li>2022-08-23T15:20:46 +0000</li>
 *     <li>2022-08-23T15:20:46Z</li>
 * </ul>
 *
 * 其他格式见：<a href="https://ijmacd.github.io/rfc3339-iso8601/">https://ijmacd.github.io/rfc3339-iso8601/</a>
 *
 * @author Looly
 */
public class DatePattern {

	/**
	 * 标准日期时间正则，每个字段支持单个数字或2个数字，包括：
	 * <pre>
	 *     yyyy-MM-dd HH:mm:ss.SSSSSS
	 *     yyyy-MM-dd HH:mm:ss.SSS
	 *     yyyy-MM-dd HH:mm:ss
	 *     yyyy-MM-dd HH:mm
	 *     yyyy-MM-dd
	 * </pre>
	 *
	 * @since 5.3.6
	 */
	public static final Pattern REGEX_NORM = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}(\\s\\d{1,2}:\\d{1,2}(:\\d{1,2})?)?(.\\d{1,6})?");

	//-------------------------------------------------------------------------------------------------------------------------------- Normal
	/**
	 * 年格式：yyyy
	 */
	public static final String NORM_YEAR_PATTERN = "yyyy";
	/**
	 * 年月格式：yyyy-MM
	 */
	public static final String NORM_MONTH_PATTERN = "yyyy-MM";
	/**
	 * 年月格式 {@link FastDateFormat}：yyyy-MM
	 */
	public static final FastDateFormat NORM_MONTH_FORMAT = FastDateFormat.getInstance(NORM_MONTH_PATTERN);
	/**
	 * 年月格式 {@link FastDateFormat}：yyyy-MM
	 */
	public static final DateTimeFormatter NORM_MONTH_FORMATTER = createFormatter(NORM_MONTH_PATTERN);

	/**
	 * 简单年月格式：yyyyMM
	 */
	public static final String SIMPLE_MONTH_PATTERN = "yyyyMM";
	/**
	 * 简单年月格式 {@link FastDateFormat}：yyyyMM
	 */
	public static final FastDateFormat SIMPLE_MONTH_FORMAT = FastDateFormat.getInstance(SIMPLE_MONTH_PATTERN);
	/**
	 * 简单年月格式 {@link FastDateFormat}：yyyyMM
	 */
	public static final DateTimeFormatter SIMPLE_MONTH_FORMATTER = createFormatter(SIMPLE_MONTH_PATTERN);

	/**
	 * 标准日期格式：yyyy-MM-dd
	 */
	public static final String NORM_DATE_PATTERN = "yyyy-MM-dd";
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyy-MM-dd
	 */
	public static final FastDateFormat NORM_DATE_FORMAT = FastDateFormat.getInstance(NORM_DATE_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyy-MM-dd
	 */
	public static final DateTimeFormatter NORM_DATE_FORMATTER = createFormatter(NORM_DATE_PATTERN);

	/**
	 * 标准时间格式：HH:mm:ss
	 */
	public static final String NORM_TIME_PATTERN = "HH:mm:ss";
	/**
	 * 标准时间格式 {@link FastDateFormat}：HH:mm:ss
	 */
	public static final FastDateFormat NORM_TIME_FORMAT = FastDateFormat.getInstance(NORM_TIME_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：HH:mm:ss
	 */
	public static final DateTimeFormatter NORM_TIME_FORMATTER = createFormatter(NORM_TIME_PATTERN);

	/**
	 * 标准日期时间格式，精确到分：yyyy-MM-dd HH:mm
	 */
	public static final String NORM_DATETIME_MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
	/**
	 * 标准日期时间格式，精确到分 {@link FastDateFormat}：yyyy-MM-dd HH:mm
	 */
	public static final FastDateFormat NORM_DATETIME_MINUTE_FORMAT = FastDateFormat.getInstance(NORM_DATETIME_MINUTE_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyy-MM-dd HH:mm
	 */
	public static final DateTimeFormatter NORM_DATETIME_MINUTE_FORMATTER = createFormatter(NORM_DATETIME_MINUTE_PATTERN);

	/**
	 * 标准日期时间格式，精确到秒：yyyy-MM-dd HH:mm:ss
	 */
	public static final String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 标准日期时间格式，精确到秒 {@link FastDateFormat}：yyyy-MM-dd HH:mm:ss
	 */
	public static final FastDateFormat NORM_DATETIME_FORMAT = FastDateFormat.getInstance(NORM_DATETIME_PATTERN);
	/**
	 * 标准日期时间格式，精确到秒 {@link FastDateFormat}：yyyy-MM-dd HH:mm:ss
	 */
	public static final DateTimeFormatter NORM_DATETIME_FORMATTER = createFormatter(NORM_DATETIME_PATTERN);

	/**
	 * 标准日期时间格式，精确到毫秒：yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static final String NORM_DATETIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
	/**
	 * 标准日期时间格式，精确到毫秒 {@link FastDateFormat}：yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static final FastDateFormat NORM_DATETIME_MS_FORMAT = FastDateFormat.getInstance(NORM_DATETIME_MS_PATTERN);
	/**
	 * 标准日期时间格式，精确到毫秒 {@link FastDateFormat}：yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static final DateTimeFormatter NORM_DATETIME_MS_FORMATTER = createFormatter(NORM_DATETIME_MS_PATTERN);

	/**
	 * ISO8601日期时间格式，精确到毫秒：yyyy-MM-dd HH:mm:ss,SSS
	 */
	public static final String ISO8601_PATTERN = "yyyy-MM-dd HH:mm:ss,SSS";
	/**
	 * ISO8601日期时间格式，精确到毫秒 {@link FastDateFormat}：yyyy-MM-dd HH:mm:ss,SSS
	 */
	public static final FastDateFormat ISO8601_FORMAT = FastDateFormat.getInstance(ISO8601_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyy-MM-dd HH:mm:ss,SSS
	 */
	public static final DateTimeFormatter ISO8601_FORMATTER = createFormatter(ISO8601_PATTERN);

	/**
	 * 标准日期格式：yyyy年MM月dd日
	 */
	public static final String CHINESE_DATE_PATTERN = "yyyy年MM月dd日";
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyy年MM月dd日
	 */
	public static final FastDateFormat CHINESE_DATE_FORMAT = FastDateFormat.getInstance(CHINESE_DATE_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyy年MM月dd日
	 */
	public static final DateTimeFormatter CHINESE_DATE_FORMATTER = createFormatter(CHINESE_DATE_PATTERN);

	/**
	 * 标准日期格式：yyyy年MM月dd日 HH时mm分ss秒
	 */
	public static final String CHINESE_DATE_TIME_PATTERN = "yyyy年MM月dd日HH时mm分ss秒";
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyy年MM月dd日HH时mm分ss秒
	 */
	public static final FastDateFormat CHINESE_DATE_TIME_FORMAT = FastDateFormat.getInstance(CHINESE_DATE_TIME_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyy年MM月dd日HH时mm分ss秒
	 */
	public static final DateTimeFormatter CHINESE_DATE_TIME_FORMATTER = createFormatter(CHINESE_DATE_TIME_PATTERN);

	//-------------------------------------------------------------------------------------------------------------------------------- Pure
	/**
	 * 标准日期格式：yyyyMMdd
	 */
	public static final String PURE_DATE_PATTERN = "yyyyMMdd";
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyyMMdd
	 */
	public static final FastDateFormat PURE_DATE_FORMAT = FastDateFormat.getInstance(PURE_DATE_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyyMMdd
	 */
	public static final DateTimeFormatter PURE_DATE_FORMATTER = createFormatter(PURE_DATE_PATTERN);

	/**
	 * 标准日期格式：HHmmss
	 */
	public static final String PURE_TIME_PATTERN = "HHmmss";
	/**
	 * 标准日期格式 {@link FastDateFormat}：HHmmss
	 */
	public static final FastDateFormat PURE_TIME_FORMAT = FastDateFormat.getInstance(PURE_TIME_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：HHmmss
	 */
	public static final DateTimeFormatter PURE_TIME_FORMATTER = createFormatter(PURE_TIME_PATTERN);

	/**
	 * 标准日期格式：yyyyMMddHHmmss
	 */
	public static final String PURE_DATETIME_PATTERN = "yyyyMMddHHmmss";
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyyMMddHHmmss
	 */
	public static final FastDateFormat PURE_DATETIME_FORMAT = FastDateFormat.getInstance(PURE_DATETIME_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyyMMddHHmmss
	 */
	public static final DateTimeFormatter PURE_DATETIME_FORMATTER = createFormatter(PURE_DATETIME_PATTERN);

	/**
	 * 标准日期格式：yyyyMMddHHmmssSSS
	 */
	public static final String PURE_DATETIME_MS_PATTERN = "yyyyMMddHHmmssSSS";
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyyMMddHHmmssSSS
	 */
	public static final FastDateFormat PURE_DATETIME_MS_FORMAT = FastDateFormat.getInstance(PURE_DATETIME_MS_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyyMMddHHmmssSSS
	 */
	public static final DateTimeFormatter PURE_DATETIME_MS_FORMATTER = createFormatter(PURE_DATETIME_MS_PATTERN);

	//-------------------------------------------------------------------------------------------------------------------------------- Others
	/**
	 * HTTP头中日期时间格式：EEE, dd MMM yyyy HH:mm:ss z
	 */
	public static final String HTTP_DATETIME_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";
	/**
	 * HTTP头中日期时间格式 {@link FastDateFormat}：EEE, dd MMM yyyy HH:mm:ss z
	 */
	public static final FastDateFormat HTTP_DATETIME_FORMAT = FastDateFormat.getInstance(HTTP_DATETIME_PATTERN, TimeZone.getTimeZone("GMT"), Locale.US);

	/**
	 * JDK中日期时间格式：EEE MMM dd HH:mm:ss zzz yyyy
	 */
	public static final String JDK_DATETIME_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy";
	/**
	 * JDK中日期时间格式 {@link FastDateFormat}：EEE MMM dd HH:mm:ss zzz yyyy
	 */
	public static final FastDateFormat JDK_DATETIME_FORMAT = FastDateFormat.getInstance(JDK_DATETIME_PATTERN, Locale.US);

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ss
	 */
	public static final String UTC_SIMPLE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
	/**
	 * UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ss
	 */
	public static final FastDateFormat UTC_SIMPLE_FORMAT = FastDateFormat.getInstance(UTC_SIMPLE_PATTERN, TimeZone.getTimeZone("UTC"));

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ss.SSS
	 */
	public static final String UTC_SIMPLE_MS_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	/**
	 * UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ss.SSS
	 */
	public static final FastDateFormat UTC_SIMPLE_MS_FORMAT = FastDateFormat.getInstance(UTC_SIMPLE_MS_PATTERN, TimeZone.getTimeZone("UTC"));

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ss'Z'
	 */
	public static final String UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	/**
	 * UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ss'Z'
	 */
	public static final FastDateFormat UTC_FORMAT = FastDateFormat.getInstance(UTC_PATTERN, TimeZone.getTimeZone("UTC"));

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ssZ
	 */
	public static final String UTC_WITH_ZONE_OFFSET_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
	/**
	 * UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ssZ
	 */
	public static final FastDateFormat UTC_WITH_ZONE_OFFSET_FORMAT = FastDateFormat.getInstance(UTC_WITH_ZONE_OFFSET_PATTERN, TimeZone.getTimeZone("UTC"));

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ssXXX
	 */
	public static final String UTC_WITH_XXX_OFFSET_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX";
	/**
	 * UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ssXXX
	 */
	public static final FastDateFormat UTC_WITH_XXX_OFFSET_FORMAT = FastDateFormat.getInstance(UTC_WITH_XXX_OFFSET_PATTERN);

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
	 */
	public static final String UTC_MS_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	/**
	 * UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
	 */
	public static final FastDateFormat UTC_MS_FORMAT = FastDateFormat.getInstance(UTC_MS_PATTERN, TimeZone.getTimeZone("UTC"));

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ss.SSSZ
	 */
	public static final String UTC_MS_WITH_ZONE_OFFSET_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	/**
	 * UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ss.SSSZ
	 */
	public static final FastDateFormat UTC_MS_WITH_ZONE_OFFSET_FORMAT = FastDateFormat.getInstance(UTC_MS_WITH_ZONE_OFFSET_PATTERN, TimeZone.getTimeZone("UTC"));

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ss.SSSXXX
	 */
	public static final String UTC_MS_WITH_XXX_OFFSET_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	/**
	 * UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ss.SSSXXX
	 */
	public static final FastDateFormat UTC_MS_WITH_XXX_OFFSET_FORMAT = FastDateFormat.getInstance(UTC_MS_WITH_XXX_OFFSET_PATTERN);

	/**
	 * 创建并为 {@link DateTimeFormatter} 赋予默认时区和位置信息，默认值为系统默认值。
	 *
	 * @param pattern 日期格式
	 * @return {@link DateTimeFormatter}
	 * @since 5.7.5
	 */
	public static DateTimeFormatter createFormatter(final String pattern) {
		return DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
				.withZone(ZoneId.systemDefault());
	}
}
