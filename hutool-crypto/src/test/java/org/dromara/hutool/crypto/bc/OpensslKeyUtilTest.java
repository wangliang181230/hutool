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

package org.dromara.hutool.crypto.bc;

import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.crypto.asymmetric.SM2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

public class OpensslKeyUtilTest {

	@Test
	public void verifyPemUtilReadKey() {
		// 公钥
		// PKCS#10 文件读取公钥
		final PublicKey csrPublicKey = (PublicKey) OpensslKeyUtil.readPemKey(ResourceUtil.getStream("test_ec_certificate_request.csr"), null);

		// 证书读取公钥
		final PublicKey certPublicKey = (PublicKey) OpensslKeyUtil.readPemKey(ResourceUtil.getStream("test_ec_certificate.cer"), null);

		// PEM 公钥
		final PublicKey plainPublicKey = (PublicKey) OpensslKeyUtil.readPemKey(ResourceUtil.getStream("test_ec_public_key.pem"), null);

		// 私钥
		// 加密的 PEM 私钥
		final PrivateKey encPrivateKey = (PrivateKey) OpensslKeyUtil.readPemKey(ResourceUtil.getStream("test_ec_encrypted_private_key.key"), "123456".toCharArray());

		// PKCS#8 私钥
		final PrivateKey pkcs8PrivateKey = (PrivateKey) OpensslKeyUtil.readPemKey(ResourceUtil.getStream("test_ec_pkcs8_private_key.key"), null);

		// SEC 1 私钥
		final PrivateKey sec1PrivateKey = (PrivateKey) OpensslKeyUtil.readPemKey(ResourceUtil.getStream("test_ec_sec1_private_key.pem"), null);

		// 组装还原后的公钥和私钥列表
		final List<PublicKey> publicKeyList = Arrays.asList(csrPublicKey, certPublicKey, plainPublicKey);
		final List<PrivateKey> privateKeyList = Arrays.asList(encPrivateKey, pkcs8PrivateKey, sec1PrivateKey);

		// 做笛卡尔积循环验证
		for (final PrivateKey privateKeyItem : privateKeyList) {
			for (final PublicKey publicKeyItem : publicKeyList) {
				// 校验公私钥
				final SM2 genSm2 = new SM2(privateKeyItem, publicKeyItem);
				genSm2.usePlainEncoding();

				final String content = "我是Hanley.";
				final byte[] sign = genSm2.sign(ByteUtil.toUtf8Bytes(content));
				final boolean verify = genSm2.verify(ByteUtil.toUtf8Bytes(content), sign);
				Assertions.assertTrue(verify);
			}
		}
	}
}
