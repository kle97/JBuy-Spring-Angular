package io.spring.jbuy.common.search;

import org.apache.lucene.analysis.charfilter.HTMLStripCharFilterFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.payloads.DelimitedPayloadTokenFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;

/**
 * Custom analyzer, registered by hibernate.search.backend.analysis.configurer in application.properties
 */
public class CustomLuceneAnalysisConfigurer implements LuceneAnalysisConfigurer {

    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {
        context.analyzer("english").custom()
                .tokenizer(StandardTokenizerFactory.class)
                .charFilter(HTMLStripCharFilterFactory.class)
                .tokenFilter(LowerCaseFilterFactory.class)
                .tokenFilter(StopFilterFactory.class)
//                .tokenFilter(SynonymFilterFactory.class)
//                .param("ignoreCase", "true")
                .tokenFilter(SnowballPorterFilterFactory.class)
                .param("language", "English")
                .tokenFilter(ASCIIFoldingFilterFactory.class);

        context.normalizer("delimiter").custom()
                .tokenFilter(DelimitedPayloadTokenFilterFactory.class)
                .param("encoder", "identity");

        context.normalizer("category").custom()
                .charFilter(HTMLStripCharFilterFactory.class)
                .tokenFilter(LowerCaseFilterFactory.class)
                .tokenFilter(SnowballPorterFilterFactory.class)
                .param("language", "English");

        context.analyzer("autocomplete_indexing").custom()
                .tokenizer(WhitespaceTokenizerFactory.class)
                .tokenFilter(LowerCaseFilterFactory.class)
                .tokenFilter(ASCIIFoldingFilterFactory.class)
                .tokenFilter(EdgeNGramFilterFactory.class)
                .param("minGramSize", "1")
                .param("maxGramSize", "10");

        // Same as "autocomplete-indexing", but without the edge-ngram filter
        context.analyzer("autocomplete_search").custom()
                .tokenizer(WhitespaceTokenizerFactory.class)
                .tokenFilter(LowerCaseFilterFactory.class)
                .tokenFilter(ASCIIFoldingFilterFactory.class);
    }
}
