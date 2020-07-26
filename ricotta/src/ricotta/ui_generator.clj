(ns ricotta.ui-generator
  (:require [clojure.string :as str])
  (:use [ricotta.config :only [config]]))

(defn interpolate [template data]
  (str/replace template "{{data}}" data))

(defn generate-message [output-path]
  (str "Success: " output-path))

(defn generate-ui [env-config]
  (let [js-template-path (:js-template-path env-config)
        json-input-path (:json-daily-data-path env-config)
        js-output-path (:js-prod-path env-config)]
    (->>
      (interpolate
        (slurp js-template-path)
        (slurp json-input-path))
      (spit js-output-path))
    (->>
      (generate-message js-output-path)
      (println))))

