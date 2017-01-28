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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.framework.annotations.Description;
import org.culturegraph.mf.framework.annotations.In;
import org.culturegraph.mf.framework.annotations.Out;
import org.culturegraph.mf.framework.helpers.DefaultObjectPipe;
import org.culturegraph.mf.mediawiki.WikiTextParser.ParseLevel;
import org.culturegraph.mf.mediawiki.objects.WikiPage;
import org.culturegraph.mf.mediawiki.helpers.TextExtractor;
import org.culturegraph.mf.mediawiki.helpers.TraverseTree;
import org.sweble.wikitext.lazy.preprocessor.Template;
import org.sweble.wikitext.lazy.preprocessor.TemplateArgument;

import de.fau.cs.osr.ptk.common.ast.AstNode;

/**
 * Extracts all templates from the wiki page whose name matches
 * a pattern. Each template is wrapped in an entity and each
 * key value pair in the template is emitted as an literal. The
 * name of the entity is the template name. Spaces in entity and
 * literal names are replaced with underscores.
 *
 * @author Christoph Böhme
 *
 */
@Description("Extracts all templates from the wiki page whose name matches a pattern.")
@In(WikiPage.class)
@Out(StreamReceiver.class)
public final class TemplateExtractor
		extends DefaultObjectPipe<WikiPage, StreamReceiver>
		implements Analyzer {

	private Matcher nameMatcher;

	private final TemplateVisitor visitor = new TemplateVisitor();

	public TemplateExtractor() {
		this("");
	}

	public TemplateExtractor(final String namePattern) {
		super();
		setNamePattern(namePattern);
	}

	public void setNamePattern(final String namePattern) {
		nameMatcher = Pattern.compile(namePattern).matcher("");
	}

	public String getNamePattern() {
		return nameMatcher.pattern().pattern();
	}

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
		return ParseLevel.PREPROCESS;
	}

	/**
	 * Searches the AST for template nodes and outputs the
	 * relevant stream events.
	 */
	public class TemplateVisitor extends TraverseTree {

		private final TextExtractor textExtractor = new TextExtractor();

		public final void visit(final Template node) {
			final String name = extractText(node.getName());

			nameMatcher.reset(name.trim());
			if (nameMatcher.matches()) {
				getReceiver().startEntity(sanitizeName(name));
				getReceiver().literal("_TEMPLATE_", "");
				iterate(node.getArgs());
				getReceiver().endEntity();
			}
		}

		public final void visit(final TemplateArgument node) {
			if (node.getHasName()) {
				final String name = sanitizeName(extractText(node.getName()));
				final String value = sanitizeValue(extractText(node.getValue()));
				getReceiver().literal(name, value);
			}
		}

		private String extractText(final AstNode node) {
			return (String) textExtractor.go(node);
		}

		private String sanitizeName(final String name) {
			return name.trim().replace(" ", "_");
		}

		/**
		 * Removes everything between &lt;&gt; from the string and replaces
		 * internal and external links with their link text.
		 *
		 * @param string string to sanitise
		 * @return sanitised version of the input
		 */
		private String sanitizeValue(final String string) {
			return string.replaceAll("<[^>]+>", "")
					.replaceAll("\\[\\[(?:.*?\\|)*?([^|]*?)\\]\\]", "$1")
					.replaceAll("\\[(?:.*? )*?([^ ]*?)\\]", "$1")
					.trim();
		}

	}

}
