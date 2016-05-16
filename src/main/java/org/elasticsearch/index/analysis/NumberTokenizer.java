/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elasticsearch.index.analysis;

import java.io.IOException;
import java.nio.CharBuffer;
import org.apache.lucene.analysis.Tokenizer;

/**
 *
 * @author Marijo
 */
public class NumberTokenizer  extends Tokenizer{

    protected static final int BUFFERMAX = 1024;
    protected final char buffer[] = new char[BUFFERMAX];
    /** true length of text in the buffer */
    private int length = 0; 
    /** accumulated offset of previous buffers for this reader, for offsetAtt */
    protected int offset = 0;

    @Override
    public boolean incrementToken() throws IOException {
        input.r
        return false;
    }
    
}
