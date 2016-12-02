package de.unistuttgart.ims.creta.cute.evaluation;

import java.io.File;
import java.io.FilenameFilter;

public class Filters {

	public static FilenameFilter xmiFilter = new FilenameFilter() {

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".xmi");
		}
	};

	public static FilenameFilter conllFilter = new FilenameFilter() {

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".tsv");
		}
	};

}
