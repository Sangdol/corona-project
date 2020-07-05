(ns ricotta.ui-generator
  (:require [clojure.string :as str]))

(defn interpolate [template data]
  (str/replace template "{{data}}" data))

(defn generate-message [output-path]
  (str "Success: " output-path))

(defn generate-ui [template-path data-path output-path]
  (->>
    (interpolate
      (slurp template-path)
      (slurp data-path))
    (spit output-path))
  (->>
    (generate-message output-path)
    (println)))

