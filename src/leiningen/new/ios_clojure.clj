(ns leiningen.new.ios-clojure
  (:require [leiningen.new.templates :as t]))

(defn ios-clojure
  [name package-name]
  (let [render (t/renderer "ios-clojure")
        java-render (t/renderer "ios-java")
        class-name "Main"
        package-name (t/sanitize (t/multi-segment (or package-name name)))
        package-prefix (->> (.lastIndexOf package-name ".")
                            (subs package-name 0))
        main-ns (t/sanitize-ns package-name)
        java-ns (str package-name "." class-name)
        data {:app-name name
              :name (t/project-name name)
              :package package-name
              :package-prefix package-prefix
              :class-name class-name
              :namespace main-ns
              :java-namespace java-ns
              :path (t/name-to-path main-ns)
              :java-path (t/name-to-path java-ns)
              :year (t/year)}]
    (t/->files data
               ["project.clj" (render "project.clj" data)]
               ["Info.plist.xml" (java-render "Info.plist.xml" data)]
               ["src/clojure/{{path}}.clj" (render "core.clj" data)]
               ["src/java/{{java-path}}.java" (render "Main.java" data)]
               "resources")))
