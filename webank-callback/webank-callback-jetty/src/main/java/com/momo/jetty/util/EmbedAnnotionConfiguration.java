package com.momo.jetty.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.annotations.AbstractDiscoverableAnnotationHandler;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.annotations.AnnotationParser;
import org.eclipse.jetty.annotations.ClassNameResolver;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * 解决jetty自带的AnnotationConfiguration只扫描WEB-INF/classes的问题，maven编译后的目录为target，
 * 不符合其规则
 * 
 */
public class EmbedAnnotionConfiguration extends AnnotationConfiguration {

	@Override
	public void parseContainerPath(final WebAppContext context, final AnnotationParser parser) throws Exception {
		super.parseContainerPath(context, parser);
		parse(context, parser);
	}

	protected void parse(final WebAppContext context, AnnotationParser parser) throws Exception {
		List<Resource> _resources = getResources(getClass().getClassLoader());

		for (Resource _resource : _resources) {
			if (_resource == null) {
				return;
			}

			parser.clearHandlers();
			for (AnnotationParser.DiscoverableAnnotationHandler h : _discoverableAnnotationHandlers) {
				if (h instanceof AbstractDiscoverableAnnotationHandler) {
					((AbstractDiscoverableAnnotationHandler) h).setResource(null); //
				}
			}
			parser.registerHandlers(_discoverableAnnotationHandlers);
			parser.registerHandler(_classInheritanceHandler);
			parser.registerHandlers(_containerInitializerAnnotationHandlers);

			parser.parse(_resource, new ClassNameResolver() {
				public boolean isExcluded(String name) {
					if (context.isSystemClass(name)) {
						return true;
					}
					if (context.isServerClass(name)) {
						return false;
					}
					return false;
				}

				public boolean shouldOverride(String name) {
					return !context.isParentLoaderPriority();
				}
			});
		}
	}

	protected List<Resource> getResources(ClassLoader aLoader) throws IOException {
		List<Resource> _result = new ArrayList<Resource>();
		URL[] _urls = ((URLClassLoader) aLoader).getURLs();
		for (URL _url : _urls) {
			_result.add(Resource.newResource(_url));
		}
		return _result;
	}

}
