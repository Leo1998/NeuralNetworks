package com.nn.core.training;

import java.util.Iterator;

public class Lesson implements Iterable<Sample> {

	private final Sample[] samples;

	public Lesson(Sample... samples) {
		this.samples = samples;
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
