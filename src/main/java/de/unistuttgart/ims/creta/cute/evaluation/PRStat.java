package de.unistuttgart.ims.creta.cute.evaluation;

public class PRStat {
	int tp = 0;
	int fp = 0;
	int fn = 0;

	public double precision() {
		return tp / ((double) tp + fp);
	}

	public double recall() {
		return tp / ((double) tp + fn);
	}

	public void tp1() {
		tp++;
	}

	public void fp1() {
		fp++;
	}

	public void fn1() {
		fn++;
	}

	public int tp() {
		return tp;
	}

	public int fp() {
		return fp;
	}

	public int fn() {
		return fn;
	}

}
