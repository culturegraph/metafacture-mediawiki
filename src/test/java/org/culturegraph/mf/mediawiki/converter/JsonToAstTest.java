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
import org.culturegraph.mf.mediawiki.JsonToAst;
import org.culturegraph.mf.mediawiki.objects.WikiPage;
import org.junit.Test;

/**
 * @author Christoph Böhme
 *
 */
public final class JsonToAstTest {

	@Test
	public void testJsonToAst() throws IOException {
		final JsonToAst jsonToAst = new JsonToAst();
		final ObjectCollector<WikiPage> result = new ObjectCollector<>();

		jsonToAst.setReceiver(result);

		final WikiPage page = new WikiPage();
		page.setPageId(57252L);
		page.setRevisionId(105226552L);
		page.setUrl("http://de.wikipedia.org/wiki/Birmingham");
		page.setTitle("Birmingham");
		page.setJsonAst(ResourceUtil.loadTextFile("json/birmingham.json"));
		page.setWikiAst(null);

		jsonToAst.process(page);
		jsonToAst.closeStream();

		assertNotNull(result.pop().getWikiAst());

		// NOTE: This test does not check whether the AST created
		// from the JSON presentation is correct. It merely tests
		// that an AST is created.
	}

}
