# Metafacture-Mediawiki

Data extraction from MediaWiki pages made easy.

## About Metafacture-Mediawiki 

Metafacture-Mediawiki is a plugin for [Metafacture](https://github.com/culturegraph/Metafacture). It provides modules for extracting information from [MediaWiki](http://mediawiki.org/) pages such as [Wikipedia](http://wikipedia.org/) articles. The plugin relies on the terrific [Sweble](http://sweble.org/) Wikitext parser for parsing the MediaWiki markup. 

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