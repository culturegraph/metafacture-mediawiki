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

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.culturegraph.mf.exceptions.MetafactureException;
import org.culturegraph.mf.exceptions.ShouldNeverHappenException;
import org.culturegraph.mf.framework.DefaultObjectPipe;
import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.framework.annotations.Description;
import org.culturegraph.mf.framework.annotations.In;
import org.culturegraph.mf.framework.annotations.Out;
import org.culturegraph.mf.mediawiki.type.WikiPage;
import org.sweble.wikitext.engine.CompiledPage;
import org.sweble.wikitext.engine.Compiler;
import org.sweble.wikitext.engine.CompilerException;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.config.WikiConfigurationInterface;
import org.sweble.wikitext.engine.utils.SimpleWikiConfiguration;
import org.sweble.wikitext.lazy.LinkTargetException;

/**
 * Parses a wiki page and adds the AST to the {@link WikiPage} object.
 * 
 * The parser does not support expansion (inclusion of template contents)
 * based on the assumption that the page should already contain all 
 * information. Templates can only transform this information into a 
 * different form but cannot add new knowledge which is not somehow
 * already part of the page.
 * 
 * @author Christoph Böhme
 * 
 */
@Description("Parses a wiki page and adds the AST to the {@link WikiPage} object.")
@In(WikiPage.class)
@Out(WikiPage.class)
public final class WikiTextParser 
		extends DefaultObjectPipe<WikiPage, ObjectReceiver<WikiPage>> {

	/**
	 * Processing levels of the Wiki parser. See http://sweble.org/downloads/diwp-preprint.pdf
	 * for an explanation of the different levels.
	 */
	public enum ParseLevel { PREPROCESS, PARSE, POSTPROCESS };
	
	public static final String DEFAULT_CONFIG = "classpath:/org/sweble/wikitext/engine/SimpleWikiConfiguration.xml";

	private ParseLevel parseLevel = ParseLevel.PREPROCESS;

	private final WikiConfigurationInterface config;
	private final Compiler compiler;

	public WikiTextParser() throws IOException {
		this(DEFAULT_CONFIG);
	}
	
	public WikiTextParser(final String configFile) throws IOException {
		super();
		try {
			config = new SimpleWikiConfiguration(configFile);
		} catch (JAXBException e) {
			throw new MetafactureException(e);
		}
		compiler = new Compiler(config);
	}

	public ParseLevel getParseLevel() {
		return parseLevel;
	}

	public void setParseLevel(final ParseLevel parseLevel) {
		this.parseLevel = parseLevel;
	}

	@Override
	public void process(final WikiPage page) {
		final CompiledPage compiledPage;
		try {
			final PageTitle pageTitle = PageTitle.make(config, page.getTitle());
			final PageId pageId = new PageId(pageTitle, page.getRevisionId());
			switch(parseLevel) {
			case PREPROCESS:
				compiledPage = compiler.preprocess(pageId, page.getWikiText(),
						false, null);
				break;
			case PARSE:
				compiledPage = compiler.parse(pageId, page.getWikiText(), null);
				break;
			case POSTPROCESS:
				compiledPage = compiler.postprocess(pageId, page.getWikiText(), null);
				break;
			default:
				throw new ShouldNeverHappenException("Illegal value for parseLevel");
			}
		} catch (LinkTargetException e) {
			throw new MetafactureException(e);
		} catch (CompilerException e) {
			throw new MetafactureException(e);
		}
		
		page.setWikiAst(compiledPage);
		page.setParseLevel(parseLevel);
		getReceiver().process(page);
	}
	
}
