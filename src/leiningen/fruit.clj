(ns leiningen.fruit
  (:require [leiningen compile javac]
            [robert.hooke :as hooke]))

(defn compile-source
  "Compiles both Java and Clojure source files."
  [{java-only :java-only :as project} & args]
  (apply leiningen.javac/javac project args)
  (when-not java-only
    (leiningen.compile/compile project)))

(defn execute-subtask
  "Executes a subtask defined by `name` on the given project."
  [project name args]
  (case name
    "compile" (compile-source project)
    :else (println "Subtask is not recognized:" name)))

(defn classpath-hook
  [f {{:keys [robovm-path]} :ios :as project}]
  (conj (f project)
        (str robovm-path "/lib/robovm-rt.jar")
        (str robovm-path "/lib/robovm-objc.jar")
        (str robovm-path "/lib/robovm-cocoatouch.jar")))

(defn fruit
  "Provides a main entry point."
  [project & [cmd & args]]
  (hooke/add-hook #'leiningen.core.classpath/get-classpath #'classpath-hook)
  (execute-subtask project cmd args))
