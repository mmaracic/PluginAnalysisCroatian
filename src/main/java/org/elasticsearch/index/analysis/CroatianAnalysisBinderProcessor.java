/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elasticsearch.index.analysis;

/**
 *
 * @author Marijo
 */
public class CroatianAnalysisBinderProcessor extends AnalysisModule.AnalysisBinderProcessor {

    @Override
    public void processCharFilters(CharFiltersBindings charFiltersBindings) {
        charFiltersBindings.processCharFilter("icu_normalizer", IcuNormalizerCharFilterFactory.class);
    }

    @Override
    public void processTokenizers(TokenizersBindings tokenizersBindings) {
        tokenizersBindings.processTokenizer("icu_tokenizer", IcuTokenizerFactory.class);
    }

    @Override
    public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {
        tokenFiltersBindings.processTokenFilter("icu_normalizer", IcuNormalizerTokenFilterFactory.class);
        tokenFiltersBindings.processTokenFilter("icu_folding", IcuFoldingTokenFilterFactory.class);
        tokenFiltersBindings.processTokenFilter("icu_collation", IcuCollationTokenFilterFactory.class);
        tokenFiltersBindings.processTokenFilter("icu_transform", IcuTransformTokenFilterFactory.class);
    }
}
