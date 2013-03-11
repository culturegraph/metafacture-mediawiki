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
package org.culturegraph.mf.mediawiki.converter.xml;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.culturegraph.mf.exceptions.MetafactureException;
import org.culturegraph.mf.framework.DefaultXmlPipe;
import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.framework.XmlReceiver;
import org.culturegraph.mf.framework.annotations.Description;
import org.culturegraph.mf.framework.annotations.In;
import org.culturegraph.mf.framework.annotations.Out;
import org.culturegraph.mf.mediawiki.type.WikiPage;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Extracts wiki pages from a media wiki xml dump.
 * 
 * @author Christoph Böhme
 * 
 */
@Description("Extracts wiki pages from a media wiki xml dump.")
@In(XmlReceiver.class)
@Out(WikiPage.class)
public final class WikiXmlHandler extends
		DefaultXmlPipe<ObjectReceiver<WikiPage>> {

	private static final String ENCODING = "UTF-8";

	/**
	 * The XML tags which are used by WikiXmlHandler.
	 */
	private enum Tag {
		BASE, PAGE, TITLE, NS, ID, REVISION, TEXT, OTHER
	}

	private final Deque<Tag> stack = new LinkedList<Tag>();
	private final StringBuilder charData = new StringBuilder();
	
	private final Set<Integer> includeNamespaceIds = new HashSet<Integer>();
	
	private Matcher includePagesMatcher;

	private String baseURL;
	private WikiPage currentPage;
	private boolean includePage;

	public Set<Integer> getIncludeNamespaceIds() {
		return Collections.unmodifiableSet(includeNamespaceIds);
	}

	public void setIncludeNamespacesIds(final Collection<Integer> includeNamespaces) {
		this.includeNamespaceIds.clear();
		this.includeNamespaceIds.addAll(includeNamespaces);
	}

	public void setIncludeNamespaceIds(final String includeNamespaceIds) {
		final String[] stringIds = includeNamespaceIds.split("(,|\\s)");
		this.includeNamespaceIds.clear();
		for (String stringId: stringIds) {
			this.includeNamespaceIds.add(Integer.valueOf(Integer.parseInt(stringId.trim())));
		}
	}

	public String getIncludePagesPattern() {
		if (includePagesMatcher == null) {
			return null;
		}
		return includePagesMatcher.pattern().pattern();
	}

	public void setIncludePagesPattern(final String includePagesPattern) {
		if (includePagesPattern == null) {
			this.includePagesMatcher = null;
			return;
		}
		this.includePagesMatcher = Pattern.compile(includePagesPattern).matcher("");
	}

	@Override
	public void startDocument() {
		includePage = false;
		baseURL = null;
		stack.clear();
	}

	@Override
	public void startElement(final String uri, final String localName,
			final String qName, final Attributes atts) throws SAXException {

		final Tag tag = localNameToTag(localName);

		switch (tag) {
		case PAGE:
			currentPage = new WikiPage();
			includePage = true;
			break;
		case BASE:
		case TITLE:
		case NS:
		case ID:
		case TEXT:
			charData.delete(0, charData.length());
			break;
		default:
		}

		stack.push(tag);
	}

	@Override
	public void endElement(final String uri, final String localName,
			final String qName) throws SAXException {

		final Tag tag = localNameToTag(localName);

		stack.pop();

		switch (tag) {
		case PAGE:
			if (includePage) {
				getReceiver().process(currentPage);
			}
			includePage = false;
			break;
		case BASE:
			baseURL = extractBaseURL(charData.toString());
			break;
		case TITLE:
			handleTitleEndElement();
			break;
		case NS:
			handleNSEndElement();
			break;
		case ID:
			handleIdEndElement();
			break;
		case TEXT:
			if (includePage) {
				currentPage.setWikiText(charData.toString());
			}
			break;
		default:
		}
	}

	@Override
	public void characters(final char[] characters, final int start,
			final int length) throws SAXException {
		if (includePage || stack.peek() == Tag.BASE) {
			charData.append(characters, start, length);
		}
	}

	private void handleTitleEndElement() {
		if (!includePage) {
			return;
		}
		
		currentPage.setTitle(charData.toString());
		currentPage.setUrl(urlFromTitle(currentPage.getTitle()));

		if (includePagesMatcher != null) {
			includePagesMatcher.reset(currentPage.getTitle());
			includePage = includePagesMatcher.matches();
		}
	}
	
	private void handleNSEndElement() {
		if (!includePage) {
			return;
		}
		
		final int namespaceId = Integer.parseInt(charData.toString().trim());
		currentPage.setNamespaceId(namespaceId);
		
		if (!includeNamespaceIds.isEmpty()) {
			includePage = includeNamespaceIds.contains(Integer.valueOf(namespaceId));
		}
	}
	
	private void handleIdEndElement() {
		if (!includePage) {
			return;
		}

		switch (stack.peek()) {
		case PAGE:
			currentPage.setPageId(Long.parseLong(charData.toString().trim()));
			break;
		case REVISION:
			currentPage.setRevisionId(Long.parseLong(charData.toString().trim()));
			break;
		default:
		}
	}

	private Tag localNameToTag(final String localName) {
		try {
			return Tag.valueOf(localName.toUpperCase(Locale.ROOT));
		} catch (IllegalArgumentException e) {
			return Tag.OTHER;
		}
	}

	/**
	 * Cuts of the last component of the URL. This assumes that the URL in base
	 * points to the wiki's main page as it is the case in dumps of Wikipedia.
	 * 
	 * @param base
	 * @return
	 */
	private String extractBaseURL(final String base) {
		final String trimmedBase = base.trim();
		if (trimmedBase.isEmpty()) {
			return null;
		}
		return trimmedBase.replaceAll("/[^/]*$", "/");
	}

	/**
	 * Turns the title into a URL. This method will replace all spaces in the
	 * title with underscores, URL encode the title and attach it to the base
	 * URL.
	 * 
	 * @param title
	 * @return
	 */
	private String urlFromTitle(final String title) {
		if (baseURL == null) {
			return null;
		}
		try {
			return baseURL
					+ URLEncoder.encode(title.trim().replace(" ", "_"),
							ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new MetafactureException(e);
		}
	}

}
