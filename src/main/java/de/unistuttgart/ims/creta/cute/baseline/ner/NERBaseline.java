package de.unistuttgart.ims.creta.cute.baseline.ner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.uima.UIMAException;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.xml.sax.SAXException;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.Event;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.Location;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.Organization;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.Person;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.WorkOfArt;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordNamedEntityRecognizer;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.unistuttgart.creta.api.type.Token;
import de.unistuttgart.ims.creta.api.Entity;
import de.unistuttgart.ims.creta.api.EntityEVT;
import de.unistuttgart.ims.creta.api.EntityLOC;
import de.unistuttgart.ims.creta.api.EntityORG;
import de.unistuttgart.ims.creta.api.EntityPER;
import de.unistuttgart.ims.creta.api.EntityWRK;
import de.unistuttgart.ims.creta.cute.evaluation.Filters;
import de.unistuttgart.ims.uimautil.ClearAnnotation;
import de.unistuttgart.ims.uimautil.MapAnnotations;
import de.unistuttgart.ims.uimautil.SetDocumentId;
import de.unistuttgart.ims.uimautil.SetJCasLanguage;

public class NERBaseline {

	public static void main(String[] args)
			throws ResourceInitializationException, UIMAException, IOException, SAXException {
		Options options = CliFactory.parseArguments(Options.class, args);

		for (File f : options.getInput().listFiles(Filters.xmiFilter)) {
			JCas jcas = JCasFactory.createJCas();

			InputStream is = new FileInputStream(f);
			XmiCasDeserializer.deserialize(is, jcas.getCas(), true);

			AggregateBuilder b = new AggregateBuilder();

			if (!JCasUtil.exists(jcas, Token.class)) {
				b.add(AnalysisEngineFactory.createEngineDescription(BreakIteratorSegmenter.class));
				b.add(AnalysisEngineFactory.createEngineDescription(MapAnnotations.class,
						MapAnnotations.PARAM_SOURCE_CLASS,
						de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token.class,
						MapAnnotations.PARAM_TARGET_CLASS, Token.class, MapAnnotations.PARAM_DELETE_SOURCE, true));
			}

			SimplePipeline.runPipeline(jcas, AnalysisEngineFactory.createEngineDescription(SetDocumentId.class,
					SetDocumentId.PARAM_DOCUMENT_ID, f.getName().substring(0, f.getName().indexOf('.'))),

					AnalysisEngineFactory.createEngineDescription(SetJCasLanguage.class, SetJCasLanguage.PARAM_LANGUAGE,
							"de"),
					AnalysisEngineFactory.createEngineDescription(ClearAnnotation.class, ClearAnnotation.PARAM_TYPE,
							Entity.class),
					b.createAggregateDescription(),
					AnalysisEngineFactory.createEngineDescription(StanfordNamedEntityRecognizer.class),
					AnalysisEngineFactory.createEngineDescription(MapAnnotations.class,
							MapAnnotations.PARAM_SOURCE_CLASS, Person.class, MapAnnotations.PARAM_TARGET_CLASS,
							EntityPER.class),
					AnalysisEngineFactory.createEngineDescription(MapAnnotations.class,
							MapAnnotations.PARAM_SOURCE_CLASS, Location.class, MapAnnotations.PARAM_TARGET_CLASS,
							EntityLOC.class),
					AnalysisEngineFactory.createEngineDescription(MapAnnotations.class,
							MapAnnotations.PARAM_SOURCE_CLASS, Organization.class, MapAnnotations.PARAM_TARGET_CLASS,
							EntityORG.class),
					AnalysisEngineFactory.createEngineDescription(MapAnnotations.class,
							MapAnnotations.PARAM_SOURCE_CLASS, Event.class, MapAnnotations.PARAM_TARGET_CLASS,
							EntityEVT.class),
					AnalysisEngineFactory.createEngineDescription(MapAnnotations.class,
							MapAnnotations.PARAM_SOURCE_CLASS, WorkOfArt.class, MapAnnotations.PARAM_TARGET_CLASS,
							EntityWRK.class),
					AnalysisEngineFactory.createEngineDescription(XmiWriter.class, XmiWriter.PARAM_TARGET_LOCATION,
							options.getOutput(), XmiWriter.PARAM_OVERWRITE, true));
		}

	}

	public interface Options {
		@Option(defaultValue = "/Users/reiterns/Documents/CRETA/cute/gold/xmi")
		public File getInput();

		@Option(defaultValue = "/Users/reiterns/Documents/CRETA/cute/silver/baseline-ner")
		public File getOutput();

	}

}
