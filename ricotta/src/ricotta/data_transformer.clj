(ns ricotta.data-transformer)

(defn csv-data->maps [csv-data]
  "Read csv as maps lazily"
  (map zipmap
       (->> (first csv-data)
            (map keyword)
            repeat)
       (rest csv-data)))

(defn transform-data [path])
