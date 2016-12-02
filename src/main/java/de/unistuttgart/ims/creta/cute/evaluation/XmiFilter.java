package de.unistuttgart.ims.creta.cute.evaluation;

import java.io.File;
import java.io.FilenameFilter;

public class XmiFilter implements FilenameFilter {

	public boolean accept(File dir, String name) {
		return name.endsWith(".xmi");
	}

	public static XmiFilter instance = new XmiFilter();

}
