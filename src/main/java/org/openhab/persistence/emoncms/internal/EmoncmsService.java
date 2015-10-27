/**
 * Copyright (c) 2015, Inria.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Nicolas Bonnefond - initial API and implementation and/or initial documentation
 */
package org.openhab.persistence.emoncms.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.items.Item;
import org.openhab.core.persistence.PersistenceService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the implementation of the Emoncms {@link PersistenceService}.
 * 
 * @author Nicolas Bonnefond (nicolas.bonnefond@inria.fr)
 */
public class EmoncmsService implements PersistenceService, ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(EmoncmsService.class);

	private String apiKey;
	private String url;
	private int node;
	private EmoncmsLogger emoncmsLogger;
	private boolean round;

	private final static String DEFAULT_EVENT_URL = "http://emoncms.org/";

	private final static int DEFAULT_NODE = 0;

	private final static boolean DEFAULT_ROUND = false;

	private boolean initialized = false;

	/**
	 * @{inheritDoc
	 */
	public String getName() {
		return "emoncms";
	}

	/**
	 * @{inheritDoc
	 */
	public void store(Item item, String alias) {
		if (!initialized) {
			logger.debug("emoncms persistence logger not initialized");
		} else {
			logger.debug("logged item " + item.getName() + " = "
					+ this.emoncmsLogger.logEvent(item));
		}

	}

	/**
	 * @{inheritDoc
	 */
	public void store(Item item) {
		store(item, item.getName());
	}

	/**
	 * @{inheritDoc
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {

		logger.debug("Parsing new configuration for emoncms persistence service");

		if (config != null) {

			url = (String) config.get("url");
			if (StringUtils.isBlank(url)) {
				url = DEFAULT_EVENT_URL;
			}

			try {
				node = Integer.parseInt((String) config.get("node"));
			} catch (Exception e) {
				logger.error("emoncms using default node : " + DEFAULT_NODE
						+ " error : " + e.getLocalizedMessage());
				node = DEFAULT_NODE;
			}

			try {
				round = (boolean) Boolean.parseBoolean((String) config
						.get("round"));
			} catch (Exception e) {
				logger.error("emoncms using default round : " + DEFAULT_ROUND
						+ " error : " + e.getLocalizedMessage());
				round = DEFAULT_ROUND;
			}

			apiKey = (String) config.get("apikey");
			if (StringUtils.isBlank(apiKey)) {
				throw new ConfigurationException("emoncms:apikey",
						"The emoncms API-Key is missing - please configure it in openhab.cfg");
			}

			this.emoncmsLogger = new EmoncmsLogger(url, apiKey, node, round);
			initialized = true;
		}
	}

}
