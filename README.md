## Introduction

A Leiningen plugin for building native iOS apps in Clojure and Java using the [RoboVM](http://www.robovm.org) bytecode-to-native translator. It is modeled after [lein-droid](https://github.com/clojure-android/lein-droid) and the commands are similar.

## Caveats

1. This plugin has not been well-tested on actual iOS devices because...I don't own any. I'm an Android guy (go figure).
2. There is no REPL and any attempt to call eval at runtime will cause an exception because iOS doesn't allow executable memory.
3. Due to Clojure's [import behavior](https://groups.google.com/d/msg/clojure/tWSEsOk_pM4/y7kDQpEV-1gJ), importing most [RoboVM Cocoa Touch classes](https://github.com/robovm/robovm/tree/master/cocoatouch/src/main/java/org/robovm/cocoatouch) will lead to a compile error. Instead, you must invoke these classes dynamically. The template provides an example of this, including some helpful convenience functions.

## Installation

1. Get a computer running Mac OS X
2. Install Xcode (tested with 4.6.3)
3. Install [JDK 7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)
4. Download and extract [robovm-0.0.5.tar.gz](http://download.robovm.org/robovm-0.0.5.tar.gz)
5. Install [Leiningen](https://github.com/technomancy/leiningen), and create or modify `~/.lein/profiles.clj` so it looks like this:

```clojure
{:user {
    :plugins [[lein-fruit "x.x.x"]]
    :ios {:robovm-path "/absolute/path/to/robovm-0.0.5"}
}}
```

Replace the "x.x.x" with the version below:
![](https://clojars.org/lein-fruit/latest-version.svg)

## Usage

```bash
# Create a new Clojure/iOS project
lein fruit new hello-world
# ...or a new Java/iOS project
lein fruit new-java hello-world
# You may optionally specify a package name
lein fruit new-java hello-world info.oakleaf.hello_world

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

All source files that originate from this project are dedicated to the public domain. I would love pull requests, and will assume that they are also dedicated to the public domain.
