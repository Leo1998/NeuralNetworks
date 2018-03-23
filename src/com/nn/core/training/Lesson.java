package com.nn.core.training;

import java.util.Iterator;
import java.util.List;

public class Lesson implements Iterable<Sample> {

	private final Sample[] samples;

	public Lesson(Sample... samples) {
		this.samples = samples;
	}

	public Lesson(List<Sample> batch) {
		this.samples = new Sample[batch.size()];
		batch.toArray(samples);
	}

	public Sample[] getSamples() {
		return samples;
	}

	public Sample getSample(int i) {
		return samples[i];
	}

	public int countSamples() {
		return this.samples.length;
	}

	@Override
	public Iterator<Sample> iterator() {
		return new LessonShuffleIterator(this);
	}

}
