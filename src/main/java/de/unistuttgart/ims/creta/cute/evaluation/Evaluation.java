package de.unistuttgart.ims.creta.cute.evaluation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.cleartk.eval.AnnotationStatistics;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.unistuttgart.creta.api.type.Token;
import de.unistuttgart.ims.creta.api.Entity;

public class Evaluation {
	AnnotationStatistics<String> categoryBasedStats = new AnnotationStatistics<String>();
	AnnotationStatistics<String> annotationBasedStats = new AnnotationStatistics<String>();

	Map<String, PRStat> stats = new HashMap<String, PRStat>();
	int correctTokens = 0;
	int allTokens = 0;

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
			if (DocumentMetaData.get(jcas).getDocumentId().equalsIgnoreCase("3_28_26")) {
				System.out.println();
			}

			JCas silverView = jcas.getView(EvaluationMain.SILVER_VIEW);

			Collection<Entity> goldEntities = JCasUtil.select(jcas, Entity.class);
			Collection<Entity> systemEntities = JCasUtil.select(silverView, Entity.class);
			annotationBasedStats.add(goldEntities, systemEntities);
			categoryBasedStats.add(goldEntities, systemEntities, AnnotationStatistics.annotationToSpan(),
					AnnotationStatistics.annotationToFeatureValue("category"));

			tokenBasedEvaluation(jcas, silverView);

			// evalCategory(jcas, silverView, "PER", EntityPER.class);
			// evalCategory(jcas, silverView, "ORG", EntityORG.class);
			// evalCategory(jcas, silverView, "LOC", EntityLOC.class);
			// evalCategory(jcas, silverView, "WRK", EntityWRK.class);
			// evalCategory(jcas, silverView, "EVT", EntityEVT.class);
			// evalCategory(jcas, silverView, "CNC", EntityCNC.class);

			/*
			 * for (Entity e : goldEntities) { for (Entity e2 : systemEntities)
			 * { if (overlapping(e, e2)) { if (e.getCategory() ==
			 * e2.getCategory()) stats.get(e.getCategory()).tp1(); else {
			 * stats.get(e.getCategory()).fn1();
			 * stats.get(e2.getCategory()).fp1(); } } } }
			 */

		} catch (CASException e) {
			e.printStackTrace();
		}

	}

	public void tokenBasedEvaluation(JCas gold, JCas silver) {
		Map<Token, Collection<Entity>> goldIndex = JCasUtil.indexCovering(gold, Token.class, Entity.class);
		Map<Token, Collection<Entity>> silverIndex = JCasUtil.indexCovering(silver, Token.class, Entity.class);

		Iterator<Token> goldIterator = new LinkedList<Token>(JCasUtil.select(gold, Token.class)).iterator();
		Iterator<Token> silverIterator = new LinkedList<Token>(JCasUtil.select(silver, Token.class)).iterator();
		System.out.println("gold tokens: " + JCasUtil.select(gold, Token.class).size());
		System.out.println("silver tokens: " + JCasUtil.select(silver, Token.class).size());

		while (goldIterator.hasNext() /* && silverIterator.hasNext() */) {
			silverIterator.hasNext();
			allTokens++;
			Token gToken = goldIterator.next();
			Token sToken = silverIterator.next();

			Collection<Entity> gEntities = goldIndex.get(gToken);
			Collection<Entity> sEntities = silverIndex.get(sToken);
			if (gEntities.isEmpty() && sEntities.isEmpty())
				correctTokens++;
			else if (!gEntities.isEmpty() && !sEntities.isEmpty()) {
				// both have something
				Entity gE = gEntities.iterator().next();
				Entity sE = sEntities.iterator().next();
				if (gE.getCategory().equalsIgnoreCase(sE.getCategory()))
					correctTokens++;
			}

		}

	}

	public <T extends Entity> void evalCategory(JCas jcas, JCas silverView, String cat, Class<T> clazz)
			throws CASException {
		Collection<T> silverEntities = new LinkedList<T>(JCasUtil.select(silverView, clazz));
		for (T e : JCasUtil.select(jcas, clazz)) {
			Collection<T> candidates = getOverlappingEntities(jcas, e, silverEntities);
			if (candidates.isEmpty()) {
				stats.get(e.getCategory()).fn1();
			} else {
				stats.get(e.getCategory()).tp1();
			}
		}

		for (T e : silverEntities) {
			Collection<T> candidates = getOverlappingEntities(jcas, e, JCasUtil.select(jcas, clazz));
			if (candidates.isEmpty()) {
				stats.get(e.getCategory()).fp1();
			}
		}
	}

	public <T extends Annotation> Collection<T> getOverlappingEntities(JCas jcas, T e1, Collection<T> otherEntities)
			throws CASException {
		Set<T> r = new HashSet<T>();
		Set<T> toRemove = new HashSet<T>();

		Collection<Token> c1 = JCasUtil.selectCovered(Token.class, e1);
		for (T other : otherEntities) {
			Collection<Token> c2 = JCasUtil.selectCovered(jcas, Token.class, other.getBegin(), other.getEnd());
			for (Token t1 : c1) {
				for (Token t2 : c2) {
					if (t1.getBegin() == t2.getBegin() && t1.getEnd() == t2.getEnd()) {
						r.add(other);
						toRemove.add(other);
					}
				}
			}
		}

		for (T tr : toRemove)
			otherEntities.remove(tr);

		return r;

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

	public double getTokenAccuracy() {
		return correctTokens / (double) allTokens;
	}

}
