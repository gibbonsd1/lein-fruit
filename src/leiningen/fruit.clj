(ns leiningen.fruit
  (:require [clojure.java.io :as io]
            [leiningen compile javac]
            [leiningen.core classpath eval]
            [robert.hooke :as hooke]))

(def ^:const robovm-exec "/bin/robovm")
(def ^:const robovm-jars
  ["/lib/robovm-rt.jar"
   "/lib/robovm-objc.jar"
   "/lib/robovm-cocoatouch.jar"])

(defn source-to-bytecode
  "Compiles the Java and Clojure source files into bytecode."
  [{java-only :java-only :as project}]
  (leiningen.javac/javac project)
  (when-not java-only
    (leiningen.compile/compile project)))

(defn run-robovm
  "Runs the RoboVM executable."
  [{{:keys [robovm-path robovm-opts]} :ios :as project} args]
  (->> [(str robovm-path robovm-exec)
        "-os" "ios" "-cp"
        (->> (leiningen.core.classpath/get-classpath project)
             (filter #(.exists (io/file %)))
             (clojure.string/join ":"))
        robovm-opts
        args
        (str (:main project))]
       flatten
       (remove nil?)
       (apply leiningen.core.eval/sh)))

(defn execute-subtask
  "Executes a subtask defined by `name` on the given project."
  [project name args]
  (case name
    "compile" (source-to-bytecode project)
    
    ; x86 tasks
    "create-x86" (run-robovm project ["-arch" "x86" args])
    "run" (run-robovm project ["-arch" "x86" "-run" args])
    "doall" (or (execute-subtask project "compile" args)
                (execute-subtask project "run" args))
    
    ; arm tasks
    "create-arm" (run-robovm project ["-arch" "thumbv7" args])
    "ipa" (run-robovm project ["-arch" "thumbv7" "-createipa" args])
    "release" (or (execute-subtask project "compile" args)
                  (execute-subtask project "ipa" args))
    
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
