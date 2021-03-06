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
import org.apache.log4j.Logger;

/**
 *
 * @author marijom
 */
public class BackFrontTokenizerFactory extends AbstractTokenizerFactory{
    
    private static Logger log = Logger.getLogger(BackFrontTokenizerFactory.class);

    protected int takeBack=0;
    protected int takeFront=0;
    
    @Inject
    public BackFrontTokenizerFactory(Index index, IndexSettingsService indexSettingsService, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettingsService.getSettings(), name, settings);
        takeBack = settings.getAsInt("takeBack", 0);
        takeFront = settings.getAsInt("takeFront", 0);
//        log.info("Factory takeBack: "+takeBack+" takeFront: "+takeFront);
//        log.info("Factory Settings: "+settings.toDelimitedString('#'));
    }

    @Override
    public Tokenizer create() {
//        log.info("Tokenizer created by factory");
        return new BackFrontTokenizer(takeBack, takeFront);
    }        
}
