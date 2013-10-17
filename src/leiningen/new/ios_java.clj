(ns leiningen.new.ios-java
  (:require [leiningen.new.templates :as t]))

(defn ios-java
  [name]
  (let [render (t/renderer "ios-java")
        package-name (t/multi-segment (t/sanitize name))
        class-name "Main"
        main-ns (str package-name "." class-name)
        data {:app-name name
              :name (t/project-name name)
              :package package-name
              :class-name class-name
              :namespace main-ns
              :path (t/name-to-path main-ns)
              :year (t/year)}]
    (t/->files data
               ["project.clj" (render "project.clj" data)]
               ["src/{{path}}.java" (render "Main.java" data)]
               "resources")))
