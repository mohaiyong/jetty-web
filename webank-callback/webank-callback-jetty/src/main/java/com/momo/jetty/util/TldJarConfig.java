package com.momo.jetty.util;

import java.io.IOException;
import java.util.List;

import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

import com.momo.jetty.util.SystemUtil;
import com.momo.jetty.util.TldJar;

public class TldJarConfig {
	protected String getTldDir(final List<Resource> jars) throws IOException {
		for (Resource resource : jars) {
			String url = resource.toString();
			// if (url.endsWith(".jar")) {
			// System.err.println("url:" + url);
			// }
			if (url.endsWith("platform-web/target/classes/")) {
				return replaceUrl(url);
			}
		}
		return null;
	}

	protected String replaceUrl(String url) {
		if (SystemUtil.isWindows()) {
			return url.replace("file:/", "");
		} else {
			return url.replace("file:/", "/");
		}
	}

	protected String getTargetDir(final WebAppContext context) throws IOException {
		Resource resource = context.getWebInf();
		String webInfoUrl = resource.toString();
		// System.err.println("webInfoUrl:" + webInfoUrl);
		String webMoudleDir = webInfoUrl.substring(0, webInfoUrl.indexOf("/src/main/webapp/WEB-INF/"));
		String targetDir = webMoudleDir + "/target";
		// System.out.println("targetDir:" + targetDir);
		targetDir = replaceUrl(targetDir);
		return targetDir;
	}

	protected String addLeopardTld(final WebAppContext context, List<Resource> jars) throws IOException {
		String tldDir = this.getTldDir(jars);
		String targetDir = this.getTargetDir(context);
		return new TldJar(tldDir, targetDir).save();

	}
}
