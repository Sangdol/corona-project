(ns ricotta.config)

(def home (System/getProperty "user.home"))
(def project-root (str home "/projects/corona-project"))

(def common
  {:csv-owid-data-path   (str project-root "/data/owid-covid-data.csv")
   :json-daily-data-path (str project-root "/data/europe-data.json")
   :js-template-path     (str project-root "/ricotta/resources/web/template/ricotta-milk.js")
   :js-prod-path         (str project-root "/ricotta/resources/web/dist/ricotta-cheese.js")})

(def local {})

(def prod {})

(defn config [env]
  ({:local (merge common local)
    :prod  (merge common prod)}
   (keyword env)))




