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

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import org.openhab.core.items.Item;
import org.openhab.core.persistence.PersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the implementation of the Emoncms {@link PersistenceService}.
 * 
 * @author Nicolas Bonnefond (nicolas.bonnefond@inria.fr)
 */
public class EmoncmsLogger {

	private String apiKey;
	private String econcmsUrl;
	private int node;
	private boolean round;
	public static String FUNCTION_POST = "input/post.json?";
	public static String NODE = "node=";
	public static String JSON = "json=";
	public static final String API = "apikey=";
	public static final String UNINITIALIZED = "Uninitialized";

	private static final Logger logger = LoggerFactory
			.getLogger(EmoncmsLogger.class);

	public EmoncmsLogger(String econcmsUrl, String apiKey, int node,
			boolean round) {
		this.apiKey = apiKey;
		this.econcmsUrl = econcmsUrl;
		this.node = node;
		this.round = round;
	}

	public boolean logEvent(Item item) {

		if (!item.getState().toString().equals(UNINITIALIZED)) {
			HashMap<String, String> datas = new HashMap<String, String>();

			String value = item.getState().toString();

			try {
				if (round) {
					value = "" + Math.round(Double.parseDouble(value));
				}
			} catch (Exception e1) {
				logger.error("error trying to round "
						+ item.getState().toString() + "  : "
						+ e1.getLocalizedMessage());
			}

			datas.put(item.getName(), value);

			try {
				this.postDatas(datas);
			} catch (IOException e) {
				logger.debug("emoncms error : " + e);
				return false;
			}

			return true;
		} else {
			logger.debug("emoncms logger : object " + item.getName()
					+ " Uninitialized");
			return false;
		}
	}

	public void postDatas(HashMap<String, String> datas) throws IOException {
		StringBuilder urlString = new StringBuilder(this.econcmsUrl);
		urlString.append(FUNCTION_POST);
		urlString.append(NODE + this.node);
		urlString.append("&" + API + this.apiKey);
		urlString.append("&" + JSON + "{");

		if (datas != null) {
			for (String key : datas.keySet()) {

				urlString.append(key + ":" + datas.get(key));
				urlString.append(",");
			}
		}
		urlString.replace(urlString.lastIndexOf(","),
				urlString.lastIndexOf(",") + 1, "");
		urlString.append("}");

		URL url = new URL(urlString.toString());
		logger.debug("Posting " + url.toExternalForm());
		logger.debug("retour : " + url.getContent());
	}

}
