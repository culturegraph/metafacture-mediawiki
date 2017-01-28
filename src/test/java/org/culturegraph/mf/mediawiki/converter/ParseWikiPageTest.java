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

import org.culturegraph.mf.commons.ResourceUtil;
import org.culturegraph.mf.javaintegration.ObjectCollector;
import org.culturegraph.mf.mediawiki.type.WikiPage;
import org.junit.Test;

/**
 * @author Christoph Böhme
 *
 */
public final class ParseWikiPageTest {

	@Test
	public void test() throws IOException {

		final WikiTextParser wikiPageParser = new WikiTextParser();
		final ObjectCollector<WikiPage> results = new ObjectCollector<>();

		wikiPageParser.setReceiver(results);

		final String wikitextBirmingham = ResourceUtil.loadTextFile(
				"wikitext/birmingham.txt");

		WikiPage page = new WikiPage();
		page.setPageId(57252L);
		page.setRevisionId(105226552L);
		page.setUrl("http://de.wikipedia.org/wiki/Birmingham");
		page.setTitle("Birmingham");
		page.setWikiText(wikitextBirmingham);
		page.setWikiAst(null);

		wikiPageParser.process(page);
		wikiPageParser.closeStream();

		page = results.pop();
		assertNotNull(page);
		assertEquals(57252L, page.getPageId());
		assertEquals(105226552L, page.getRevisionId());
		assertEquals("http://de.wikipedia.org/wiki/Birmingham", page.getUrl());
		assertEquals("Birmingham", page.getTitle());
		assertEquals(wikitextBirmingham, page.getWikiText());
		assertNotNull(page.getWikiAst());

		// NOTE: This test does not check whether the AST produced by WikiTextParser is
		// correct. It merely checks that a CompiledPage object is created and added
		// to the WikiPage object.
	}

}
