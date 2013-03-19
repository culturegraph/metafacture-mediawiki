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
package org.culturegraph.mf.mediawiki.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.culturegraph.mf.mediawiki.type.WikiPage;
import org.culturegraph.mf.stream.pipe.ObjectBuffer;
import org.culturegraph.mf.util.ResourceUtil;
import org.junit.Test;

/**
 * @author Christoph Böhme
 *
 */
public final class ParseWikiPageTest {

	private static final long PAGEID_BIRMINGHAM = 57252L;
	private static final long REVISIONID_BIRMINGHAM = 105226552L;
	private static final String URL_BIRMINGHAM = "http://de.wikipedia.org/wiki/Birmingham";
	private static final String TITLE_BIRMINGHAM = "Birmingham";
	private static final String WIKITEXT_FILE_BIRMINGHAM = "wikitext/birmingham.txt";

	@Test
	public void test() throws IOException {
		
		final WikiTextParser wikiPageParser = new WikiTextParser();
		final ObjectBuffer<WikiPage> results = new ObjectBuffer<WikiPage>(); 
		
		wikiPageParser.setReceiver(results);
		
		final String wikitextBirmingham = ResourceUtil.loadTextFile(WIKITEXT_FILE_BIRMINGHAM);
		
		WikiPage page = new WikiPage();
		page.setPageId(PAGEID_BIRMINGHAM);
		page.setRevisionId(REVISIONID_BIRMINGHAM);
		page.setUrl(URL_BIRMINGHAM);
		page.setTitle(TITLE_BIRMINGHAM);
		page.setWikiText(wikitextBirmingham);
		page.setWikiAst(null);
		
		wikiPageParser.process(page);
		wikiPageParser.closeStream();
		
		page = results.pop();
		assertNotNull(page);
		assertEquals(PAGEID_BIRMINGHAM, page.getPageId());
		assertEquals(REVISIONID_BIRMINGHAM, page.getRevisionId());
		assertEquals(URL_BIRMINGHAM, page.getUrl());
		assertEquals(TITLE_BIRMINGHAM, page.getTitle());
		assertEquals(wikitextBirmingham, page.getWikiText());
		assertNotNull(page.getWikiAst());
		
		// NOTE: This test does not check whether the AST produced by WikiTextParser is
		// correct. It merely checks that a CompiledPage object is created and added
		// to the WikiPage object.
	}
	
}
