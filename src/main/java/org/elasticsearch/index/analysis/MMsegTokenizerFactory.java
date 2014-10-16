package org.elasticsearch.index.analysis;

import com.chenlb.mmseg4j.*;
import com.chenlb.mmseg4j.analysis.MMSegTokenizer;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettings;

import java.io.File;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Medcl'
 * Date: 12-6-6
 * Time: 下午3:59
 */
public class MMsegTokenizerFactory extends AbstractTokenizerFactory {

    Dictionary dic;
    private String seg_type;
    private boolean forceAddWholeSentence;
    private int maxLenSentenceToAdd;

    @Inject
    public MMsegTokenizerFactory(Index index, @IndexSettings Settings indexSettings,Environment env, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings, name, settings);
        String path=new File(env.configFile(),"mmseg").getPath();
        dic = Dictionary.getInstance(path);
        seg_type = settings.get("seg_type", "max_word");
        forceAddWholeSentence = settings.getAsBoolean("force_add_whole_sentence", Boolean.FALSE).booleanValue();
        if (forceAddWholeSentence) {
        	maxLenSentenceToAdd = settings.getAsInt("max_len_sentence_to_add", 6);
        	if (maxLenSentenceToAdd < 3)
        		maxLenSentenceToAdd = 3;
        }
    }

    @Override
    public Tokenizer create(Reader reader) {
        Seg seg_method=null;
        if(seg_type.equals("max_word")){
            seg_method = new MaxWordSeg(dic);
        }else if(seg_type.equals("complex")){
            seg_method = new ComplexSeg(dic);
        }else if(seg_type.equals("simple")){
            seg_method =new SimpleSeg(dic);
        }
        if (forceAddWholeSentence) {
        	seg_method = new ForceWholeSentenceSeg(dic, seg_method, maxLenSentenceToAdd);
        }
        return  new MMSegTokenizer(seg_method,reader);
    }
}
