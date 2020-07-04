(ns ricotta.config-test
  (:require [clojure.test :refer :all])
  (:require [ricotta.config :as cf])
  (:require [ricotta.config :refer [config]]))

(deftest config-test
  (is (= (str cf/project-root "/data/owid-covid-data.csv")
         ((config "local") :input-csv-path))))

