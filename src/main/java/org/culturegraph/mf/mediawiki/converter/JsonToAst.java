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

import org.culturegraph.mf.exceptions.MetafactureException;
import org.culturegraph.mf.framework.DefaultObjectPipe;
import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.mediawiki.type.WikiPage;
import org.sweble.wikitext.engine.CompiledPage;

import de.fau.cs.osr.ptk.common.json.JsonConverter;

/**
 * Adds an AST to the [@link WikiPage} object by deserialising the
 * JSON representation in the {@link WikiPage} object.
 *  
 * @author Christoph Böhme
 */
public final class JsonToAst extends
		DefaultObjectPipe<WikiPage, ObjectReceiver<WikiPage>> {

	@Override
	public void process(final WikiPage wikiPage) {
		if (null == wikiPage.getJsonAst()) {
			throw new MetafactureException("The wiki page does not have a JSON-ised AST");
		}
		wikiPage.setWikiAst(JsonConverter.fromJson(wikiPage.getJsonAst(), CompiledPage.class));
		getReceiver().process(wikiPage);
	}
}
