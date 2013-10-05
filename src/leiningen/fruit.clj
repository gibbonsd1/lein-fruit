(ns leiningen.fruit
  (:require [leiningen compile javac]
            [leiningen.core.eval]
            [robert.hooke :as hooke]))

(def ^:const robovm-exec "/bin/robovm")
(def ^:const robovm-jars
  ["/lib/robovm-rt.jar"
   "/lib/robovm-objc.jar"
   "/lib/robovm-cocoatouch.jar"])

(defn source-to-bytecode
  "Compiles both Java and Clojure source files."
  [{java-only :java-only :as project} args]
  (apply leiningen.javac/javac project args)
  (when-not java-only
    (leiningen.compile/compile project)))

(defn bytecode-to-native
  "Compiles the bytecode into native code."
  [{{:keys [robovm-path]} :ios :as project} args]
  (->> [(str robovm-path robovm-exec)
        "-arch"
        "x86"
        "-os"
        "ios"
        "-cp"
        (->> (for [path robovm-jars] (str robovm-path path))
             (clojure.string/join ":")
             (str (:target-path project) "/classes:"))
        args
        (str (:main project))]
       flatten
       (remove nil?)
       (apply leiningen.core.eval/sh)))

(defn execute-subtask
  "Executes a subtask defined by `name` on the given project."
  [project name args]
  (case name
    "compile" (source-to-bytecode project args)
    "create-native" (bytecode-to-native project args)
    :else (println "Subtask is not recognized:" name)))

(defn classpath-hook
  "Adds the RoboVM paths to the classpath."
  [f {{:keys [robovm-path]} :ios :as project}]
  (reduce (fn [paths path]
            (conj paths (str robovm-path path)))
          (f project)
          robovm-jars))

(defn fruit
  "Provides a main entry point."
  [project & [cmd & args]]
  (hooke/add-hook #'leiningen.core.classpath/get-classpath #'classpath-hook)
  (execute-subtask project cmd args))
