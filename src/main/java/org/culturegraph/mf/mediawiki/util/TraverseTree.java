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
package org.culturegraph.mf.mediawiki.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sweble.wikitext.lazy.parser.ExternalLink;
import org.sweble.wikitext.lazy.parser.ImageLink;
import org.sweble.wikitext.lazy.parser.InternalLink;
import org.sweble.wikitext.lazy.parser.Section;
import org.sweble.wikitext.lazy.parser.Table;
import org.sweble.wikitext.lazy.parser.TableCaption;
import org.sweble.wikitext.lazy.parser.TableCell;
import org.sweble.wikitext.lazy.parser.TableHeader;
import org.sweble.wikitext.lazy.parser.TableRow;
import org.sweble.wikitext.lazy.parser.XmlElement;
import org.sweble.wikitext.lazy.preprocessor.Template;
import org.sweble.wikitext.lazy.preprocessor.TemplateArgument;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.ContentNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

/**
 * Traverses an AST. This is a base class for doing a
 * depth-first traversal of a wiki page AST.
 *
 * @author Christoph Böhme
 *
 */
public class TraverseTree extends AstVisitor {

	protected static final Logger LOG
			= LoggerFactory.getLogger(TraverseTree.class);

	public void visit(final AstNode node) {
		// Skip all node types not handled below
		LOG.debug("Unhandled AST node type: {}", node.getNodeTypeName());
	}

	public void visit(final NodeList node) {
		iterate(node);
	}

	public void visit(final ContentNode node) {
		dispatch(node.getContent());
	}

	public void visit(final InternalLink node) {
		dispatch(node.getTitle());
	}

	public void visit(final ExternalLink node) {
		dispatch(node.getTitle());
	}

	public void visit(final ImageLink node) {
		dispatch(node.getTitle());
	}

	public void visit(final Section node) {
		dispatch(node.getTitle());
		dispatch(node.getBody());
	}

	public void visit(final Table node) {
		dispatch(node.getBody());
	}

	public void visit(final TableCaption node) {
		dispatch(node.getBody());
	}

	public void visit(final TableCell node) {
		dispatch(node.getBody());
	}

	public void visit(final TableHeader node) {
		dispatch(node.getBody());
	}

	public void visit(final TableRow node) {
		dispatch(node.getBody());
	}

	public void visit(final Template node) {
		dispatch(node.getName());
		dispatch(node.getArgs());
	}

	public void visit(final TemplateArgument node) {
		dispatch(node.getName());
		dispatch(node.getValue());
	}

	public void visit(final XmlElement node) {
		dispatch(node.getBody());
	}

}
