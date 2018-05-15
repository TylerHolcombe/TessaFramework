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

package org.tessatech.tessa.framework.core.security.provider.tessa.jwt.client.container;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.ZonedDateTime;

@ApiModel(description = "An issued token to be used for Authorization and Authentication.")
public class Token
{

	@ApiModelProperty(value = "The App the token was generated for.")
	public String appName;

	@ApiModelProperty(value = "The User the token was generated for.")
	public String username;

	@ApiModelProperty(value = "The token to use on future API requests.")
	public String token;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	@ApiModelProperty(value = "The expiration date of this token.")
	public ZonedDateTime expires;

	@ApiModelProperty(value = "The type of token.", allowableValues = "IDENTITY,AUTHORIZATION")
	public String type;

}
