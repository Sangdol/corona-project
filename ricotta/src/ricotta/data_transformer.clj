(ns ricotta.data-transformer
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))


(def europe "Europe")
(def columns [])
(def double-columns [])
(def long-columns [])


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


(defn select-europe [table]
  (filter #(= (:continent %) europe) table))


(defn update-keys [map keys f]
  (reduce #(update % %2 f) map keys))


(defn cast-columns [table columns f]
  (map (fn [row] (update-keys row columns f))
       table))


(defn week-sum [cases-bucket]
  (reduce + (take-last 7 cases-bucket)))


(defn is-valid
  "Returns false if 0 is less than 5% of the sum of the last week data"
  [row cases-bucket]
  (or (not= (:new_cases row) 0)
      (< (/ (week-sum cases-bucket) 20) 1)))


(defn select-latest-valid-data-of-country
  "Select latest valid data of one country data"
  [table]
  (loop [prev (first table)
         cases-bucket [(:new_cases prev)]
         rows (rest table)]
    (if (empty? rows)
      prev
      (let [latest (first rows)
            new-cases-bucket (conj cases-bucket (:new_cases latest))
            rest-rows (rest rows)]
        (if (is-valid latest cases-bucket)
          (recur latest new-cases-bucket rest-rows)
          (recur prev new-cases-bucket rest-rows))))))


(defn select-latest-valid-data-per-country
  "Select latest valid data of mixed countries data"
  [table]
  (->>
    (group-by :location table)
    (vals)
    (map select-latest-valid-data-of-country)))


(defn write-as-csv [table out-path])


(defn transform-data [in-path out-path]
  (-> (read-csv-as-maps in-path)
      (select-columns columns)
      (select-europe)
      (cast-columns double-columns #(Double/parseDouble %))
      (cast-columns long-columns #(Long/parseLong %))
      (select-latest-valid-data-per-country)
      (write-as-csv out-path)))
