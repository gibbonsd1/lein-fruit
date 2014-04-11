(ns {{namespace}}
  (:import [org.robovm.apple.coregraphics CGRect]
           [org.robovm.apple.uikit UIButton UIButtonType UIColor
            UIControl$OnTouchUpInsideListener UIControlState UIScreen UIWindow]))

(def window (atom nil))

(defn init
  []
  (let [button (UIButton/create UIButtonType/RoundedRect)
        normal UIControlState/Normal
        click-count (atom 0)]
    (doto button
      (.setFrame (CGRect. 115 121 91 37))
      (.setTitle "Click me!" normal)
      (.addOnTouchUpInsideListener
        (proxy [UIControl$OnTouchUpInsideListener] []
          (onTouchUpInside [control event]
            (.setTitle button (str "Click #" (swap! click-count inc)) normal)))))
    (reset! window (UIWindow. (.getBounds (UIScreen/getMainScreen))))
    (doto @window
      (.setBackgroundColor (UIColor/colorLightGray))
      (.addSubview button)
      .makeKeyAndVisible)))

