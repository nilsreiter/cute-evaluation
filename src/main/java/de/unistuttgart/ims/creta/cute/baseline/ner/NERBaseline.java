package de.unistuttgart.ims.creta.cute.baseline.ner;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.Person;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordNamedEntityRecognizer;
import de.unistuttgart.ims.creta.api.Entity;
import de.unistuttgart.ims.creta.api.EntityPER;
import de.unistuttgart.ims.creta.cute.evaluation.Filters;
import de.unistuttgart.ims.uimautil.ClearAnnotation;
import de.unistuttgart.ims.uimautil.MapAnnotations;
import de.unistuttgart.ims.uimautil.SetDocumentId;
import de.unistuttgart.ims.uimautil.SetJCasLanguage;

public class NERBaseline {

	public static void main(String[] args) throws ResourceInitializationException, UIMAException, IOException {
		Options options = CliFactory.parseArguments(Options.class, args);

		for (File f : options.getInput().listFiles(Filters.xmiFilter)) {
			SimplePipeline.runPipeline(
					CollectionReaderFactory.createReaderDescription(XmiReader.class, XmiReader.PARAM_SOURCE_LOCATION,
							f.getAbsolutePath(), XmiReader.PARAM_LENIENT, true),
					AnalysisEngineFactory.createEngineDescription(SetDocumentId.class, SetDocumentId.PARAM_DOCUMENT_ID,
							f.getName().substring(0, f.getName().indexOf('.'))),
					AnalysisEngineFactory.createEngineDescription(SetJCasLanguage.class, SetJCasLanguage.PARAM_LANGUAGE,
							"de"),
					AnalysisEngineFactory.createEngineDescription(ClearAnnotation.class, ClearAnnotation.PARAM_TYPE,
							Entity.class),
					AnalysisEngineFactory.createEngineDescription(StanfordNamedEntityRecognizer.class),
					AnalysisEngineFactory.createEngineDescription(MapAnnotations.class,
							MapAnnotations.PARAM_SOURCE_CLASS, Person.class, MapAnnotations.PARAM_TARGET_CLASS,
							EntityPER.class),
					AnalysisEngineFactory.createEngineDescription(XmiWriter.class, XmiWriter.PARAM_TARGET_LOCATION,
							options.getOutput(), XmiWriter.PARAM_OVERWRITE, true));
		}

	}

	public interface Options {
		@Option(defaultValue = "src/test/resources/gold")
		public File getInput();

		@Option(defaultValue = "src/test/resources/baseline-ner")
		public File getOutput();

	}

}
