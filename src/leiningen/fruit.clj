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
  "Compiles the Java and Clojure source files into bytecode."
  [{java-only :java-only :as project}]
  (leiningen.javac/javac project)
  (when-not java-only
    (leiningen.compile/compile project)))

(defn run-robovm
  "Runs the RoboVM executable."
  [{{:keys [robovm-path robovm-args]} :ios :as project} args]
  (->> [(str robovm-path robovm-exec)
        "-os" "ios" "-cp"
        (->> (for [path robovm-jars] (str robovm-path path))
             (clojure.string/join ":")
             (str (:target-path project) "/classes:"))
        robovm-args
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
    "create-x86" (run-robovm project ["-arch" "x86" args])
    "create-arm" (run-robovm project ["-arch" "thumbv7" args])
    "ipa" (run-robovm project ["-arch" "thumbv7" "-createipa" args])
    "run" (run-robovm project ["-arch" "x86" "-run" args])
    "install" (run-robovm project ["-arch" "thumbv7" "-run" args])
    "build" (doseq [task ["compile" "create-x86"]]
              (execute-subtask project task args))
    "release" (doseq [task ["compile" "ipa"]]
                (execute-subtask project task args))
    "doall" (doseq [task ["compile" "run"]]
              (execute-subtask project task args))
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
