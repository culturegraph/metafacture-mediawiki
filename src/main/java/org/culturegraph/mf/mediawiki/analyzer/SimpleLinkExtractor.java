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

import org.culturegraph.mf.framework.DefaultObjectPipe;
import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.framework.annotations.Description;
import org.culturegraph.mf.framework.annotations.In;
import org.culturegraph.mf.framework.annotations.Out;
import org.culturegraph.mf.mediawiki.converter.WikiTextParser.ParseLevel;
import org.culturegraph.mf.mediawiki.type.WikiPage;

/**
 * 
 * very simple but efficient link extraction for wikitext.
 * 
 * @author Markus Michael Geipel
 *
 */
@Description("Very simple but efficient link extraction for wikitext.")
@In(WikiPage.class)
@Out(StreamReceiver.class)
public final class SimpleLinkExtractor extends DefaultObjectPipe<WikiPage, StreamReceiver>
implements Analyzer {

	@Override
	public boolean wikiTextOnly() {
		return true;
	}

	@Override
	public ParseLevel requiredParseLevel() {
		return null;
	}
	
	@Override
	public void process(final WikiPage page) {
		
		//getReceiver().startRecord(Long.toString(page.getPageId()));
		final String wikiText = page.getWikiText();
		
		int start = 0;

		while (true) {
			start = wikiText.indexOf("[[", start);

			if (start < 0) {
				break;
			}

			final int end = wikiText.indexOf("]]", start);

			if (end < 0) {
				break;
			}

			String linkText = wikiText.substring(start + 2, end);

			// skip empty links
			if (linkText.length() == 0) {
				start = end + 1;
				continue;
			}

			// skip special links
			//if (excludeSpecialLinks && linkText.indexOf(':') != -1) {
			//	start = end + 1;
			//	continue;
			//}

			// if there is anchor text, get only article title
			int tmp;
			tmp = linkText.indexOf('|');
			if (tmp != -1) {
				linkText = linkText.substring(0, tmp);
			}
			tmp = linkText.indexOf('#');
			if (tmp != -1) {
				linkText = linkText.substring(0, tmp);
			}

			// ignore article-internal links, e.g., [[#section|here]]
			if (linkText.length() == 0) {
				start = end + 1;
				continue;
			}

			getReceiver().literal("ref", linkText.trim());

			start = end + 1;
		}
		//getReceiver().endRecord();
	}
	

}
