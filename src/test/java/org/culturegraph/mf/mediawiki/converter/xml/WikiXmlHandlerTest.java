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
package org.culturegraph.mf.mediawiki.converter.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.culturegraph.mf.io.ResourceOpener;
import org.culturegraph.mf.javaintegration.ObjectCollector;
import org.culturegraph.mf.mediawiki.WikiXmlHandler;
import org.culturegraph.mf.mediawiki.objects.WikiPage;
import org.culturegraph.mf.xml.XmlDecoder;
import org.junit.Test;

/**
 * @author Christoph Böhme
 *
 */
public final class WikiXmlHandlerTest {

	private static final String INCLUDE_MAIN_NS = "^[^:]+$";

	private static final String WIKI_XML_FILE = "xmldumps/frankfurt-birmingham-v0.7.xml";

	private static final long PAGEID_BIRMINGHAM = 57252L;
	private static final long REVISIONID_BIRMINGHAM = 105226552L;
	private static final String URL_BIRMINGHAM = "http://de.wikipedia.org/wiki/Birmingham";
	private static final String TITLE_BIRMINGHAM = "Birmingham";
	private static final String WIKITEXT_BIRMINGHAM = "Wikitext Birmingham";

	private static final long PAGEID_FRANKFURT = 306231L;
	private static final long REVISIONID_FRANKFURT = 102048201L;
	private static final String URL_FRANKFURT = "http://de.wikipedia.org/wiki/Frankfurt";
	private static final String TITLE_FRANKFURT = "Frankfurt";
	private static final String WIKITEXT_FRANKFURT = "Wikitext Frankfurt";

	@Test
	public void testIncludePagesPattern() {
		final WikiXmlHandler wikiXmlHandler = new WikiXmlHandler();
		wikiXmlHandler.setIncludePagesPattern(INCLUDE_MAIN_NS);

		runTest(wikiXmlHandler);
	}

	@Test
	public void testIncludeNamespaceIds() {
		final WikiXmlHandler wikiXmlHandler = new WikiXmlHandler();
		wikiXmlHandler.setIncludeNamespaceIds("0");

		runTest(wikiXmlHandler);
	}

	private void runTest(final WikiXmlHandler wikiXmlHandler) {
		final ResourceOpener opener = new ResourceOpener();
		final XmlDecoder xmlDecoder = new XmlDecoder();
		final ObjectCollector<WikiPage> results = new ObjectCollector<WikiPage>();

		opener.setReceiver(xmlDecoder)
				.setReceiver(wikiXmlHandler)
				.setReceiver(results);

		opener.process(WIKI_XML_FILE);
		opener.closeStream();

		WikiPage page;

		page = results.pop();
		assertNotNull(page);
		assertEquals(PAGEID_BIRMINGHAM, page.getPageId());
		assertEquals(REVISIONID_BIRMINGHAM, page.getRevisionId());
		assertEquals(URL_BIRMINGHAM, page.getUrl());
		assertEquals(TITLE_BIRMINGHAM, page.getTitle());
		assertEquals(WIKITEXT_BIRMINGHAM, page.getWikiText());
		assertNull(page.getWikiAst());

		page = results.pop();
		assertNotNull(page);
		assertEquals(PAGEID_FRANKFURT, page.getPageId());
		assertEquals(REVISIONID_FRANKFURT, page.getRevisionId());
		assertEquals(URL_FRANKFURT, page.getUrl());
		assertEquals(TITLE_FRANKFURT, page.getTitle());
		assertEquals(WIKITEXT_FRANKFURT, page.getWikiText());
		assertNull(page.getWikiAst());

		page = results.pop();
		assertNull(page);
	}

}
