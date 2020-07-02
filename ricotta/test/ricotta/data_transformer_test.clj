(ns ricotta.data-transformer-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [ricotta.data-transformer :refer :all]))

(deftest read-csv-as-maps-test
  (is (= [{:a "1" :b "2"} {:a "3" :b "4"}]
         (read-csv-as-maps (io/resource "resources/test-ab.csv")))))


(deftest select-columns-test
  (is (= [{:a "1"} {:a "3"}]
         (select-columns [{:a "1" :b "2"} {:a "3" :b "4"}] '(:a)))))

(deftest update-keys-test
  (is (= {:a 1 :b 2 :c "3"}
         (update-keys {:a "1" :b "2" :c "3"} '(:a :b) #(Long/parseLong %)))))


;(deftest convert-number-data-types-test
;  (is (= [{:a 1 :b "2"} {:a 3 :b "4"}]
;         (convert-number-data-types [{:a "1" :b "2"} {:a "3" :b "4"}] '(:a)))))

