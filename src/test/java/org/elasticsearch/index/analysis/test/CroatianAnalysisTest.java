package org.elasticsearch.index.analysis.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static org.hamcrest.Matchers.equalTo;
import static com.google.common.io.Files.createTempDir;
import java.io.IOException;
import java.io.StringReader;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.elasticsearch.Version;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.inject.ModulesBuilder;
import org.elasticsearch.common.settings.Settings;
import static org.elasticsearch.common.settings.Settings.settingsBuilder;
import org.elasticsearch.common.settings.SettingsModule;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.EnvironmentModule;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.IndexNameModule;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.AnalysisService;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.index.settings.IndexSettingsModule;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marijo
 */
public class CroatianAnalysisTest {
    
    public CroatianAnalysisTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

     @Test
     public void TestTokenizer()
     {
        Settings settings = Settings.settingsBuilder()
                .put("path.home", createTempDir())
                .put("index.analysis.filter.myCollator.type", "icu_collation")
                .put("index.analysis.filter.myCollator.language", "tr")
                .put("index.analysis.filter.myCollator.strength", "primary")
                .build();
        AnalysisService analysisService = createAnalysisService(settings);

        TokenFilterFactory filterFactory = analysisService.tokenFilter("myCollator");
        assertCollatesToSame(filterFactory, "I WİLL USE TURKİSH CASING", "ı will use turkish casıng");
     }
    private void assertCollatesToSame(TokenFilterFactory factory, String string1, String string2) throws IOException {
        assertCollation(factory, string1, string2, 0);
    }
    
    private void assertCollation(TokenFilterFactory factory, String string1, String string2, int comparison) throws IOException {
        Tokenizer tokenizer = new KeywordTokenizer();
        tokenizer.setReader(new StringReader(string1));
        TokenStream stream1 = factory.create(tokenizer);
    
        tokenizer = new KeywordTokenizer();
        tokenizer.setReader(new StringReader(string2));
        TokenStream stream2 = factory.create(tokenizer);
      
        assertCollation(stream1, stream2, comparison);
    }

    private void assertCollation(TokenStream stream1, TokenStream stream2, int comparison) throws IOException {
        CharTermAttribute term1 = stream1.addAttribute(CharTermAttribute.class);
        CharTermAttribute term2 = stream2.addAttribute(CharTermAttribute.class);

        stream1.reset();
        stream2.reset();

        assertThat(stream1.incrementToken(), equalTo(true));
        assertThat(stream2.incrementToken(), equalTo(true));
        assertThat(Integer.signum(term1.toString().compareTo(term2.toString())), equalTo(Integer.signum(comparison)));
        assertThat(stream1.incrementToken(), equalTo(false));
        assertThat(stream2.incrementToken(), equalTo(false));
        
        stream1.end();
        stream2.end();
        
        stream1.close();
        stream2.close();
    }
    
    public static AnalysisService createAnalysisService(Settings settings) {
        Index index = new Index("test");
        Settings indexSettings = settingsBuilder().put(settings)
                .put(IndexMetaData.SETTING_VERSION_CREATED, Version.CURRENT)
                .build();
        Injector parentInjector = new ModulesBuilder().add(new SettingsModule(settings), new EnvironmentModule(new Environment(settings))).createInjector();
        Injector injector = new ModulesBuilder().add(
                new IndexSettingsModule(index, indexSettings),
                new IndexNameModule(index),
                new AnalysisModule(settings, parentInjector.getInstance(IndicesAnalysisService.class)).addProcessor(new IcuAnalysisBinderProcessor()))
                .createChildInjector(parentInjector);

        return injector.getInstance(AnalysisService.class);
    }     
}
