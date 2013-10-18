(ns leiningen.new.ios-clojure
  (:require [leiningen.new.templates :as t]))

(defn ios-clojure
  [name]
  (let [render (t/renderer "ios-clojure")
        main-ns (t/multi-segment (t/sanitize-ns name))
        utils-ns (str main-ns "-utils")
        package-name (t/sanitize main-ns)
        class-name "Main"
        java-ns (str package-name "." class-name)
        data {:app-name name
              :name (t/project-name name)
              :package package-name
              :class-name class-name
              :namespace main-ns
              :utils-namespace utils-ns
              :java-namespace java-ns
              :path (t/name-to-path main-ns)
              :utils-path (t/name-to-path utils-ns)
              :java-path (t/name-to-path java-ns)
              :year (t/year)}]
    (t/->files data
               ["project.clj" (render "project.clj" data)]
               ["src/clojure/{{path}}.clj" (render "core.clj" data)]
               ["src/clojure/{{utils-path}}.clj" (render "utils.clj" data)]
               ["src/java/{{java-path}}.java" (render "Main.java" data)]
               "resources")))
