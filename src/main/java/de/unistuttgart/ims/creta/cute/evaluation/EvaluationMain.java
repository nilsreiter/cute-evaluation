package de.unistuttgart.ims.creta.cute.evaluation;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.cas.CAS;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.cleartk.eval.AnnotationStatistics;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.unistuttgart.ims.uimautil.SetDocumentId;

public class EvaluationMain {

	public static String SILVER_VIEW = "Silver";

	public static void main(String[] args) throws ResourceInitializationException, UIMAException, IOException {
		Options options = CliFactory.parseArguments(Options.class, args);
		TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription();

		AggregateBuilder ag = new AggregateBuilder();

		if (options.getFormat().equalsIgnoreCase("xmi")) {
			ag.add(AnalysisEngineFactory.createEngineDescription(ReadSilverXmi.class,
					ReadSilverXmi.PARAM_INPUT_DIRECTORY, options.getSilver()));
		}

		ag.add(AnalysisEngineFactory.createEngineDescription(VerifyCategoryFeatures.class));
		ag.add(AnalysisEngineFactory.createEngineDescription(VerifyCategoryFeatures.class), SILVER_VIEW,
				CAS.NAME_DEFAULT_SOFA);
		ag.add(AnalysisEngineFactory.createEngineDescription(XmiWriter.class, XmiWriter.PARAM_TARGET_LOCATION,
				"target/", XmiWriter.PARAM_OVERWRITE, true));

		Evaluation eval = new Evaluation();
		for (File f : options.getGold().listFiles(XmiFilter.instance)) {
			JCas jcas = JCasFactory.createJCas(f.getAbsolutePath(), tsd);
			SimplePipeline.runPipeline(
					jcas, AnalysisEngineFactory.createEngineDescription(SetDocumentId.class,
							SetDocumentId.PARAM_DOCUMENT_ID, f.getName().replaceAll(".xmi", "")),
					ag.createAggregateDescription());

			eval.process(jcas);
			AnnotationStatistics<String> stats = eval.getStats();
			System.out.println(stats.countCorrectOutcomes());
			System.out.println(stats.precision());

		}
	}

	public interface Options {
		@Option(defaultValue = "src/test/resources")
		public File getGold();

		@Option(defaultValue = "src/test/resources")
		public File getSilver();

		@Option(defaultValue = "xmi")
		public String getFormat();
	}

}
