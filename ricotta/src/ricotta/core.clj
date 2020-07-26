(ns ricotta.core
  (:use [ricotta.data-transformer :only [transform-data]]
        [ricotta.ui-generator :only [generate-ui]]
        [ricotta.config :only [config]]))

(defn -main [& args]
  (if-not (empty? args)
    (let [env (nth args 0)
          env-config (config env)]
      (transform-data
        (:csv-owid-data-path env-config)
        (:json-daily-data-path env-config))
      (generate-ui env-config))
    (throw (Exception. "Env is needed - local or prod?"))))
