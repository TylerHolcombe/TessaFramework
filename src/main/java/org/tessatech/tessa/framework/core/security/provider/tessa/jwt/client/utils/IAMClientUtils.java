/*
 * Copyright (c) 2015-2018 TessaTech LLC.
 *
 * Licensed under the MIT License (the "License"); you may not use this file
 *  except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 *
 */

package org.tessatech.tessa.framework.core.security.provider.tessa.jwt.client.utils;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.core.security.provider.tessa.jwt.client.TessaIAMServiceClient;
import org.tessatech.tessa.framework.core.security.provider.tessa.jwt.client.container.SecretAuthorization;

@Component
public class IAMClientUtils
{
	private static TessaIAMServiceClient tessaIamServiceClient;

	@Value("${security.tessa.iam.secret.hmac.key}")
	private String hmacKey;

	public SecretAuthorization generateSecretAuthorization(String appName)
	{
		SecretAuthorization request = new SecretAuthorization();
		request.appName = appName;
		request.timestamp = System.currentTimeMillis();
		request.nonce = RandomStringUtils.randomAlphanumeric(24);
		request.signature = generateSignature(request);
		return request;
	}

	public String generateSignature(SecretAuthorization request)
	{
		HmacUtils hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_512, hmacKey);
		return hmac.hmacHex(request.appName + ":" + request.timestamp + ":" + request.nonce);
	}
}
