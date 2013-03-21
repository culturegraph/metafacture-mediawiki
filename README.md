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

_The Metafacture-Mediawiki plugin will soon be available for download on the [Culturegraph Software Website](http://culturegraph.github.com)_

### Java Library Usage

_Metafacture-Mediawiki release will soon be available on [Maven Central](http://search.maven.org/)_

Development snapshots are distributed via [Sonatype OSS](http://oss.sonatype.org/). To use the snapshots add the Sonatype repository and the Metafacture-Mediawiki dependency to your project's POM:

```xml
<repositories>
    <repository>
        <id>sonatype-nexus-snapshots</id>
        <name>Sonatype Nexus Snapshots</name>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.culturegraph</groupId>
        <artifactId>metafacture-mediawiki</artifactId>
        <version>0.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## Documentation

The documentation of Metafacture-Mediawiki can be found in the [Wiki](https://github.com/culturegraph/metafacture-mediawiki/wiki).

## License

Copyright 2013 Deutsche Nationalbibliothek.

Metafacture-Mediawiki is distributed under the [Apache 2.0 License]( http://www.apache.org/licenses/LICENSE-2.0).
