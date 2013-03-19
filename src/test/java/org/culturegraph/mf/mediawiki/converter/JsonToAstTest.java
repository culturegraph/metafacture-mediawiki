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

import org.culturegraph.mf.mediawiki.type.WikiPage;
import org.culturegraph.mf.stream.pipe.ObjectBuffer;
import org.culturegraph.mf.util.ResourceUtil;
import org.junit.Test;

/**
 * @author Christoph Böhme
 *
 */
public final class JsonToAstTest {

	private static final long PAGEID_BIRMINGHAM = 57252L;
	private static final long REVISIONID_BIRMINGHAM = 105226552L;
	private static final String URL_BIRMINGHAM = "http://de.wikipedia.org/wiki/Birmingham";
	private static final String TITLE_BIRMINGHAM = "Birmingham";
	private static final String JSON_FILE_BIRMINGHAM = "json/birmingham.json";
	
	@Test
	public void testJsonToAst() throws IOException {
		final JsonToAst jsonToAst = new JsonToAst();
		final ObjectBuffer<WikiPage> result = new ObjectBuffer<WikiPage>();
		
		jsonToAst.setReceiver(result);

		final WikiPage page = new WikiPage();
		page.setPageId(PAGEID_BIRMINGHAM);
		page.setRevisionId(REVISIONID_BIRMINGHAM);
		page.setUrl(URL_BIRMINGHAM);
		page.setTitle(TITLE_BIRMINGHAM);
		page.setJsonAst(ResourceUtil.loadTextFile(JSON_FILE_BIRMINGHAM));
		page.setWikiAst(null);

		jsonToAst.process(page);
		jsonToAst.closeStream();
		
		assertNotNull(result.pop().getWikiAst());
		
		// NOTE: This test does not check whether the AST created
		// from the JSON presentation is correct. It merely tests
		// that an AST is created.
	}

}
