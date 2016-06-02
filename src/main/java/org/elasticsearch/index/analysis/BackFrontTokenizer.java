/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elasticsearch.index.analysis;

import java.io.IOException;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.log4j.Logger;

/**
 *
 * @author marijom
 */
public class BackFrontTokenizer extends Tokenizer{
    
    private static Logger log = Logger.getLogger(BackFrontTokenizer.class);

    protected static final int BUFFERMAX = 1024;
    protected static final int OUTPUTMAX = 512;
    protected final char buffer[] = new char[BUFFERMAX];
    protected final char outputBuffer[] = new char[OUTPUTMAX];
    /** true length of text in the buffer */
    private int length = 0; 
    /** offset of not-yet-used text in the buffer - first such character*/
    protected int offset = 0;
    /** end of not-yet-used text in the buffer - place after last such character*/
    protected int textEnd = 0;
    
    protected int outputEnd = 0;
    protected int outputSize = 0;
    protected int outputCounter = 0;
    boolean nonWs = false;

    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
    private final PositionIncrementAttribute positionAtt = addAttribute(PositionIncrementAttribute.class);
    
    protected int takeBack=0;
    protected int takeFront=0;
    
    public BackFrontTokenizer(int takeBack, int takeFront){
        super();
        this.takeBack = takeBack;
        this.takeFront = takeFront;
        //log.info("Tokenizer takeBack: "+takeBack+" takeFront: "+takeFront);
    }
  
    @Override
    public final boolean incrementToken() throws IOException
    {   
        boolean readAll = false;
        boolean endToken = false;
        if (outputSize>(takeBack+takeFront) && outputCounter >0 && outputCounter < ((takeBack+1)*(takeFront+1))){
            int tempTakeFront = outputCounter%(takeFront+1);
            int tempTakeBack = (int) (((float)outputCounter)/(takeFront+1));
            outputCounter++;

            termAtt.copyBuffer(outputBuffer, tempTakeBack, outputSize-tempTakeFront-tempTakeBack);
            //log.info(termAtt.toString());
            offsetAtt.setOffset(tempTakeBack, outputSize-tempTakeFront-1);
            positionAtt.setPositionIncrement(1);
            return true;
        } else {
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
                    if (!Character.isWhitespace(c))
                    {
                        //part of token
                        outputBuffer[outputEnd] = c;
                        outputEnd++;
                        nonWs = true;
                    } else {
                        //outside token
                        if (nonWs)
                        {
                            endToken = true;                                
                            break;
                        }
                    }
                    c = buffer[offset];
                }
                if (endToken || (readAll && nonWs)){
                    outputCounter=1;
                    break;
                }
                if (readAll){
                    return false;
                }
            }
            if (outputEnd>0){
                termAtt.copyBuffer(outputBuffer, 0, outputEnd);
                //log.info(termAtt.toString());
                offsetAtt.setOffset(0, outputEnd-1);
                positionAtt.setPositionIncrement(1);
                outputSize = outputEnd;
                outputEnd=0;
                nonWs = false;
                return true;
            } else {
                return false;
            }
        }
    }    
}
