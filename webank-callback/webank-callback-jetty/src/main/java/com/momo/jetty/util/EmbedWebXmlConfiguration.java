package com.momo.jetty.util;

import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

public class EmbedWebXmlConfiguration extends WebXmlConfiguration {
	@Override
	public void preConfigure(WebAppContext context) throws Exception {
		super.preConfigure(context);
	}
}
