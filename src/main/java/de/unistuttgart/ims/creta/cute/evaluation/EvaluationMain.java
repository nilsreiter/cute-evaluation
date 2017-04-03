package de.unistuttgart.ims.creta.cute.evaluation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.uima.UIMAException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.cleartk.eval.AnnotationStatistics;
import org.xml.sax.SAXException;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.unistuttgart.ims.creta.api.Entity;
import de.unistuttgart.ims.uimautil.SetDocumentId;
import de.unistuttgart.ims.uimautil.TrimAnnotations;

public class EvaluationMain {

	public static String SILVER_VIEW = "Silver";

	public static List<String> categories = Arrays.asList("PER", "WRK", "ORG", "EVT", "CNC", "LOC");

	public static void main(String[] args)
			throws ResourceInitializationException, UIMAException, IOException, SAXException {
		Options options = CliFactory.parseArguments(Options.class, args);

		AggregateBuilder ag = new AggregateBuilder();

		if (options.getFormat().equalsIgnoreCase("xmi"))
			ag.add(AnalysisEngineFactory.createEngineDescription(ReadSilverXmi.class,
					ReadSilverXmi.PARAM_INPUT_DIRECTORY, options.getSilver()));
		else if (options.getFormat().equalsIgnoreCase("conll"))
			ag.add(AnalysisEngineFactory.createEngineDescription(ReadSilverCoNLL.class,
					ReadSilverCoNLL.PARAM_INPUT_DIRECTORY, options.getSilver()));

		ag.add(AnalysisEngineFactory.createEngineDescription(VerifyCategoryFeatures.class));
		ag.add(AnalysisEngineFactory.createEngineDescription(VerifyCategoryFeatures.class), CAS.NAME_DEFAULT_SOFA,
				SILVER_VIEW);
		ag.add(AnalysisEngineFactory.createEngineDescription(TrimAnnotations.class, TrimAnnotations.PARAM_TYPE,
				Entity.class));
		ag.add(AnalysisEngineFactory.createEngineDescription(TrimAnnotations.class, TrimAnnotations.PARAM_TYPE,
				Entity.class), CAS.NAME_DEFAULT_SOFA, SILVER_VIEW);
		ag.add(AnalysisEngineFactory.createEngineDescription(XmiWriter.class, XmiWriter.PARAM_TARGET_LOCATION,
				options.getOutput(), XmiWriter.PARAM_OVERWRITE, true, XmiWriter.PARAM_USE_DOCUMENT_ID, true));

		Evaluation eval = new Evaluation();
		for (File f : options.getGold().listFiles(Filters.xmiFilter)) {
			JCas jcas = JCasFactory.createJCas();
			XmiCasDeserializer.deserialize(new FileInputStream(f), jcas.getCas(), true);
			SimplePipeline.runPipeline(
					jcas, AnalysisEngineFactory.createEngineDescription(SetDocumentId.class,
							SetDocumentId.PARAM_DOCUMENT_ID, f.getName().replaceAll(".xmi", "")),
					ag.createAggregateDescription());

			if (JCasUtil.exists(jcas.getView(SILVER_VIEW), Entity.class))
				eval.process(jcas);

		}
		AnnotationStatistics<String> stats = eval.getCategoryBasedStats();
		FileWriter fw = new FileWriter(new File(options.getOutput(), options.getLabel() + ".strict.txt"));
		fw.write(eval("OVERALL", stats, categories));
		// fw.write(eval("spans", eval.getAnnotationBasedStats(),
		// Arrays.asList()));
		fw.flush();
		fw.close();

		fw = new FileWriter(new File(options.getOutput(), options.getLabel() + ".loose.txt"));
		/*
		 * fw.write(String.valueOf(eval.getLoose().precision()));
		 * fw.write("\t"); fw.write(String.valueOf(eval.getLoose().recall()));
		 * fw.write("\n");
		 */
		for (String s : categories) {
			fw.write(s);
			fw.write("\t");
			fw.write(String.valueOf(eval.getStats().get(s).precision()));
			fw.write("\t");
			fw.write(String.valueOf(eval.getStats().get(s).recall()));
			fw.write("\n");
		}

		fw.flush();
		fw.close();

		fw = new FileWriter(new File(options.getOutput(), options.getLabel() + ".tokens.txt"));
		fw.write("Token accuracy: ");
		fw.write(String.valueOf(eval.getTokenAccuracy()));
		fw.write('\n');
		fw.flush();
		fw.close();

	}

	public static String eval(String t, AnnotationStatistics<String> stats, Collection<String> classes) {
		StringBuilder b = new StringBuilder();

		b.append(t);
		b.append("\t").append(stats.precision());
		b.append("\t").append(stats.recall());
		b.append("\n");

		for (String s : classes) {
			b.append(s);
			if (stats.countPredictedOutcomes(s) == 0)
				b.append("\t").append(0);
			else
				b.append("\t").append(stats.precision(s));
			b.append("\t").append(stats.recall(s));
			b.append("\n");
		}

		b.append("\n");

		return b.toString();

	}

	public interface Options {

		@Option
		public String getLabel();

		@Option(defaultValue = "src/test/resources/gold")
		public File getGold();

		@Option(defaultValue = "src/test/resources/baseline-ner")
		public File getSilver();

		@Option(defaultValue = "conll")
		public String getFormat();

		@Option(defaultValue = "target/")
		public File getOutput();

		@Option
		List<String> getClasses();
	}

}
