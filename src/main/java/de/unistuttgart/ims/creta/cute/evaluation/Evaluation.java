package de.unistuttgart.ims.creta.cute.evaluation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.cleartk.eval.AnnotationStatistics;

import de.unistuttgart.creta.api.type.Token;
import de.unistuttgart.ims.creta.api.Entity;

public class Evaluation {
	AnnotationStatistics<String> categoryBasedStats = new AnnotationStatistics<String>();
	AnnotationStatistics<String> annotationBasedStats = new AnnotationStatistics<String>();

	Map<String, PRStat> stats = new HashMap<String, PRStat>();

	public Evaluation() {
		stats.put("PER", new PRStat());
		stats.put("LOC", new PRStat());
		stats.put("EVT", new PRStat());
		stats.put("WRK", new PRStat());
		stats.put("ORG", new PRStat());
		stats.put("CNC", new PRStat());
	}

	public AnnotationStatistics<String> getCategoryBasedStats() {
		return categoryBasedStats;
	}

	public void process(JCas jcas) throws AnalysisEngineProcessException {
		try {
			JCas silverView = jcas.getView(EvaluationMain.SILVER_VIEW);

			Collection<Entity> goldEntities = JCasUtil.select(jcas, Entity.class);
			Collection<Entity> systemEntities = JCasUtil.select(silverView, Entity.class);
			annotationBasedStats.add(goldEntities, systemEntities);
			categoryBasedStats.add(goldEntities, systemEntities, AnnotationStatistics.annotationToSpan(),
					AnnotationStatistics.annotationToFeatureValue("category"));

			for (Entity e : goldEntities) {
				for (Entity e2 : systemEntities) {
					if (overlapping(e, e2)) {
						if (e.getCategory() == e2.getCategory())
							stats.get(e.getCategory()).tp1();
						else {
							stats.get(e.getCategory()).fn1();
							stats.get(e2.getCategory()).fp1();
						}
					}
				}
			}

		} catch (CASException e) {
			e.printStackTrace();
		}

	}

	public boolean overlapping(Entity e1, Entity e2) {
		Collection<Token> c1 = JCasUtil.selectCovered(Token.class, e1);
		Collection<Token> c2 = JCasUtil.selectCovered(Token.class, e2);

		for (Token t1 : c1) {
			for (Token t2 : c2) {
				if (t1.getBegin() == t2.getBegin() && t1.getEnd() == t2.getEnd())
					return true;
			}
		}

		return false; // e1.getBegin() <= e2.getEnd() && e2.getBegin() <=
						// e1.getBegin();
	}

	public AnnotationStatistics<String> getAnnotationBasedStats() {
		return annotationBasedStats;
	}

	public Map<String, PRStat> getStats() {
		return stats;
	}

}
