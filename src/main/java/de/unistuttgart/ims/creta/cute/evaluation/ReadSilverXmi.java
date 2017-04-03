package de.unistuttgart.ims.creta.cute.evaluation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.uima.UIMAException;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.XmlCasDeserializer;
import org.xml.sax.SAXException;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.unistuttgart.ims.creta.api.Entity;

public class ReadSilverXmi extends JCasAnnotator_ImplBase {

	public static final String PARAM_INPUT_DIRECTORY = "Input Directory";

	@ConfigurationParameter(name = PARAM_INPUT_DIRECTORY)
	File inputDirectory;

	Map<String, File> silverFiles = new HashMap<String, File>();

	@Override
	public void initialize(final UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		for (File f : inputDirectory.listFiles(Filters.xmiFilter)) {
			silverFiles.put(f.getName().replaceAll(".xmi", ""), f);
		}

	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String id = DocumentMetaData.get(jcas).getDocumentId();

		JCas silverView = null;
		try {
			silverView = jcas.createView(EvaluationMain.SILVER_VIEW);
			silverView.setDocumentText(jcas.getDocumentText());
		} catch (CASException e) {
			throw new AnalysisEngineProcessException(e);
		}

		if (!silverFiles.containsKey(id)) {
			return;
			// throw new AnalysisEngineProcessException();
		}
		try {
			JCas silverJCas = JCasFactory.createJCas();
			XmlCasDeserializer.deserialize(new FileInputStream(silverFiles.get(id)), silverJCas.getCas(), true);
			for (Entity entity : JCasUtil.select(silverJCas, Entity.class)) {
				Entity e = AnnotationFactory.createAnnotation(silverView, entity.getBegin(), entity.getEnd(),
						entity.getClass());
				e.setCategory(entity.getCategory());

			}

			for (POS pos : JCasUtil.select(silverJCas, POS.class)) {
				POS p = AnnotationFactory.createAnnotation(silverView, pos.getBegin(), pos.getEnd(), pos.getClass());
				p.setPosValue(pos.getPosValue());
			}

			for (Token token : JCasUtil.select(jcas, de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token.class)) {
				AnnotationFactory.createAnnotation(silverView, token.getBegin(), token.getEnd(),
						de.unistuttgart.creta.api.type.Token.class);
			}
		} catch (SAXException | IOException | UIMAException e) {
			e.printStackTrace();
			throw new AnalysisEngineProcessException(e);
		}
	}

}
