(defproject {{app-name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :java-source-paths ["src"]
  :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"]
  :java-only true
  :ios {:robovm-opts ["-frameworks" "UIKit:OpenGLES:QuartzCore:CoreGraphics:OpenAL:AudioToolbox:AVFoundation"
                      "-resources" "resources/**"
                      "-plist" "Info.plist.xml"

                      ; these properties will be inserted into Info.plist.xml
                      "-Papp.build=1"
                      "-Papp.id={{package}}"
                      "-Papp.executable={{app-name}}"
                      "-Papp.name={{app-name}}"]}
  :aot :all
  :main {{namespace}})
