(ns ricotta.ui-generator)

(defn interpolate [template data])


(defn read-template [template-path])


(defn read-countries-data [data-path])


(defn write-html [html])


(defn generate-ui [template-path data-path]
  (->>
    (interpolate
      (read-template template-path)
      (read-countries-data data-path))
    (write-html)))
