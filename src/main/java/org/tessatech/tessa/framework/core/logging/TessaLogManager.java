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

package org.tessatech.tessa.framework.core.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.core.logging.export.LogDataExporter;

@Component
public class TessaLogManager
{
	private static final Logger logger = LogManager.getLogger("org.tessatech.transaction.logger");

	@Value("${log.export.enabled:false}")
	private boolean exportEnabled = false;

	@Autowired
	private LogMessageBuilder logMessageBuilder;

	@Autowired
	private LogDataExporter logDataExporter;

	public void logTransaction(Object response)
	{
		writeLogMessage(logMessageBuilder.buildTransactionLog(response));
	}

	public void logEvent(Object response)
	{
		writeLogMessage(logMessageBuilder.buildEventLog(response));
	}


	private void writeLogMessage(String logMessage)
	{
		logger.info(logMessage);

		if (exportEnabled)
		{
			logDataExporter.exportLogMessage(logMessage);
		}
	}
}
