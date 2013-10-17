## Introduction

A Leiningen plugin for building native iOS apps in Clojure and Java using the [RoboVM](http://www.robovm.org) bytecode-to-native translator.

## Usage

1. You must be running Mac OS X with Xcode installed (tested with 4.6.3 but the latest might also work).
2. Download and extract [robovm-0.0.5.tar.gz](http://download.robovm.org) somewhere.
3. Install [Leiningen](https://github.com/technomancy/leiningen) and create or modify `~/.lein/profiles.clj`:
	- Here's what mine looks like:
    {:user {
        :plugins [[lein-fruit "0.1.0-SNAPSHOT"]]
        :ios {:robovm-path "/absolute/path/to/robovm-0.0.5"}
    }}

## Licensing

All source files that originate from this project are dedicated to the public domain. I would love pull requests, and will assume that any Clojure contributions are also dedicated to the public domain.
