(ns ricotta.core
  (:use [ricotta.data-transformer :only [transform-data]]
        [ricotta.ui-generator :only [generate-ui]]
        [ricotta.config :only [config]]))

(defn -main [& args]
  (if-not (empty? args)
    (let [env (nth args 0)
          config (config env)]
      (do
        (transform-data
          (:input-csv-path config)
          (:output-json-path config))
        (generate-ui
          (:js-template-path config)
          (:output-json-path config)
          (:js-output-path config))))
    (throw (Exception. "Env is needed - local or prod?"))))
