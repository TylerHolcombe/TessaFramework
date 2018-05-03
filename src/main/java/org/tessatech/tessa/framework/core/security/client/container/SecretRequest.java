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

package org.tessatech.tessa.framework.core.security.client.container;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SecretRequest", description = "The fields necessary to retrieve the current currentSecret.")
public class SecretRequest
{
	@ApiModelProperty(value = "The name of the requesting application.", required = true)
	public String appName;

	@ApiModelProperty(value = "The epoch timestamp in milliseconds of the request to retrieve the current currentSecret.", required = true)
	public long timestamp;

	@ApiModelProperty(value = "A 24 character nonce used to generate this request.", required = true)
	public String nonce;

	@ApiModelProperty(value = "The HMAC-SHA256 Encoded Signature of this request. Format: appName:timestmap:nonce.", required = true)
	public String signature;
}
