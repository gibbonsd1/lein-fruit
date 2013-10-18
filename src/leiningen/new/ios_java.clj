(ns leiningen.new.ios-java
  (:require [leiningen.new.templates :as t]))

(defn ios-java
  [name package-name]
  (let [render (t/renderer "ios-java")
        class-name "Main"
        package-name (t/sanitize (t/multi-segment (or package-name name)))
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
               ["Info.plist.xml" (render "Info.plist.xml" data)]
               ["src/{{path}}.java" (render "Main.java" data)]
               "resources")))
