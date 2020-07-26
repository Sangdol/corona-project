(ns ricotta.config)

(def home (System/getProperty "user.home"))
(def project-root (str home "/projects/corona-project"))
(def data (str project-root "/data"))
(def web (str project-root "/ricotta/resources/web"))

(def common
  {:csv-owid-data-path           (str data "/owid-covid-data.csv")
   ;; js
   :json-daily-data-path         (str data "/europe-data.json")
   :js-template-path             (str web "/template/ricotta-milk.js")
   :js-output-path               (str web "/dist/ricotta-cheese.js")
   ;; html
   :html-template-path           (str web "/template/template.html")
   :html-index-body-content-path (str web "/contents/index-body.html")
   :html-index-prod-path         (str web "/index.html")})


(def local {})

(def prod {})

(defn config [env]
  ({:local (merge common local)
    :prod  (merge common prod)}
   (keyword env)))




