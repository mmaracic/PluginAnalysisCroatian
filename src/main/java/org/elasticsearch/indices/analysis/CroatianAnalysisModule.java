/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elasticsearch.indices.analysis;

import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.analysis.AnalysisModule;

/**
 *
 * @author Marijo
 */
public class CroatianAnalysisModule extends AbstractModule {

//    public CroatianAnalysisModule(Settings settings, IndicesAnalysisService indicesAnalysisService){
//        super(settings, indicesAnalysisService);
//    }
    
    @Override
    protected void configure() {
        bind(CroatianIndicesAnalysis.class).asEagerSingleton();
    }
}
