## Introduction

A Leiningen plugin for building native iOS apps in Clojure and Java using the [RoboVM](http://www.robovm.org) bytecode-to-native translator. It is modeled after [lein-droid](https://github.com/clojure-android/lein-droid) and the commands are similar.

## Caveats

This plugin has not been well-tested on actual iOS devices because...I don't own any. I'm an Android guy (go figure). Also, keep in mind that, due to Clojure's weird import behavior, importing most [RoboVM Cocoa Touch classes](https://github.com/robovm/robovm/tree/master/cocoatouch/src/main/java/org/robovm/cocoatouch) will lead to a compile error. Instead, you must invoke these classes dynamically. The template provides an example of this, including some helpful convenience functions.

## Installation

1. You must be running Mac OS X with Xcode installed (tested with 4.6.3 but the latest might also work)
2. Download and extract [robovm-0.0.5.tar.gz](http://download.robovm.org/robovm-0.0.5.tar.gz) somewhere
3. Install [Leiningen](https://github.com/technomancy/leiningen) and create or modify `~/.lein/profiles.clj`
	- Here's what mine looks like:
    {:user {
        :plugins [[lein-fruit "0.1.0-SNAPSHOT"]]
        :ios {:robovm-path "/absolute/path/to/robovm-0.0.5"}
    }}

## Usage

```bash
# Create a new Clojure/iOS project
lein fruit new hello-world
# ...or a new Java/iOS project
lein fruit new-java hello-world
# Go inside the project
cd hello-world

# Build an x86 version and run in a simulator
lein fruit doall
# ...which is the same thing as
lein fruit compile && lein fruit run
# Use the iPad simulator
lein fruit doall -ios-sim-family ipad

# Build an ARM version and run on a device
lein fruit release
# ..which is the same thing as
lein fruit compile && lein fruit ipa
# Show RoboVM flags (all flags are passed to it)
lein fruit help
```

## Licensing

All source files that originate from this project are dedicated to the public domain. I would love pull requests, and will assume they are also dedicated to the public domain.
