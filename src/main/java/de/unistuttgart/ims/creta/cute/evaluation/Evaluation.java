package de.unistuttgart.ims.creta.cute.evaluation;

import java.util.Collection;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.cleartk.eval.AnnotationStatistics;

import de.unistuttgart.ims.creta.api.Entity;

public class Evaluation {
	AnnotationStatistics<String> stats = new AnnotationStatistics<String>();

	public AnnotationStatistics<String> getStats() {
		return stats;
	}

	public void process(JCas jcas) throws AnalysisEngineProcessException {
		try {
			JCas silverView = jcas.getView(EvaluationMain.SILVER_VIEW);

			Collection<Entity> goldEntities = JCasUtil.select(jcas, Entity.class);
			Collection<Entity> systemEntities = JCasUtil.select(silverView, Entity.class);
			// stats.add(goldEntities, systemEntities);
			stats.add(goldEntities, systemEntities, AnnotationStatistics.annotationToSpan(),
					AnnotationStatistics.annotationToFeatureValue("category"));

		} catch (CASException e) {
			e.printStackTrace();
		}

	}

}
