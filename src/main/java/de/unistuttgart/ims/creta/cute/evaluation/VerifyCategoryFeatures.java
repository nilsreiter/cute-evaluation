package de.unistuttgart.ims.creta.cute.evaluation;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.unistuttgart.ims.creta.api.Entity;
import de.unistuttgart.ims.creta.api.EntityCNC;
import de.unistuttgart.ims.creta.api.EntityEVT;
import de.unistuttgart.ims.creta.api.EntityLOC;
import de.unistuttgart.ims.creta.api.EntityORG;
import de.unistuttgart.ims.creta.api.EntityPER;
import de.unistuttgart.ims.creta.api.EntityWRK;

public class VerifyCategoryFeatures extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (Entity e : JCasUtil.select(jcas, Entity.class)) {
			if (e.getClass() == EntityPER.class)
				e.setCategory("PER");
			else if (e.getClass() == EntityLOC.class)
				e.setCategory("LOC");
			else if (e.getClass() == EntityCNC.class)
				e.setCategory("CNC");
			else if (e.getClass() == EntityEVT.class)
				e.setCategory("EVT");
			else if (e.getClass() == EntityORG.class)
				e.setCategory("ORG");
			else if (e.getClass() == EntityWRK.class)
				e.setCategory("WRK");
		}
	}

}
