package de.unistuttgart.ims.creta.cute.evaluation;

import org.apache.uima.jcas.tcas.Annotation;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

public class HashableSpan {
	public int end;

	public int begin;

	public HashableSpan(Annotation annotation) {
		this.begin = annotation.getBegin();
		this.end = annotation.getEnd();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.begin, this.end);
	}

	@Override
	public boolean equals(Object obj) {
		if (!this.getClass().equals(obj.getClass())) {
			return false;
		}
		HashableSpan that = (HashableSpan) obj;
		return this.begin == that.begin && this.end == that.end;
	}

	@Override
	public String toString() {
		ToStringHelper helper = Objects.toStringHelper(this);
		helper.add("begin", this.begin);
		helper.add("end", this.end);
		return helper.toString();
	}
}
