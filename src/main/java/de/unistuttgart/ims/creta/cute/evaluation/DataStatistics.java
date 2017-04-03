package de.unistuttgart.ims.creta.cute.evaluation;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;
import de.unistuttgart.creta.api.type.Token;
import de.unistuttgart.ims.creta.api.Entity;
import de.unistuttgart.ims.creta.api.EntityCNC;
import de.unistuttgart.ims.creta.api.EntityEVT;
import de.unistuttgart.ims.creta.api.EntityLOC;
import de.unistuttgart.ims.creta.api.EntityORG;
import de.unistuttgart.ims.creta.api.EntityPER;
import de.unistuttgart.ims.creta.api.EntityWRK;

public class DataStatistics {

	public static void main(String[] args) throws ResourceInitializationException, UIMAException, IOException {

		Options options = CliFactory.parseArguments(Options.class, args);

		SimplePipeline.runPipeline(
				CollectionReaderFactory.createReaderDescription(XmiReader.class, XmiReader.PARAM_SOURCE_LOCATION,
						options.getInput().getAbsolutePath() + "/*.xmi", XmiReader.PARAM_LENIENT, true),
				AnalysisEngineFactory.createEngineDescription(Statistics.class));
	}

	public static class Statistics extends JCasAnnotator_ImplBase {

		@Override
		public void process(JCas jcas) throws AnalysisEngineProcessException {
			StringBuilder b = new StringBuilder();

			if (JCasUtil.exists(jcas, DocumentMetaData.class)) {
				b.append("documentId: ").append(DocumentMetaData.get(jcas).getDocumentId());
				b.append("\n");
			}

			b.append("Token: ").append(JCasUtil.select(jcas, Token.class).size()).append("\n");
			b.append("Entity: ").append(JCasUtil.select(jcas, Entity.class).size()).append("\n");
			b.append("EntityPER: ").append(JCasUtil.select(jcas, EntityPER.class).size()).append("\n");
			b.append("EntityLOC: ").append(JCasUtil.select(jcas, EntityLOC.class).size()).append("\n");
			b.append("EntityORG: ").append(JCasUtil.select(jcas, EntityORG.class).size()).append("\n");
			b.append("EntityWRK: ").append(JCasUtil.select(jcas, EntityWRK.class).size()).append("\n");
			b.append("EntityEVT: ").append(JCasUtil.select(jcas, EntityEVT.class).size()).append("\n");
			b.append("EntityCNC: ").append(JCasUtil.select(jcas, EntityCNC.class).size()).append("\n");

			System.out.println(b.toString());
		}

	}

	public interface Options {

		@Option
		public File getInput();
	}
}
