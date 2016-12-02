package de.unistuttgart.ims.creta.cute.evaluation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.unistuttgart.creta.api.type.Token;
import de.unistuttgart.ims.creta.api.Entity;
import de.unistuttgart.ims.creta.api.EntityCNC;
import de.unistuttgart.ims.creta.api.EntityEVT;
import de.unistuttgart.ims.creta.api.EntityLOC;
import de.unistuttgart.ims.creta.api.EntityORG;
import de.unistuttgart.ims.creta.api.EntityPER;
import de.unistuttgart.ims.creta.api.EntityWRK;

public class ReadSilverCoNLL extends JCasAnnotator_ImplBase {

	public static final String PARAM_INPUT_DIRECTORY = "Input Directory";

	@ConfigurationParameter(name = PARAM_INPUT_DIRECTORY)
	File inputDirectory;

	Map<String, File> silverFiles = new HashMap<String, File>();

	@Override
	public void initialize(final UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		for (File f : inputDirectory.listFiles(Filters.conllFilter)) {
			silverFiles.put(f.getName().replaceAll(".tsv", ""), f);
		}

	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String id = DocumentMetaData.get(jcas).getDocumentId();

		if (!silverFiles.containsKey(id)) {
			throw new AnalysisEngineProcessException();
		}

		JCas silverView = null;
		try {
			silverView = jcas.createView(EvaluationMain.SILVER_VIEW);
			silverView.setDocumentText(jcas.getDocumentText());
		} catch (CASException e) {
			throw new AnalysisEngineProcessException(e);
		}

		try {
			CSVParser p = new CSVParser(new FileReader(silverFiles.get(id)), CSVFormat.TDF.withHeader((String) null));

			List<CSVRecord> records = p.getRecords();
			List<Token> tokens = new ArrayList<Token>(JCasUtil.select(jcas, Token.class));

			int e = 0;
			Map<String, AnnotationCandidate> candidates = new HashMap<String, AnnotationCandidate>();
			Set<String> toRemove = new HashSet<String>();
			for (int i = 0; i < records.size(); i++) {
				for (AnnotationCandidate c : candidates.values())
					c.setContinued(false);
				int b = tokens.get(i).getBegin();

				CSVRecord currentRecord = records.get(i);
				for (int j = 1; j < currentRecord.size(); j++) {
					String[] rec = currentRecord.get(j).split("_");
					if (rec.length == 2) {
						if (rec[0].startsWith("B")) {
							candidates.put(rec[1], new AnnotationCandidate(b, rec[0].substring(2)));
						} else if (rec[0].startsWith("I"))
							candidates.get(rec[1]).setContinued(true);
					}
				}

				for (String candId : candidates.keySet()) {
					AnnotationCandidate cand = candidates.get(candId);
					if (!cand.isContinued()) {
						AnnotationFactory.createAnnotation(silverView, cand.getBegin(), e, cand.getEntityClass());
						toRemove.add(candId);
					}
				}

				for (String c : toRemove) {
					candidates.remove(c);
				}
				e = tokens.get(i).getEnd();
			}

			IOUtils.closeQuietly(p);
		} catch (IOException e) {
			e.printStackTrace();
			throw new AnalysisEngineProcessException(e);
		}
	}

	class AnnotationCandidate {
		int begin;
		String category;
		boolean continued = false;

		public AnnotationCandidate(int begin, String category) {
			this.begin = begin;
			this.category = category;
			this.continued = true;
		}

		public int getBegin() {
			return begin;
		}

		public void setBegin(int begin) {
			this.begin = begin;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public Class<? extends Entity> getEntityClass() {
			if (category.equalsIgnoreCase("PER"))
				return EntityPER.class;
			else if (category.equalsIgnoreCase("CNC"))
				return EntityCNC.class;
			else if (category.equalsIgnoreCase("LOC"))
				return EntityLOC.class;
			else if (category.equalsIgnoreCase("ORG"))
				return EntityORG.class;
			else if (category.equalsIgnoreCase("WRK"))
				return EntityWRK.class;
			else if (category.equalsIgnoreCase("EVT"))
				return EntityEVT.class;
			return null;
		}

		public boolean isContinued() {
			return continued;
		}

		public void setContinued(boolean continued) {
			this.continued = continued;
		}
	}

}
