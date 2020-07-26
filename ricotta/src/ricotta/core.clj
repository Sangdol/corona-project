(ns ricotta.core
  (:use [ricotta.data-transformer :only [transform-data]]
        [ricotta.ui-generator :only [generate-ui]]
        [ricotta.config :only [config]]))

(def ops {"data" (fn [env-config]
                   (transform-data
                     (:csv-owid-data-path env-config)
                     (:json-daily-data-path env-config)))
          "ui" (fn [env-config]
                  (generate-ui env-config))})


(defn run [op env-config]
  ((ops op) env-config))


(defn run-all [env-config]
  (doseq [op (keys ops)]
    (run op env-config)))


(defn -main [& args]
  (if (= (count args) 2)
    (let [op (nth args 0)
          env (nth args 1)
          env-config (config env)]
      (cond
        (= op "all") (run-all env-config)
        :else (run op env-config)))
    (throw (Exception. "Env is needed - local or prod?"))))
