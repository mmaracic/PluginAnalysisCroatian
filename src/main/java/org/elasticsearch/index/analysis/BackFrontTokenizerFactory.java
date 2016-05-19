/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettingsService;

/**
 *
 * @author marijom
 */
public class BackFrontTokenizerFactory extends AbstractTokenizerFactory{
    
    protected int takeBack=0;
    protected int takeFront=0;
    
    @Inject
    public BackFrontTokenizerFactory(Index index, IndexSettingsService indexSettingsService, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettingsService.getSettings(), name, settings);
        takeBack = settings.getAsInt("index.analysis.tokenizer.takeBack", 0);
        takeFront = settings.getAsInt("index.analysis.tokenizer.takeFront", 0);
    }

    @Override
    public Tokenizer create() {
        return new BackFrontTokenizer(takeBack, takeFront);
    }        
}
