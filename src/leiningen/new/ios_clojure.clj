(ns leiningen.new.ios-clojure
  (:require [leiningen.new.templates :as t]))

(defn ios-clojure
  [name]
  (let [render (t/renderer "ios-clojure")
        package-name (t/multi-segment (t/sanitize name))
        class-name "IOSLauncher"
        main-ns (str package-name "." class-name)
        data {:app-name name
              :name (t/project-name name)
              :class-name class-name
              :package package-name
              :namespace main-ns
              :path (t/name-to-path main-ns)
              :year (t/year)}]
    (t/->files data
               ["project.clj" (render "project.clj" data)]
               ["src/java/{{path}}.java" (render "IOSLauncher.java" data)]
               "src/clojure"
               "resources")))
