/*
 *  Copyright 2013 Deutsche Nationalbibliothek
 *
 *  Licensed under the Apache License, Version 2.0 the "License";
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.culturegraph.mf.mediawiki.analyzer;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.culturegraph.mf.exceptions.FormatException;
import org.culturegraph.mf.mediawiki.converter.WikiTextParser;
import org.culturegraph.mf.mediawiki.converter.WikiTextParser.ParseLevel;
import org.culturegraph.mf.mediawiki.type.WikiPage;
import org.culturegraph.mf.stream.converter.CGTextDecoder;
import org.culturegraph.mf.stream.sink.EventList;
import org.culturegraph.mf.stream.sink.StreamValidator;
import org.culturegraph.mf.util.ResourceUtil;
import org.junit.Test;

/**
 * @author Christoph Böhme
 *
 */
public final class LinkExtractorTest {
	private static final String EXPECTED_STREAM_FILE = "expected-streams/links-birmingham-simple.txt"; 
	
	private static final long PAGEID_BIRMINGHAM = 57252L;
	private static final long REVISIONID_BIRMINGHAM = 105226552L;
	private static final String URL_BIRMINGHAM = "http://de.wikipedia.org/wiki/Birmingham";
	private static final String TITLE_BIRMINGHAM = "Birmingham";
	private static final String WIKITEXT_FILE_BIRMINGHAM = "wikitext/birmingham-simple.txt";
	
	@Test
	public void test() throws IOException {
		
		final CGTextDecoder cgTextDecoder = new CGTextDecoder();
		final EventList expected = new EventList();
		
		cgTextDecoder.setReceiver(expected);
		cgTextDecoder.process(ResourceUtil.loadTextFile(EXPECTED_STREAM_FILE));
		cgTextDecoder.closeStream();
		
		final WikiTextParser wikiPageParser = new WikiTextParser();
		wikiPageParser.setParseLevel(ParseLevel.PARSE);
		final LinkExtractor linkExtractor = new LinkExtractor();
		final StreamValidator validator = new StreamValidator(expected.getEvents());
		
		wikiPageParser.setReceiver(linkExtractor)
				.setReceiver(validator);
		
		final WikiPage page = new WikiPage();
		page.setPageId(PAGEID_BIRMINGHAM);
		page.setRevisionId(REVISIONID_BIRMINGHAM);
		page.setUrl(URL_BIRMINGHAM);
		page.setTitle(TITLE_BIRMINGHAM);
		page.setWikiText(ResourceUtil.loadTextFile(WIKITEXT_FILE_BIRMINGHAM));
		
		try {
			wikiPageParser.process(page);
			wikiPageParser.closeStream();
		} catch(FormatException e) {
			fail(e.toString());
		}
	}
	
}
