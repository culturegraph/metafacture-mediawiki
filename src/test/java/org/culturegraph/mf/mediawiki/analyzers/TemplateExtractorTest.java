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

import org.culturegraph.mf.commons.ResourceUtil;
import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.mediawiki.WikiTextParser;
import org.culturegraph.mf.mediawiki.objects.WikiPage;
import org.culturegraph.mf.monitoring.StreamLogger;
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
public final class TemplateExtractorTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
	private StreamReceiver receiver;

	@Test
	public void test() throws IOException {
		final WikiTextParser wikiPageParser = new WikiTextParser();
		final TemplateExtractor templateExtractor = new TemplateExtractor();
		templateExtractor.setNamePattern("Infobox.*");

		wikiPageParser
				.setReceiver(templateExtractor)
				.setReceiver(new StreamLogger())
				.setReceiver(receiver);

		final WikiPage page = new WikiPage();
		page.setPageId(57252L);
		page.setRevisionId(105226552L);
		page.setUrl("http://de.wikipedia.org/wiki/Birmingham");
		page.setTitle("Birmingham");
		page.setWikiText(ResourceUtil.loadTextFile(
				"wikitext/birmingham-simple.txt"));

		wikiPageParser.process(page);

		final InOrder ordered = inOrder(receiver);
		ordered.verify(receiver).startRecord("57252");
		ordered.verify(receiver).startEntity("Infobox_Ort_im_Vereinigten_Königreich");
		ordered.verify(receiver).literal("_TEMPLATE_", "");
		ordered.verify(receiver).literal("official_name", "City of Birmingham");
		ordered.verify(receiver).literal("local_name", "");
		ordered.verify(receiver).literal("country", "England");
		ordered.verify(receiver).literal("population", "1036878");
		ordered.verify(receiver).literal("shire_county", "West Midlands");
		ordered.verify(receiver).literal("website", "www.birmingham.gov.uk");
		ordered.verify(receiver).endEntity();
		ordered.verify(receiver).endRecord();
	}

}
