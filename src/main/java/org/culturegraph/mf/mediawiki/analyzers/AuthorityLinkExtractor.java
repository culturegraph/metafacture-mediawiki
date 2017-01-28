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

/**
 *
 * authority link extraction for wikitext (GND, LOC, IMDB, VIAF).
 *
 * @author Markus Michael Geipel
 *
 */
@Description("Authority link extraction for wikitext (GND, LOC, IMDB, VIAF).")
@In(WikiPage.class)
@Out(StreamReceiver.class)
public final class AuthorityLinkExtractor extends DefaultObjectPipe<WikiPage, StreamReceiver>
implements Analyzer {
	//{{IMDb Name|0096566}}
	//{{IMDb Name|ID=0000631}}
	//{{IMDb Name|ID=xxx|NAME=xxx}}
//	{{Normdaten|TYP=p|GND=118514768|LCCN=n/79/18801|NDL=00434255|VIAF=2467372}}
	private static final Pattern IMDB_PATTERN = Pattern.compile("\\{\\{\\s*imdb\\s*name\\s*\\|(?:id=)?([^|}]*)[^}]*\\}\\}", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
	private static final Pattern AUTHORITY_PATTERN = Pattern.compile("\\{\\{\\s*normdaten\\s*\\|([^}]*)\\}\\}", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
	private static final Pattern ENTRY_PATTERN = Pattern.compile("|", Pattern.LITERAL);
	private static final Pattern KEYVALUE_PATTERN = Pattern.compile("(.*)\\s*=\\s*(.*)");

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

		final String wikiText = page.getWikiText();
		final Matcher authMatcher = AUTHORITY_PATTERN.matcher(wikiText);
		if(authMatcher.find()){
			final String[] entries = ENTRY_PATTERN.split(authMatcher.group(1));
			for (String entry : entries) {
					final Matcher matcher2 = KEYVALUE_PATTERN.matcher(entry.trim());
					if(matcher2.find()){
						getReceiver().literal(matcher2.group(1).toLowerCase(), matcher2.group(2));
					}
			}
		}
		final Matcher imdbMatcher = IMDB_PATTERN.matcher(wikiText);
		if(imdbMatcher.find()){
			getReceiver().literal("imdb", imdbMatcher.group(1));
		}
	}

}
