package com.nn.core.training;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class LessonShuffleIterator implements Iterator<Sample> {

	private int currentIndex = 0;
	private Sample[] shuffleArray;
	private Lesson lesson;

	public LessonShuffleIterator(Lesson lesson) {
		this.lesson = lesson;

		this.shuffleArray = new Sample[lesson.countSamples()];
		for (int i = 0; i < lesson.countSamples(); i++) {
			this.shuffleArray[i] = lesson.getSample(i);
		}

		shuffle();
	}

	private void shuffle() {
		Random random = new Random();
		for (int i = shuffleArray.length - 1; i > 0; i--) {
			int index = random.nextInt(i + 1);

			Sample tmp = shuffleArray[index];
			shuffleArray[index] = shuffleArray[i];
			shuffleArray[i] = tmp;
		}
	}

	@Override
	public boolean hasNext() {
		return currentIndex < lesson.countSamples();
	}

	@Override
	public Sample next() {
		if (!hasNext())
			throw new NoSuchElementException();

		Sample s = shuffleArray[currentIndex];
		currentIndex++;

		return s;
	}

}
