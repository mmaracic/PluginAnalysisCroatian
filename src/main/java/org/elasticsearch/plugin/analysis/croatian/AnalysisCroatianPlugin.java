/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elasticsearch.plugin.analysis.croatian;

import org.elasticsearch.indices.analysis.CroatianAnalysisModule;
import java.util.Collection;
import java.util.Collections;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.CroatianAnalysisBinderProcessor;
import org.elasticsearch.plugins.Plugin;

/**
 *
 * @author Marijo
 */
public class AnalysisCroatianPlugin extends Plugin{
    @Override
    public String name() {
        return "analysis-croatian";
    }

    @Override
    public String description() {
        return "Croatian analysis support";
    }

    @Override
    public Collection<Module> nodeModules() {
        return Collections.<Module>singletonList(new CroatianAnalysisModule());
    }

    /**
     * Automatically called with the analysis module.
     */
    public void onModule(AnalysisModule module) {
        module.addProcessor(new CroatianAnalysisBinderProcessor());
    }
    
}
