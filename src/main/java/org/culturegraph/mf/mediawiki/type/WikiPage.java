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
package org.culturegraph.mf.mediawiki.type;

import org.culturegraph.mf.mediawiki.converter.WikiTextParser.ParseLevel;
import org.sweble.wikitext.engine.CompiledPage;

/**
 * Stores the wiki text, an optional AST of the wiki text and
 * additional metadata of a wiki page.
 * 
 * @author Christoph Böhme
 * 
 */
public final class WikiPage {

	private String url;
	private int namespaceId;
	private long pageId = -1;
	private String title;
	private long revisionId = -1;
	private String wikiText;
	private CompiledPage wikiAst;
	private String jsonAst;
	private ParseLevel parseLevel;

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public int getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(final int namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public long getPageId() {
		return pageId;
	}

	public void setPageId(final long pageId) {
		this.pageId = pageId;
	}

	public long getRevisionId() {
		return revisionId;
	}

	public void setRevisionId(final long revisionId) {
		this.revisionId = revisionId;
	}

	public String getWikiText() {
		return wikiText;
	}

	public void setWikiText(final String wikiText) {
		this.wikiText = wikiText;
	}

	public CompiledPage getWikiAst() {
		return wikiAst;
	}

	public void setWikiAst(final CompiledPage wikiAst) {
		this.wikiAst = wikiAst;
	}
	
	public String getJsonAst() {
		return jsonAst;
	}

	public void setJsonAst(final String jsonAst) {
		this.jsonAst = jsonAst;
	}

	public ParseLevel getParseLevel() {
		return parseLevel;
	}

	public void setParseLevel(final ParseLevel parseLevel) {
		this.parseLevel = parseLevel;
	}

	@Override
	public String toString() {
		final String comma = ", ";
		
		return "WikiPage(" 
				+ "url=" + url + comma
				+ "namespaceId=" + namespaceId + comma
				+ "title=" + title + comma
				+ "pageId=" + pageId +comma
				+ "revisionId=" + revisionId
				+ ")";
	}
	
}
