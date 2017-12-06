package com.momo.jetty.configuration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;

/**
 * 解决jetty自带的WebInfConfiguration只扫描WEB-INF/lib的问题，maven编译后的目录为target，不符合其规则
 * 
 */
public class EmbedWebInfConfiguration extends WebInfConfiguration {

	@Override
	protected List<Resource> findJars(WebAppContext context) throws Exception {
		List<Resource> list = super.findJars(context);
		if (list == null) {
			list = new ArrayList<Resource>();
		}
		ClassLoader aLoader = getClass().getClassLoader();
		if (aLoader instanceof URLClassLoader) {
			URL[] _urls = ((URLClassLoader) aLoader).getURLs();
			for (URL _url : _urls) {
				list.add(Resource.newResource(_url));
			}
			{
				String jarFile = new TldJarConfig().addLeopardTld(context, list);
				if (jarFile != null) {
					this.changeClassLoader(context, jarFile);
				}
				// System.err.println("jarFile:" + jarFile);
			}
		}

		return list;
	}

	protected void changeClassLoader(WebAppContext webContext, String jarFile) throws IOException {
		ClassLoader classLoader = webContext.getClassLoader();
		URL[] urls = new URL[1];
		urls[0] = new File(jarFile).toURI().toURL();
		URLClassLoader urlClassLoader = new URLClassLoader(urls, classLoader);
		webContext.setClassLoader(urlClassLoader);
	}
}
