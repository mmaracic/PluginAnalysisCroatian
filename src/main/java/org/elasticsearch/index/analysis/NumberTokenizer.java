/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elasticsearch.index.analysis;

import java.io.IOException;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 *
 * @author Marijo
 */
public class NumberTokenizer  extends Tokenizer{

    protected static final int BUFFERMAX = 1024;
    protected final char buffer[] = new char[BUFFERMAX];
    protected final char outputBuffer[] = new char[BUFFERMAX];
    /** true length of text in the buffer */
    private int length = 0; 
    /** offset of not-yet-used text in the buffer - first such character*/
    protected int offset = 0;
    /** end of not-yet-used text in the buffer - place after last such character*/
    protected int textEnd = 0;
    
    Character lastC = null;
    
    protected int outputEnd = 0;
    protected boolean digit = false;

    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
    private final PositionIncrementAttribute positionAtt = addAttribute(PositionIncrementAttribute.class);
  
    @Override
    public final boolean incrementToken() throws IOException
    {   
        boolean readAll = false;
        boolean endToken = false;
        while(!readAll)
        {
            length = (offset<=textEnd)?textEnd-offset:BUFFERMAX-(offset-textEnd);
            while(length<BUFFERMAX){
                int count = 0;
                if (offset<=textEnd){
                    count = input.read(buffer, textEnd, BUFFERMAX-textEnd);
                } else if (offset>textEnd){
                    count = input.read(buffer, textEnd, offset-textEnd);
                }
                if (count == -1){
                    readAll = true;
                    break;
                }
                textEnd+=count;
                if (textEnd == BUFFERMAX) textEnd = 0;
                length = (offset<textEnd)?textEnd-offset:BUFFERMAX-(offset-textEnd);
            }

            char c = buffer[offset];
            while(length>0){
                offset++;
                length--;
                if (offset==BUFFERMAX)
                    offset=0;
                if (Character.isDigit(c) || (c=='.' && digit && ((lastC != null && Character.isDigit(lastC)) || lastC == null))
                        || (c==',' && digit && ((lastC != null && Character.isDigit(lastC)) || lastC == null)))
                {
                    outputBuffer[outputEnd] = c;
                    outputEnd++;
                    digit = true;
                } else {
                    if (digit)
                    {
                        endToken = true;
                        break;
                    }
                }
                lastC = c;
                c = buffer[offset];
            }
            if (endToken) break;
            if (readAll){
                return false;
            }
        }
        if (outputEnd>0){
            if (outputBuffer[outputEnd-1] == '.' || outputBuffer[outputEnd-1] == ','){
                outputEnd--;
            }
            termAtt.copyBuffer(outputBuffer, 0, outputEnd);
            offsetAtt.setOffset(0, outputEnd-1);
            typeAtt.setType(StandardTokenizer.TOKEN_TYPES[StandardTokenizer.NUM]);
            positionAtt.setPositionIncrement(1);
            outputEnd=0;
            digit = false;
            lastC = null;
            return true;
        } else {
            return false;
        }
    }
    
}
