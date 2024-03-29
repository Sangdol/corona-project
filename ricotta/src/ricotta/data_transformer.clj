(ns ricotta.data-transformer
  (:require [clojure.data.json :as json]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]))


(def europe "Europe")
(def columns-to-use '(:continent :location :date
                       :total_cases :new_cases
                       :total_cases_per_million :new_cases_per_million
                       :population))
(def double-columns [:total_cases_per_million :new_cases_per_million
                     :total_cases :new_cases :population])

(def weekly-trend-size 36)


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
  "Type cast using f."
  (map (fn [row]
           (update-keys row columns f))
       table))


(defn week-sum [cases-bucket]
  (->>
    (filter number? cases-bucket)  ;; some data could be an empty string
    (take-last 7)
    (reduce +)))


(defn is-valid
  [new-cases cases-bucket]
  (and
    (number? new-cases)
    (or
        (not (== new-cases 0))
        (< (week-sum cases-bucket) 20))))


(defn println-error-row [row]
  (println (str "Not valid data: "
                (:date row) " "
                (:location row) " "
                (:new_cases row) " ")))


(defn numerify [list]
  "a number can be empty string or minus (UK July 03: -29726)"
  (map (fn [n]
         (if (and (number? n) (>= n 0))
           n 0))
       list))


(defn remove-trailing-zeros [list]
  (->>
    (reverse list)
    (drop-while zero?)
    (reverse)))


(defn trend [new-cases-bucket prev]
  "This will remove trailing 0s based on the `prev` value.
   The logic is dependent on the select-latest-valid-data-of-country function"
  ;; what does this function do?
  ;;   the new_cases of prev will be only 0 when it's a valid 0 (see is-valid)
  ;;   otherwise `prev` won't be updated, in turn, the trailing 0s can be considered
  ;;   as not-valid values. WTF, who wrote this code?
  (->>
    (numerify new-cases-bucket)
    (#(if (or (not (number? (:new_cases prev)))
              (zero? (:new_cases prev)))
        %
        (remove-trailing-zeros %)))))


(defn weekly-trend [trend-data]
  "take weekly sum data"
  (->> trend-data
       reverse
       (partition 7)
       (map #(reduce + %))
       reverse
       (take-last weekly-trend-size)))


(defn select-latest-valid-data-of-country
  "Select latest valid data of one country data.
   A number in a row of the table can be an empty string."
  [table]
  ;; when does this loop stop?
  ;;   when `rows` is empty.
  (loop [prev (first table)
         new-cases-bucket []
         rows (rest table)]
    (if (empty? rows)
      (let [trend-data (trend new-cases-bucket prev)]
        (merge prev {:weekly-trend (weekly-trend trend-data)}))

      (let [latest (first rows)
            rest-rows (rest rows)
            new-cases (:new_cases latest)]
        (if (is-valid new-cases new-cases-bucket)
          (recur latest (conj new-cases-bucket new-cases) rest-rows)
          (do
            (println-error-row latest)
            (recur prev (conj new-cases-bucket new-cases) rest-rows)))))))


(defn select-latest-valid-data-per-country
  "Select latest valid data of mixed countries data"
  [table]
  (->>
    (group-by :location table)
    (vals)
    (map select-latest-valid-data-of-country)))


(defn create-data-with-metadata [countries]
  {:countries countries
   :weekly-trend-size weekly-trend-size
   :date      (->
                (map :date countries)
                sort
                last)})


(defn write-as-json
  "json instead of csv for number types"
  [map out-path]
  (spit out-path (json/write-str map))
  map)


(defn string->double [str]
  (if (not-empty str)
    (Double/parseDouble str)
    ""))


(defn generate-message
  [countries out-path]
  (str "Success (" (count countries) " countries): " out-path))


(defn transform-data [in-path out-path]
  (-> (read-csv-as-maps in-path)
      (select-columns columns-to-use)
      (select-europe)
      (cast-columns double-columns string->double)
      (select-latest-valid-data-per-country)
      (create-data-with-metadata)
      (write-as-json out-path)
      (generate-message out-path)
      (println)))
