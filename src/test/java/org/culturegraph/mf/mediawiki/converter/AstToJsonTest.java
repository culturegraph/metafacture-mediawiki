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

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.culturegraph.mf.commons.ResourceUtil;
import org.culturegraph.mf.javaintegration.ObjectCollector;
import org.culturegraph.mf.mediawiki.AstToJson;
import org.culturegraph.mf.mediawiki.WikiTextParser;
import org.culturegraph.mf.mediawiki.WikiTextParser.ParseLevel;
import org.culturegraph.mf.mediawiki.objects.WikiPage;
import org.junit.Test;

/**
 * Test {@link AstToJson}.
 *
 * @author Christoph Böhme
 *
 */
public final class AstToJsonTest {

	@Test
	public void testAstToJson() throws IOException {
		final WikiTextParser wikiPageParser = new WikiTextParser();
		wikiPageParser.setParseLevel(ParseLevel.POSTPROCESS);
		final AstToJson astToJson = new AstToJson();
		final ObjectCollector<WikiPage> result = new ObjectCollector<>();

		wikiPageParser.setReceiver(astToJson)
				.setReceiver(result);

		final WikiPage page = new WikiPage();
		page.setPageId(57252L);
		page.setRevisionId(105226552L);
		page.setUrl("http://de.wikipedia.org/wiki/Birmingham");
		page.setTitle("Birmingham");
		page.setWikiText(ResourceUtil.loadTextFile(
				"wikitext/birmingham.txt"));
		page.setJsonAst(null);

		wikiPageParser.process(page);
		wikiPageParser.closeStream();

		assertNotNull(result.pop().getJsonAst());

		// NOTE: This test does not check whether the output of the AstToJson
		// module is a valid JSON representation of the AST. It merely checks
		// that something is generated.
	}

}
