/**
 * Provides Metastream modules and auxiliary classes for processing wiki pages
 * from <a href="http://www.mediawiki.org/">MediaWikis</a> such as 
 * <a href="http:/www.wikipedia.org/">Wikipedia</a>.
 * 
 * The Metastream modules in this package handle {@link WikiPage} objects.
 * These objects contain basic meta data of wiki pages like page titles and
 * page identifiers as well as various representations of the wiki text.
 * {@link WikiPage} objects can be created from a MediaWiki XML dump using the
 * {@link WikiXmlHandler} module. 
 * 
 * The different representations of the wiki text in a {@link WikiPage} object
 * are not generated automatically, though; for instance the
 * {@link WikiXmlHandler} module only adds the plain wiki text of a page to the
 * {@link WikiPage} objects it produces. Other representations can be created
 * using the {@WikiTextParser}, {@AstToJson} and {@JsonToAst} modules.
 * 
 * At the heart of this package are the analyser modules in the 
 * {@link org.culturegraph.mf.mediawiki.analyzer} sub-package. These modules extract
 * various information from a wiki page and make it available in the Metastream
 * abstract record format. The {@link MultiAnalyzer} module can be used to 
 * simplify the construction of complex set-ups with multiple analysers which
 * require different representations of the wiki text.
 */
package org.culturegraph.mf.mediawiki;

