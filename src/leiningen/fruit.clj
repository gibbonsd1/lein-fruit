(ns leiningen.fruit
  (:require [leiningen compile javac]
            [robert.hooke :as hooke]))

(def ^:const robovm-paths
  ["/lib/robovm-rt.jar"
   "/lib/robovm-objc.jar"
   "/lib/robovm-cocoatouch.jar"])

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
  "Adds the robovm paths to the classpath."
  [f {{:keys [robovm-path]} :ios :as project}]
  (reduce (fn [paths path]
            (conj paths (str robovm-path path)))
          (f project)
          robovm-paths))

(defn fruit
  "Provides a main entry point."
  [project & [cmd & args]]
  (hooke/add-hook #'leiningen.core.classpath/get-classpath #'classpath-hook)
  (execute-subtask project cmd args))
