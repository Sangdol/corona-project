(ns ricotta.core
  (:use [ricotta.data-transformer :only [transform-data]]
        [ricotta.ui-generator :only [generate-ui]]))

(defn -main [& args]
  (if-not (empty? args)
    (let [in-path (nth args 0)
          out-path (nth args 1)]
      (do
        (transform-data in-path out-path)
        (generate-ui "" "")))
    (throw (Exception. "Paths to the input and output files, please."))))
