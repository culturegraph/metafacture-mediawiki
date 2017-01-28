/**
 * Provides Metastream modules and auxiliary classes for processing wiki pages
 * from <a href="http://www.mediawiki.org/">MediaWikis</a> such as
 * <a href="http:/www.wikipedia.org/">Wikipedia</a>.
 * <p>
 * The Metastream modules in this package handle
 * {@link org.culturegraph.mf.mediawiki.objects.WikiPage} objects. These objects
 * contain basic meta data of wiki pages like page titles and page identifiers
 * as well as various representations of the wiki text.
 * {@link org.culturegraph.mf.mediawiki.objects.WikiPage} objects can be created
 * from a MediaWiki XML dump using the
 * {@link org.culturegraph.mf.mediawiki.WikiXmlHandler} module.
 * <p>
 * The different representations of the wiki text in a
 * {@link org.culturegraph.mf.mediawiki.objects.WikiPage} object are not generated
 * automatically, though; for instance the
 * {@link org.culturegraph.mf.mediawiki.WikiXmlHandler} module
 * only adds the plain wiki text of a page to the
 * {@link org.culturegraph.mf.mediawiki.objects.WikiPage} objects it produces.
 * Other representations can be created using the
 * {@link org.culturegraph.mf.mediawiki.WikiTextParser},
 * {@link org.culturegraph.mf.mediawiki.AstToJson} and
 * {@link org.culturegraph.mf.mediawiki.JsonToAst} modules.
 * <p>
 * At the heart of this package are the analyser modules in the
 * {@link org.culturegraph.mf.mediawiki.analyzers} sub-package. These modules
 * extract various information from a wiki page and make it available in the
 * Metafacture abstract record format. The
 * {@link org.culturegraph.mf.mediawiki.analyzers.MultiAnalyzer} module can be
 * used to simplify the construction of complex set-ups with multiple analysers
 * which require different representations of the wiki text.
 */
package org.culturegraph.mf.mediawiki;
