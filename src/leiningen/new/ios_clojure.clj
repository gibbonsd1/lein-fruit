(ns leiningen.new.ios-clojure
  (:require [leiningen.new.templates :as t]))

(defn ios-clojure
  [name]
  (let [render (t/renderer "ios-clojure")
        main-ns (t/multi-segment (t/sanitize-ns name))
        package-name (t/sanitize main-ns)
        class-name "Main"
        java-ns (str package-name "." class-name)
        data {:app-name name
              :name (t/project-name name)
              :package package-name
              :class-name class-name
              :namespace main-ns
              :java-namespace java-ns
              :path (t/name-to-path main-ns)
              :java-path (t/name-to-path java-ns)
              :year (t/year)}]
    (t/->files data
               ["project.clj" (render "project.clj" data)]
               ["src/java/{{java-path}}.java" (render "Main.java" data)]
               "src/clojure"
               "resources")))
