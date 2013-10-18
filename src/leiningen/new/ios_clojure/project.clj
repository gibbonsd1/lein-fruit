(defproject {{app-name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java"]
  :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"]
  :ios {:robovm-opts ["-forcelinkclasses" "{{package-prefix}}.**:clojure.**:org.robovm.cocoatouch.**"
                      "-frameworks" "UIKit:OpenGLES:QuartzCore:CoreGraphics:OpenAL:AudioToolbox:AVFoundation"
                      "-resources" "resources/**"

                      ; these properties will be inserted into Info.plist.xml
                      "-Papp.build=1"
                      "-Papp.id={{package}}"
                      "-Papp.executable={{app-name}}"
                      "-Papp.name={{app-name}}"]}
  :aot :all
  :main {{java-namespace}})
