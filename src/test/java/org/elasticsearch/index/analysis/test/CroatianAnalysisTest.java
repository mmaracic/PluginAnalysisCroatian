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
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
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
import org.elasticsearch.index.analysis.BackFrontTokenizer;
import org.elasticsearch.index.analysis.CroatianAnalysisBinderProcessor;
import org.elasticsearch.index.analysis.NumberTokenizer;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.index.settings.IndexSettingsModule;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

     //@Test
     public void TestTokenizer() throws IOException
     {
        Settings settings = Settings.settingsBuilder()
                .put("path.home", createTempDir())
                .put("index.analysis.tokenizer.FrontBackTokenizer.type", "croatian_backfront_tokenizer")
                .put("index.analysis.tokenizer.FrontBackTokenizer.takeBack", 1)
                .put("index.analysis.tokenizer.FrontBackTokenizer.takeFront", 3)
                .build();
        int takeBack = settings.getAsInt("index.analysis.tokenizer.FrontBackTokenizer.takeBack", 0);
        int takeFront = settings.getAsInt("index.analysis.tokenizer.FrontBackTokenizer.takeFront", 0);
        IndicesAnalysisService analysisService = createAnalysisService(settings);
        String data = "this is 1996 a sentence";
        TokenizerFactory factory = analysisService.tokenizerFactories().get("croatian_backfront_tokenizer").create("croatian_backfront_tokenizer_factory", settings);
        Tokenizer tokenizer = factory.create();
        tokenizer.setReader(new StringReader(data));
        CharTermAttribute term1 = tokenizer.addAttribute(CharTermAttribute.class);
        
        tokenizer.reset();
        while(tokenizer.incrementToken())
        {
            String result = term1.toString();
            System.out.println(result);
        }
        tokenizer.end();
        tokenizer.close();
     }
    
    public static IndicesAnalysisService createAnalysisService(Settings settings) {
        Index index = new Index("test");
        Settings indexSettings = settingsBuilder().put(settings)
                .put(IndexMetaData.SETTING_VERSION_CREATED, Version.CURRENT)
                .build();
        Injector parentInjector = new ModulesBuilder().add(new SettingsModule(settings), new EnvironmentModule(new Environment(settings))).createInjector();
        Injector injector = new ModulesBuilder().add(
                new IndexSettingsModule(index, indexSettings),
                new IndexNameModule(index),
                new AnalysisModule(settings, parentInjector.getInstance(IndicesAnalysisService.class)).addProcessor(new CroatianAnalysisBinderProcessor()))
                .createChildInjector(parentInjector);

        return injector.getInstance(IndicesAnalysisService.class);
    }
    
    //@Test
    public void testNumberTokenizer()
    {
        try {
            Tokenizer tokenizer = new NumberTokenizer();
            tokenizer.setReader(new StringReader("this is 1996 a sentence 2004..  In a year 1998 i had 15 years."));
            //tokenizer.setReader(new StringReader("1996"));
            CharTermAttribute term1 = tokenizer.addAttribute(CharTermAttribute.class);
            TypeAttribute typeAtt = tokenizer.addAttribute(TypeAttribute.class);
            OffsetAttribute offsetAtt = tokenizer.addAttribute(OffsetAttribute.class);
            PositionIncrementAttribute positionAtt = tokenizer.addAttribute(PositionIncrementAttribute.class);
                    
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
            //tokenizer.setReader(new StringReader("this is 1996 a sentence 2004"));
            tokenizer.setReader(new StringReader("Primorsko-goranska županija.. This is a test. It is 1996 a sentence 2004..  In a year 1998 i had 15 years."));
            CharTermAttribute term1 = tokenizer.addAttribute(CharTermAttribute.class);
            TypeAttribute typeAtt = tokenizer.addAttribute(TypeAttribute.class);
            OffsetAttribute offsetAtt = tokenizer.addAttribute(OffsetAttribute.class);
            
            tokenizer.reset();
            while(tokenizer.incrementToken())
            {
                String result = term1.toString();
            }
            tokenizer.end();
            tokenizer.close();
        } catch (IOException ex) {
            Logger.getLogger(CroatianAnalysisTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
