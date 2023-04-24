/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.swing.captcha.generator;

import org.dromara.hutool.core.math.Calculator;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.util.RandomUtil;
import org.dromara.hutool.core.text.StrUtil;

/**
 * 数字计算验证码生成器
 *
 * @author looly
 * @since 4.1.2
 */
public class MathGenerator implements CodeGenerator {
	private static final long serialVersionUID = -5514819971774091076L;

	private static final String operators = "+-*";

	/** 参与计算数字最大长度 */
	private final int numberLength;

	/**
	 * 构造
	 */
	public MathGenerator() {
		this(2);
	}

	/**
	 * 构造
	 *
	 * @param numberLength 参与计算最大数字位数
	 */
	public MathGenerator(final int numberLength) {
		this.numberLength = numberLength;
	}

	@Override
	public String generate() {
		final int limit = getLimit();
		String number1 = Integer.toString(RandomUtil.randomInt(limit));
		String number2 = Integer.toString(RandomUtil.randomInt(limit));
		number1 = StrUtil.padAfter(number1, this.numberLength, CharUtil.SPACE);
		number2 = StrUtil.padAfter(number2, this.numberLength, CharUtil.SPACE);

		return StrUtil.builder()//
				.append(number1)//
				.append(RandomUtil.randomChar(operators))//
				.append(number2)//
				.append('=').toString();
	}

	@Override
	public boolean verify(final String code, final String userInputCode) {
		final int result;
		try {
			result = Integer.parseInt(userInputCode);
		} catch (final NumberFormatException e) {
			// 用户输入非数字
			return false;
		}

		final int calculateResult = (int) Calculator.conversion(code);
		return result == calculateResult;
	}

	/**
	 * 获取验证码长度
	 *
	 * @return 验证码长度
	 */
	public int getLength() {
		return this.numberLength * 2 + 2;
	}

	/**
	 * 根据长度获取参与计算数字最大值
	 *
	 * @return 最大值
	 */
	private int getLimit() {
		return Integer.parseInt("1" + StrUtil.repeat('0', this.numberLength));
	}
}
