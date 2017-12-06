package com.momo.jetty.util;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.types.FileSet;

import com.momo.jetty.util.IORuntimeException;
import com.momo.jetty.util.IOUtil;

public class TldJar {
	private String dir;

	private String targetDir;

	private String timeFile;

	private String jarFile;

	public TldJar(String tldDir, String targetDir) {
		this.dir = tldDir;
		this.targetDir = targetDir;
		this.timeFile = targetDir + "/leopard-tld.time";
		this.jarFile = this.targetDir + "/leopard-tld.jar";
	}

	protected long getLastModified() {
		File dir = new File(this.dir + "/META-INF/");
		File[] files = dir.listFiles();
		long time = 0;
		if (files != null) {
			for (File file : files) {
				if (!file.getName().endsWith(".tld")) {
					continue;
				}
				time += file.lastModified();
				// System.out.println("file:" + file.getName() + " time:" + file.lastModified());
			}
		}
		return time;
	}

	protected boolean updateLastModified(long lastModified) {
		// System.out.println("updateLastModified:" + lastModified);
		String data = lastModified + "";
		IOUtil.writeStringToFile(new File(this.timeFile), data);
		return true;
	}

	protected long getLastModifiedByTargetFile() {
		try {
			String content = IOUtil.readFileToString(this.timeFile);
			return Long.parseLong(content);
		} catch (IORuntimeException e) {
			return -1;
		}

	}

	public String save() {
		if (this.dir == null) {
			FileUtils.deleteQuietly(new File(this.jarFile));
			return null;
		}
		long lastModified = this.getLastModified();
		long lastModifiedByTargetFile = this.getLastModifiedByTargetFile();
		if (lastModified != lastModifiedByTargetFile) {
			this.saveJar();
			this.updateLastModified(lastModified);
		}
		return this.jarFile;
	}

	protected boolean saveJar() {
		Project prj = new Project();
		Jar jar = new Jar();
		jar.setProject(prj);
		jar.setDestFile(new File(jarFile));

		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(new File(dir));
		fileSet.setIncludes("/META-INF/*.tld");

		jar.addFileset(fileSet);
		jar.execute();
		return true;
	}
}
