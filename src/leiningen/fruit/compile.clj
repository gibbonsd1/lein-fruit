(ns leiningen.fruit.compile
  (:refer-clojure :exclude [compile])
  (:require [leiningen compile javac]))

(defn compile
  "Compiles both Java and Clojure source files."
  [{java-only :java-only :as project} & args]
  (apply leiningen.javac/javac project args)
  (when-not java-only
    (leiningen.compile/compile project)))
