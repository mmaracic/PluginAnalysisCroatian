package org.elasticsearch.index.analysis.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static com.google.common.io.Files.createTempDir;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.elasticsearch.index.analysis.BackFrontTokenizer;
import org.elasticsearch.index.analysis.CroatianAnalysisBinderProcessor;
import org.elasticsearch.index.analysis.NumberTokenizer;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.index.settings.IndexSettingsModule;
import org.elasticsearch.indices.analysis.CroatianAnalysisModule;
import org.elasticsearch.indices.analysis.CroatianAnalysisService;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;
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
     public void TestTokenizer() throws IOException
     {
//        Settings settings = Settings.settingsBuilder()
//                .put("path.home", createTempDir())
//                .put("index.analysis.filter.myTokenizer.type", "croatian_number_tokenizer")
//                .build();
//        IndicesAnalysisService analysisService = createAnalysisService(settings);
//
//        TokenizerFactory filterFactory = analysisService.tokenizerFactories().get("croatian_number_tokenizer").create("number_f", settings);
//        assertCollatesToSame(filterFactory, "this is 1996 a sentence", "this is a sentence 1996");
     }
    private void assertCollatesToSame(TokenizerFactory factory, String string1, String string2) throws IOException {
        assertCollation(factory, string1, string2, 0);
    }
    
    private void assertCollation(TokenizerFactory factory, String string1, String string2, int comparison) throws IOException {
        Tokenizer tokenizer = new KeywordTokenizer();
        tokenizer.setReader(new StringReader(string1));
        Tokenizer stream1 = factory.create();
    
        tokenizer = new KeywordTokenizer();
        tokenizer.setReader(new StringReader(string2));
        Tokenizer stream2 = factory.create();
      
        assertCollation(stream1, stream2, comparison);
    }

    private void assertCollation(Tokenizer stream1, Tokenizer stream2, int comparison) throws IOException {
        CharTermAttribute term1 = stream1.addAttribute(CharTermAttribute.class);
        CharTermAttribute term2 = stream2.addAttribute(CharTermAttribute.class);

        stream1.reset();
        stream2.reset();

        assertTrue(stream1.incrementToken());
        assertTrue(stream2.incrementToken());
        assertEquals(Integer.signum(term1.toString().compareTo(term2.toString())), Integer.signum(comparison));
        assertFalse(stream1.incrementToken());
        assertFalse(stream2.incrementToken());
        
        stream1.end();
        stream2.end();
        
        stream1.close();
        stream2.close();
    }
    
    public static IndicesAnalysisService createAnalysisService(Settings settings) {
//        Index index = new Index("test");
//        Settings indexSettings = settingsBuilder().put(settings)
//                .put(IndexMetaData.SETTING_VERSION_CREATED, Version.CURRENT)
//                .build();
//        Injector parentInjector = new ModulesBuilder().add(new SettingsModule(settings), new EnvironmentModule(new Environment(settings))).createInjector();
//        Injector injector = new ModulesBuilder().add(
//                new IndexSettingsModule(index, indexSettings),
//                new IndexNameModule(index),
//                new CroatianAnalysisModule(settings, parentInjector.getInstance(IndicesAnalysisService.class)).addProcessor(new CroatianAnalysisBinderProcessor()))
//                .createChildInjector(parentInjector);
//
//        return injector.getInstance(IndicesAnalysisService.class);
        return null;
    }
    
    //@Test
    public void testTokenizer2()
    {
        try {
            Tokenizer tokenizer = new NumberTokenizer();
            tokenizer.setReader(new StringReader("this is 1996 a sentence 2004"));
            CharTermAttribute term1 = tokenizer.addAttribute(CharTermAttribute.class);
            
            tokenizer.reset();
            while(tokenizer.incrementToken())
            {
                String result = term1.toString();
                System.out.println(result);
            }
            tokenizer.end();
            tokenizer.close();
        } catch (IOException ex) {
            Logger.getLogger(CroatianAnalysisTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testFrontBackTokenizer()
    {
        try {
            Tokenizer tokenizer = new BackFrontTokenizer(1,3);
            tokenizer.setReader(new StringReader("this is 1996 a sentence 2004"));
            CharTermAttribute term1 = tokenizer.addAttribute(CharTermAttribute.class);
            
            tokenizer.reset();
            while(tokenizer.incrementToken())
            {
                String result = term1.toString();
                System.out.println(result);
            }
            tokenizer.end();
            tokenizer.close();
        } catch (IOException ex) {
            Logger.getLogger(CroatianAnalysisTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
