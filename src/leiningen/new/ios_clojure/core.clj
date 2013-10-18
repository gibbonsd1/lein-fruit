(ns {{namespace}}
  (:require [{{utils-namespace}} :as u]))

(def window (atom nil))

(defn init
  []
  (let [main-screen (u/static-method :uikit.UIScreen :getMainScreen)
        button-type (u/static-field :uikit.UIButtonType :RoundedRect)
        button (u/static-method :uikit.UIButton :fromType button-type)
        normal-state (u/static-field :uikit.UIControlState :Normal)
        click-count (atom 0)]
    (doto button
      (.setFrame (u/init-class :coregraphics.CGRect 115 121 91 37))
      (.setTitle "Click me!" normal-state)
      (.addOnTouchUpInsideListener
        (proxy [org.robovm.cocoatouch.uikit.UIControl$OnTouchUpInsideListener] []
          (onTouchUpInside [control event]
            (.setTitle button (str "Click #" (swap! click-count inc)) normal-state)))))
    (reset! window (u/init-class :uikit.UIWindow (.getBounds main-screen)))
    (doto @window
      (.setBackgroundColor (u/static-method :uikit.UIColor :lightGrayColor))
      (.addSubview button)
      .makeKeyAndVisible)))

