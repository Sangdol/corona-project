(ns ricotta.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io])
  (:use [ricotta.data-transformer :only [transform-data]]
        [ricotta.ui-generator :only [generate-ui]]))

(defn -main [& args]
  (if-not (empty? args)
    (do
      (transform-data (nth args 0))
      (generate-ui))
    (throw (Exception. "A path to the csv file, please."))))
