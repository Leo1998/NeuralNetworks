package com.nn.core.training;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LessonIterator implements Iterator<Sample> {

	private int currentIndex = 0;
	private Lesson lesson;

	public LessonIterator(Lesson lesson) {
		this.lesson = lesson;
	}

	@Override
	public boolean hasNext() {
		return currentIndex < lesson.countSamples();
	}

	@Override
	public Sample next() {
		if (!hasNext())
			throw new NoSuchElementException();

		Sample s = lesson.getSample(currentIndex);
		currentIndex++;

		return s;
	}

}
