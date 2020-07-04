(ns ricotta.config)

(def home (System/getProperty "user.home"))
(def project-root (str home "/projects/corona-project"))

(def common
  {:input-csv-path   (str project-root "/data/owid-covid-data.csv")
   :output-json-path (str project-root "/data/europe-data.csv")
   :js-template-path (str project-root "/data/ricotta/resources/src/ricotta-src.js")
   :js-output-path   (str project-root "/data/ricotta/resources/src/ricotta.js")})

(def local {})

(def prod {})

(defn config [env]
  ({:local (merge common local)
    :prod (merge common prod)}
   (keyword env)))




