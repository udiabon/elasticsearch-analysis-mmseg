package com.chenlb.mmseg4j;

public class ForceWholeSentenceSeg extends Seg {
	private Seg seg;
	private int maxLenSentenceToAdd;
	
	public ForceWholeSentenceSeg(Dictionary dic, Seg seg, int maxLenSentenceToAdd) {
		super(dic);
		this.seg = seg;
		this.maxLenSentenceToAdd = maxLenSentenceToAdd;
		if (this.maxLenSentenceToAdd < 3)
			this.maxLenSentenceToAdd = 3;
	}

	@Override
	public Chunk seg(Sentence sen) {
		char[] text = sen.getText();
		Chunk chunk = seg.seg(sen);
		if (text.length <= this.maxLenSentenceToAdd) {
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
