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

import org.culturegraph.mf.framework.MetafactureException;
import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.framework.helpers.DefaultObjectPipe;
import org.culturegraph.mf.mediawiki.type.WikiPage;

import de.fau.cs.osr.ptk.common.json.JsonConverter;

/**
 * Adds a serialised version of the AST to the {@link WikiPage} object.
 *
 * @author Christoph Böhme
 */
public final class AstToJson extends DefaultObjectPipe<WikiPage, ObjectReceiver<WikiPage>> {

	@Override
	public void process(final WikiPage wikiPage) {
		if (null == wikiPage.getWikiAst()) {
			throw new MetafactureException("The wiki page does not contain an AST");
		}
		wikiPage.setJsonAst(JsonConverter.toJson(wikiPage.getWikiAst(), false));
		getReceiver().process(wikiPage);
	}
}
