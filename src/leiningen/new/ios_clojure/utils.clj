(ns {{utils-namespace}})

(defn- types-match?
  [types args]
  (and (= (count types) (count args))
       (->> (for [i (range (count types))]
              (let [parent (get types i)
                    child (type (get args i))]
                (or (nil? child) (.isAssignableFrom parent child))))
            (filter true?)
            count
            (= (count types)))))

(defn- get-object
  [objects args]
  (-> #(types-match? (.getParameterTypes %) args)
      (filter objects)
      first))

(defn- get-obj-constructor
  [class-name args]
  (-> (Class/forName class-name)
      .getConstructors
      (get-object args)))

(defn- get-obj-method
  [class-name method-name args]
  (-> (->> (Class/forName class-name)
           .getMethods
           (filter #(= method-name (.getName %))))
      (get-object args)))

(defn- parse-name
  [class-name]
  (if (keyword? class-name)
    (str "org.robovm.cocoatouch." (name class-name))
    class-name))

(defn init-class
  [class-name & args]
  (let [class-name (parse-name class-name)
        constructor (get-obj-constructor class-name args)]
    (if constructor
      (.newInstance constructor (into-array Object args))
      (println "Couldn't find class:" class-name))))

(defn static-method
  [class-name method-name & args]
  (let [class-name (parse-name class-name)
        method-name (name method-name)
        method (get-obj-method class-name method-name args)]
    (if method
      (.invoke method nil (into-array Object args))
      (println "Couldn't find class/method:" class-name method-name))))

(defn static-field
  [class-name field-name]
  (let [class-name (parse-name class-name)
        field-name (name field-name)
        field (.getField (Class/forName class-name) field-name)]
    (if field
      (.get field nil)
      (println "Couldn't find class/field:" class-name field-name))))

