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
package org.culturegraph.mf.mediawiki.analyzer;

import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.framework.annotations.Description;
import org.culturegraph.mf.framework.annotations.In;
import org.culturegraph.mf.framework.annotations.Out;
import org.culturegraph.mf.framework.helpers.DefaultObjectPipe;
import org.culturegraph.mf.mediawiki.converter.WikiTextParser.ParseLevel;
import org.culturegraph.mf.mediawiki.type.WikiPage;
import org.culturegraph.mf.mediawiki.util.TraverseTree;
import org.sweble.wikitext.lazy.parser.InternalLink;

/**
 * Extracts all internal links from the wiki page and emit them
 * as literals with INTERNAL_LINK as name and the link target as
 * value.
 *
 * @author Christoph Böhme
 *
 */
@Description("Extracts all internal links from a wiki page.")
@In(WikiPage.class)
@Out(StreamReceiver.class)
public final class LinkExtractor
		extends DefaultObjectPipe<WikiPage, StreamReceiver>
		implements Analyzer {

	private static final String LITERAL_NAME = "INTERNAL_LINK";

	private final LinkVisitor visitor = new LinkVisitor();

	@Override
	public void process(final WikiPage wikiPage) {
		getReceiver().startRecord(Long.toString(wikiPage.getPageId()));
		visitor.go(wikiPage.getWikiAst().getPage());
		getReceiver().endRecord();
	}

	@Override
	public boolean wikiTextOnly() {
		return false;
	}

	@Override
	public ParseLevel requiredParseLevel() {
		return ParseLevel.PARSE;
	}

	/**
	 * Searches the AST for internal links and outputs
	 * them as literals.
	 */
	public final class LinkVisitor extends TraverseTree {

		public void visit(final InternalLink node) {
			getReceiver().literal(LITERAL_NAME, node.getTarget());
		}

	}

}
