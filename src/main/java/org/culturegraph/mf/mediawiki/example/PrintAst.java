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
package org.culturegraph.mf.mediawiki.example;

import java.io.IOException;

import org.culturegraph.mf.mediawiki.converter.WikiTextParser;
import org.culturegraph.mf.mediawiki.converter.WikiTextParser.ParseLevel;
import org.culturegraph.mf.mediawiki.converter.xml.WikiXmlHandler;
import org.culturegraph.mf.mediawiki.sink.AstWriter;
import org.culturegraph.mf.stream.converter.xml.XmlDecoder;
import org.culturegraph.mf.stream.source.HttpOpener;

/**
 * Parses a wiki page and prints the AST.
 * 
 * @author Christoph Böhme
 */
public final class PrintAst {

	private static final String BASE_URL = "http://de.wikipedia.org/w/index.php?title=Spezial:Exportieren/";
	private static final String DEFAULT_PAGE = "Deutsche_Nationalbibliothek";

	private PrintAst() {
		// Nothing to do
	}
	
	public static void main(final String[] args) throws IOException {
		final String pageUrl;
		if (args.length > 0) {
			pageUrl = BASE_URL + args[0];
		} else {
			pageUrl = BASE_URL + DEFAULT_PAGE;
		}
		
		final HttpOpener opener = new HttpOpener();
		final XmlDecoder xmlDecoder = new XmlDecoder();
		final WikiXmlHandler xmlHandler = new WikiXmlHandler();
		xmlHandler.setIncludeNamespaceIds("0");
		final WikiTextParser wikiParser = new WikiTextParser("classpath:/parser-config/WikipediaDE.xml");
		wikiParser.setParseLevel(ParseLevel.POSTPROCESS);
		final AstWriter writer = new AstWriter();
		
		opener.setReceiver(xmlDecoder)
				.setReceiver(xmlHandler)
				.setReceiver(wikiParser)
				.setReceiver(writer);
		
		opener.process(pageUrl);
		opener.closeStream();
	}

}
