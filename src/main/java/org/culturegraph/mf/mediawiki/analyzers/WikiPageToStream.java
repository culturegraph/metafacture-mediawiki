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

import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.framework.annotations.Description;
import org.culturegraph.mf.framework.annotations.In;
import org.culturegraph.mf.framework.annotations.Out;
import org.culturegraph.mf.framework.helpers.DefaultObjectPipe;
import org.culturegraph.mf.mediawiki.WikiTextParser.ParseLevel;
import org.culturegraph.mf.mediawiki.objects.WikiPage;

/**
 * A simple analyser that turns the contents of a WikiPage object
 * as stream events.
 *
 * @author Christoph Böhme
 *
 */
@Description("A simple analyser that turns the contents of a WikiPage object as stream events.")
@In(WikiPage.class)
@Out(StreamReceiver.class)
public final class WikiPageToStream
		extends DefaultObjectPipe<WikiPage, StreamReceiver>
		implements Analyzer {

	public static final String PAGEID_LITERAL = "PAGE_ID";
	public static final String REVISIONID_LITERAL = "REVISION_ID";
	public static final String URL_LITERAL = "URL";
	public static final String TITLE_LITERAL = "PAGETITLE";
	public static final String WIKITEXT_LITERAL = "WIKITEXT";

	private boolean outputWikiText;

	public WikiPageToStream() {
		super();
	}

	public WikiPageToStream(final boolean outputWikiText) {
		super();
		this.setOutputWikiText(outputWikiText);
	}

	public boolean isOutputWikiText() {
		return outputWikiText;
	}

	public void setOutputWikiText(final boolean outputWikiText) {
		this.outputWikiText = outputWikiText;
	}

	@Override
	public void process(final WikiPage wikiPage) {
		getReceiver().startRecord(Long.toString(wikiPage.getPageId()));
		getReceiver().literal(PAGEID_LITERAL, Long.toString(wikiPage.getPageId()));
		getReceiver().literal(REVISIONID_LITERAL, Long.toString(wikiPage.getRevisionId()));
		getReceiver().literal(URL_LITERAL, wikiPage.getUrl());
		getReceiver().literal(TITLE_LITERAL, wikiPage.getTitle());
		if (outputWikiText) {
			getReceiver().literal(WIKITEXT_LITERAL, wikiPage.getWikiText());
		}
		getReceiver().endRecord();
	}

	@Override
	public boolean wikiTextOnly() {
		return true;
	}

	@Override
	public ParseLevel requiredParseLevel() {
		return null;
	}

}
