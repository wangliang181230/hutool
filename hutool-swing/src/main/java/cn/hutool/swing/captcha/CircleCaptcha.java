package cn.hutool.swing.captcha;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.swing.img.color.ColorUtil;
import cn.hutool.swing.img.GraphicsUtil;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 圆圈干扰验证码
 *
 * @author looly
 * @since 3.2.3
 *
 */
public class CircleCaptcha extends AbstractCaptcha {
	private static final long serialVersionUID = -7096627300356535494L;

	/**
	 * 构造
	 *
	 * @param width 图片宽
	 * @param height 图片高
	 */
	public CircleCaptcha(final int width, final int height) {
		this(width, height, 5);
	}

	/**
	 * 构造
	 *
	 * @param width 图片宽
	 * @param height 图片高
	 * @param codeCount 字符个数
	 */
	public CircleCaptcha(final int width, final int height, final int codeCount) {
		this(width, height, codeCount, 15);
	}

	/**
	 * 构造
	 *
	 * @param width 图片宽
	 * @param height 图片高
	 * @param codeCount 字符个数
	 * @param interfereCount 验证码干扰元素个数
	 */
	public CircleCaptcha(final int width, final int height, final int codeCount, final int interfereCount) {
		super(width, height, codeCount, interfereCount);
	}

	@Override
	public Image createImage(final String code) {
		final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g = GraphicsUtil.createGraphics(image, ObjUtil.defaultIfNull(this.background, Color.WHITE));

		// 随机画干扰圈圈
		drawInterfere(g);

		// 画字符串
		drawString(g, code);

		return image;
	}

	// ----------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 绘制字符串
	 *
	 * @param g {@link Graphics2D}画笔
	 * @param code 验证码
	 */
	private void drawString(final Graphics2D g, final String code) {
		// 指定透明度
		if (null != this.textAlpha) {
			g.setComposite(this.textAlpha);
		}
		GraphicsUtil.drawStringColourful(g, code, this.font, this.width, this.height);
	}

	/**
	 * 画随机干扰
	 *
	 * @param g {@link Graphics2D}
	 */
	private void drawInterfere(final Graphics2D g) {
		final ThreadLocalRandom random = RandomUtil.getRandom();

		for (int i = 0; i < this.interfereCount; i++) {
			g.setColor(ColorUtil.randomColor(random));
			g.drawOval(random.nextInt(width), random.nextInt(height), random.nextInt(height >> 1), random.nextInt(height >> 1));
		}
	}
	// ----------------------------------------------------------------------------------------------------- Private method end
}
