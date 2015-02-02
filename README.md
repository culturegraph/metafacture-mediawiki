# Metafacture-Mediawiki

Data extraction from MediaWiki pages made easy.

![Build Status](https://travis-ci.org/culturegraph/metafacture-mediawiki.png?branch=master)

## About Metafacture-Mediawiki 

Metafacture-Mediawiki is a plugin for [Metafacture](https://github.com/culturegraph/metafacture-core). It provides modules for extracting information from MediaWiki pages such as Wikipedia articles. Currently, modules for extracting links and templates exist. Adding new extraction modules is easy.

The plugin relies on the excellent [Sweble wikitext parser](http://sweble.org/) for parsing wikitext into abstract syntax trees.

### Key Features

* Extracts basic metadata information about pages from MediaWiki xml documents
* Extracts simple information from wikitext using regular expressions (fast but not suitable for complex tasks)
* Wraps the [Sweble wikitext parser](http://sweble.org/) for conveniently parsing wikitext into an abstract syntax tree within a [Flux](https://github.com/culturegraph/metafacture-core/wiki#flux) flow
* Extracts links and templates from abstract syntax trees created by [Sweble](http://sweble.org/) and turns them into a Metafacture event stream
* Makes writing additional extraction modules easy
* Supports running multiple extraction modules hassle-free

## Download and Install

Metafacture-Mediawiki can be used as a plugin in the Metafacture distribution or as a Java library in your own programs.

### Plugin Usage

The plugin can be downloaded on the [releases](https://github.com/culturegraph/metafacture-mediawiki/releases) page. Drop this plugin jar into the `/plugins` folder of the [metafacture-runner](https://github.com/culturegraph/metafacture-runner) to use the plugin.

### Java Library Usage

Metafacture-Mediawiki is available on [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.culturegraph%22). To use it, add the following dependency declaration to your pom.xml:

```xml
<dependency>
    <groupId>org.culturegraph</groupId>
    <artifactId>metafacture-mediawiki</artifactId>
    <version>3.0.0</version>
</dependency>
```

Additionally, you need to add the metafacture-core package as a dependency:

```xml
<dependency>
    <groupId>org.culturegraph</groupId>
    <artifactId>metafacture-core</artifactId>
    <version>3.0.0</version>
</dependency>
```

Our integration server automatically publishes successful builds of the master branch as snapshot versions on [Sonatype OSS Repository](https://oss.sonatype.org/index.html#nexus-search;quick%7Eculturegraph).

## Documentation

The documentation of Metafacture-Mediawiki can be found in the [Wiki](https://github.com/culturegraph/metafacture-mediawiki/wiki).

## License

Copyright 2013, 2015 Deutsche Nationalbibliothek.

Metafacture-Mediawiki is distributed under the [Apache 2.0 License]( http://www.apache.org/licenses/LICENSE-2.0).
