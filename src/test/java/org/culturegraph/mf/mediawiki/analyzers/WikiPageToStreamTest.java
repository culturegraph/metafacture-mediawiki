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
package org.culturegraph.mf.mediawiki.analyzers;

import static org.mockito.Mockito.inOrder;

import java.io.IOException;

import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.mediawiki.objects.WikiPage;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * @author Christoph Böhme
 *
 */
public final class WikiPageToStreamTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
	private StreamReceiver receiver;

	@Test
	public void test() throws IOException {
		final WikiPageToStream wikiPageToStream = new WikiPageToStream();
		wikiPageToStream.setOutputWikiText(true);

		wikiPageToStream.setReceiver(receiver);

		final WikiPage page = new WikiPage();
		page.setPageId(57252L);
		page.setRevisionId(105226552L);
		page.setUrl("http://de.wikipedia.org/wiki/Birmingham");
		page.setTitle("Birmingham");
		page.setWikiText("Wikitext Birmingham");
		page.setWikiAst(null);

		wikiPageToStream.process(page);

		final InOrder ordered = inOrder(receiver);
		ordered.verify(receiver).startRecord("57252");
		ordered.verify(receiver).literal("PAGE_ID", "57252");
		ordered.verify(receiver).literal("REVISION_ID", "105226552");
		ordered.verify(receiver).literal("URL", "http://de.wikipedia.org/wiki/Birmingham");
		ordered.verify(receiver).literal("PAGETITLE", "Birmingham");
		ordered.verify(receiver).literal("WIKITEXT", "Wikitext Birmingham");
	}

}
