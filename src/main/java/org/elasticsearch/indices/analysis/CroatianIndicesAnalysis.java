/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.elasticsearch.indices.analysis;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.analysis.BackFrontTokenizer;
import org.elasticsearch.index.analysis.NumberTokenizer;
import org.elasticsearch.index.analysis.PreBuiltTokenizerFactoryFactory;
import org.elasticsearch.index.analysis.TokenizerFactory;

/**
 * Registers indices level analysis components so, if not explicitly configured, will be shared
 * among all indices.
 * @author Marijo
 */
public class CroatianIndicesAnalysis extends AbstractComponent {
    
    private static Logger log = Logger.getLogger(CroatianIndicesAnalysis.class);

    @Inject
    public CroatianIndicesAnalysis(Settings settings, IndicesAnalysisService indicesAnalysisService) {
        super(settings);
        int takeBack = settings.getAsInt("tokenizer.FrontBackTokenizer.takeBack", 0);
        int takeFront = settings.getAsInt("tokenizer.FrontBackTokenizer.takeFront", 0);
//        log.info("Analysis takeBack: "+takeBack+" takeFront: "+takeFront);
//        log.info("Analysis Settings: "+settings.toDelimitedString('#'));

        indicesAnalysisService.tokenizerFactories().put("croatian_number_tokenizer", new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
            @Override
            public String name() {
                return "croatian_number_tokenizer";
            }

            @Override
            public Tokenizer create() {
                return new NumberTokenizer();
            }
        }));

        indicesAnalysisService.tokenizerFactories().put("croatian_backfront_tokenizer", new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
            @Override
            public String name() {
                return "croatian_backfront_tokenizer";
            }

            @Override
            public Tokenizer create() {
//                log.info("Tokenizer created by analysis");
                return new BackFrontTokenizer(1, 3);
            }
        }));

//        indicesAnalysisService.tokenFilterFactories().put("icu_normalizer", new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
//            @Override
//            public String name() {
//                return "icu_normalizer";
//            }
//
//            @Override
//            public TokenStream create(TokenStream tokenStream) {
//                return new org.apache.lucene.analysis.icu.ICUNormalizer2Filter(tokenStream, Normalizer2.getInstance(null, "nfkc_cf", Normalizer2.Mode.COMPOSE));
//            }
//        }));
//
//
//        indicesAnalysisService.tokenFilterFactories().put("icu_folding", new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
//            @Override
//            public String name() {
//                return "icu_folding";
//            }
//
//            @Override
//            public TokenStream create(TokenStream tokenStream) {
//                return new ICUFoldingFilter(tokenStream);
//            }
//        }));
//
//        indicesAnalysisService.tokenFilterFactories().put("icu_collation", new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
//            @Override
//            public String name() {
//                return "icu_collation";
//            }
//
//            @Override
//            public TokenStream create(TokenStream tokenStream) {
//                return new ICUCollationKeyFilter(tokenStream, Collator.getInstance());
//            }
//        }));
//
//        indicesAnalysisService.tokenFilterFactories().put("icu_transform", new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
//            @Override
//            public String name() {
//                return "icu_transform";
//            }
//
//            @Override
//            public TokenStream create(TokenStream tokenStream) {
//                return new ICUTransformFilter(tokenStream, Transliterator.getInstance("Null", Transliterator.FORWARD));
//            }
//        }));
//        
//        indicesAnalysisService.charFilterFactories().put("icu_normalizer", new PreBuiltCharFilterFactoryFactory(new CharFilterFactory() {
//            @Override
//            public String name() {
//                return "icu_normalizer";
//            }
//
//            @Override
//            public Reader create(Reader reader) {
//                return new ICUNormalizer2CharFilter(reader);
//            }
//        }));
    }
}
