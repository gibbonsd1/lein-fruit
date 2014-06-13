(ns leiningen.fruit
  (:require [clojure.java.io :as io]
            [leiningen compile javac new]
            [leiningen.core classpath eval main]
            [robert.hooke :as hooke]))

(def ^:const robovm-compiler "/lib/robovm-compiler.jar")
(def ^:const robovm-libs ["/lib/robovm-rt.jar"
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
  [{{:keys [robovm-path robovm-opts]} :ios version :version :as project} args]
  (when-not robovm-path
    (leiningen.core.main/abort "You need to set the RoboVM path."))
  (->> [(or (:java-cmd project) (System/getenv "JAVA_CMD") "java")
        "-Xmx4096m" "-Xss1024k"
        "-jar" (str robovm-path robovm-compiler)
        "-verbose" "-home" robovm-path "-os" "ios" "-cp"
        (->> (leiningen.core.classpath/get-classpath project)
             (filter #(.exists (io/file %)))
             (clojure.string/join ":"))
        "-plist" "Info.plist.xml"
        (when version (str "-Papp.version=" version))
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
    "new" (if-let [arg (first args)]
            (leiningen.new/new {} "ios-clojure" arg (second args))
            (println "Must provide a project name after `new`."))
    "new-java" (if-let [arg (first args)]
                 (leiningen.new/new {} "ios-java" arg (second args))
                 (println "Must provide a project name after `new-java`."))
    "compile" (source-to-bytecode project)
    "help" (run-robovm project ["-help"])
    
    ; x86 tasks
    "create-x86" (run-robovm project ["-arch" "x86" args])
    "run" (run-robovm project ["-arch" "x86" "-run" args])
    "doall" (or (execute-subtask project "compile" args)
                (execute-subtask project "run" args))
    
    ; arm tasks
    "create-arm" (run-robovm project ["-arch" "thumbv7" args])
    "create-ipa" (run-robovm project ["-arch" "thumbv7" "-createipa" args])
    "run-ipa" (run-robovm project ["-arch" "thumbv7" "-run" args])
    "ipa" (or (execute-subtask project "create-ipa" args)
              (execute-subtask project "run-ipa" args))
    "release" (or (execute-subtask project "compile" args)
                  (execute-subtask project "ipa" args))
    
    (println "Subtask is not recognized:" name)))

(defn classpath-hook
  "Adds the RoboVM paths to the classpath."
  [f {{:keys [robovm-path]} :ios :as project}]
  (reduce (fn [paths path]
            (conj paths (str robovm-path path)))
          (f project)
          robovm-libs))

(defn ^{:no-project-needed true} fruit
  "Provides a main entry point."
  [project & [cmd & args]]
  (hooke/add-hook #'leiningen.core.classpath/get-classpath #'classpath-hook)
  (execute-subtask project cmd args))
