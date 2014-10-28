package com.chenlb.mmseg4j;

public class FullNameSeg extends Seg {
	private Seg seg;
	private int maxLenFullNameToAdd;
	
	public FullNameSeg(Dictionary dic, Seg seg, int maxLenFullNameToAdd) {
		super(dic);
		this.seg = seg;
		this.maxLenFullNameToAdd = maxLenFullNameToAdd;
		if (this.maxLenFullNameToAdd < 3)
			this.maxLenFullNameToAdd = 3;
	}

	@Override
	public Chunk seg(Sentence sen) {
		char[] text = sen.getText();
		Chunk chunk = seg.seg(sen);
		if (text.length <= this.maxLenFullNameToAdd) {
			int wordCount = chunk.getCount();
			for (int i = 0; i < wordCount; i++) {
				Word word = chunk.words[i];
				if (word.getStartOffset() == 0 && word.getLength() == text.length)
					return chunk;
			}
			Word word = new Word(text, sen.getStartOffset(), 0, text.length);
			Word newWords[] = new Word[wordCount+1];
			System.arraycopy(chunk.words,0, newWords, 0, wordCount);
			newWords[wordCount] = word;
			chunk.words = newWords;
			chunk.count = newWords.length;
		}
		return chunk;
	}

}
