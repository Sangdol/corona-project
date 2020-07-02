(ns ricotta.data-transformer
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))


(def continent "Europe")
(def columns [])


(defn read-csv-as-maps [in-path]
  (with-open [reader (io/reader in-path)]
    (let [data (csv/read-csv reader)]
      (mapv zipmap
            (->> (first data)
                 (map keyword)
                 repeat)
            (rest data)))))


(defn select-columns [table columns]
  (map select-keys table (repeat columns)))


(defn update-keys [map keys f]
  (reduce (fn [row key] (update row key f))
          map keys))


(defn convert-number-data-types [table columns]
  (map (fn [row] update-keys row columns) table))


(defn select-latest-data-per-country [table])


(defn write-as-csv [table out-path])


(defn transform-data [in-path out-path]
  (-> (read-csv-as-maps in-path)
      (select-columns columns)
      (convert-number-data-types columns)
      (select-latest-data-per-country)
      (write-as-csv out-path)))
