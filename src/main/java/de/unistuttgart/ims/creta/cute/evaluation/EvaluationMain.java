package de.unistuttgart.ims.creta.cute.evaluation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
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

	public static void main(String[] args)
			throws ResourceInitializationException, UIMAException, IOException, SAXException {
		Options options = CliFactory.parseArguments(Options.class, args);

		AggregateBuilder ag = new AggregateBuilder();

		if (options.getFormat().equalsIgnoreCase("xmi")) {
			ag.add(AnalysisEngineFactory.createEngineDescription(ReadSilverXmi.class,
					ReadSilverXmi.PARAM_INPUT_DIRECTORY, options.getSilver()));
		} else if (options.getFormat().equalsIgnoreCase("conll"))
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
				"target/", XmiWriter.PARAM_OVERWRITE, true));

		Evaluation eval = new Evaluation();
		for (File f : options.getGold().listFiles(Filters.xmiFilter)) {
			JCas jcas = JCasFactory.createJCas();
			XmiCasDeserializer.deserialize(new FileInputStream(f), jcas.getCas(), true);
			SimplePipeline.runPipeline(
					jcas, AnalysisEngineFactory.createEngineDescription(SetDocumentId.class,
							SetDocumentId.PARAM_DOCUMENT_ID, f.getName().replaceAll(".xmi", "")),
					ag.createAggregateDescription());

			eval.process(jcas);

		}
		AnnotationStatistics<String> stats = eval.getStats();
		System.out.println(stats.countCorrectOutcomes());
		System.out.println(stats.precision());
		System.out.println(stats.confusions().toHTML());
	}

	public interface Options {
		@Option(defaultValue = "src/test/resources/gold")
		public File getGold();

		@Option(defaultValue = "src/test/resources/baseline-ner")
		public File getSilver();

		@Option(defaultValue = "xmi")
		public String getFormat();
	}

}
