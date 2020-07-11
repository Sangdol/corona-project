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


(deftest select-europe-test
  (is (= [{:continent "Europe" :a 1}]
         (select-europe [{:continent "Europe" :a 1}
                         {:continent "Asia" :a 2}]))))


(deftest update-keys-test
  (is (= {:a 1 :b 2 :c "3"}
         (update-keys {:a "1" :b "2" :c "3"} '(:a :b) #(Long/parseLong %)))))


(deftest cast-columns-test
  (is (= [{:a 1 :b "2"} {:a 3 :b "4"}]
         (cast-columns [{:a "1" :b "2"} {:a "3" :b "4"}]
                       '(:a)
                       #(Long/parseLong %)))))


(deftest week-sum-test
  (is (= 3 (week-sum [1 1 1 0 0])))
  (is (= 4 (week-sum [100 1 1 1 1 0 0 0]))))


(deftest is-valid-test
  (is (is-valid 1 [0 0 0]))
  (is (is-valid 100 [0 0 0]))
  (is (is-valid 0 [10 0 0]))
  (is (not (is-valid 0 [100 0 0])))
  (is (not (is-valid 0 [10 11 12 0 0 20 30]))))


(deftest select-latest-valid-data-of-country-test

  (is (= {:date "2020-06-26" :location "a" :new_cases 30}
         (select-latest-valid-data-of-country
            [{:date "2020-06-20" :location "a" :new_cases 10}
             {:date "2020-06-21" :location "a" :new_cases 11}
             {:date "2020-06-22" :location "a" :new_cases 12}
             {:date "2020-06-23" :location "a" :new_cases 0}
             {:date "2020-06-24" :location "a" :new_cases 0}
             {:date "2020-06-25" :location "a" :new_cases 20}
             {:date "2020-06-26" :location "a" :new_cases 30}
             {:date "2020-06-27" :location "a" :new_cases 0}])))

  (is (= {:date "2020-06-27" :location "a" :new_cases 0}
         (select-latest-valid-data-of-country
           [{:date "2020-06-20" :location "a" :new_cases 1}
            {:date "2020-06-21" :location "a" :new_cases 1}
            {:date "2020-06-22" :location "a" :new_cases 2}
            {:date "2020-06-23" :location "a" :new_cases 0}
            {:date "2020-06-24" :location "a" :new_cases 0}
            {:date "2020-06-25" :location "a" :new_cases 2}
            {:date "2020-06-26" :location "a" :new_cases 3}
            {:date "2020-06-27" :location "a" :new_cases 0}])))

  (is (= {:date "2020-06-27" :location "a" :new_cases 0}
         (select-latest-valid-data-of-country
           [{:date "2020-06-20" :location "a" :new_cases 1}
            {:date "2020-06-21" :location "a" :new_cases 1}
            {:date "2020-06-22" :location "a" :new_cases 2}
            {:date "2020-06-23" :location "a" :new_cases 0}
            {:date "2020-06-24" :location "a" :new_cases 0}
            {:date "2020-06-25" :location "a" :new_cases 2}
            {:date "2020-06-26" :location "a" :new_cases ""}
            {:date "2020-06-27" :location "a" :new_cases 0}])))

  (is (= {:date "2020-06-25" :location "a" :new_cases 200}
         (select-latest-valid-data-of-country
           [
            {:date "2020-06-14" :location "a" :new_cases 0}
            {:date "2020-06-15" :location "a" :new_cases 0}
            {:date "2020-06-16" :location "a" :new_cases 2}
            {:date "2020-06-17" :location "a" :new_cases 0}
            {:date "2020-06-18" :location "a" :new_cases 0}
            {:date "2020-06-19" :location "a" :new_cases 2}
            {:date "2020-06-20" :location "a" :new_cases 1}
            {:date "2020-06-21" :location "a" :new_cases 1}
            {:date "2020-06-22" :location "a" :new_cases 2}
            {:date "2020-06-23" :location "a" :new_cases 0}
            {:date "2020-06-24" :location "a" :new_cases 0}
            {:date "2020-06-25" :location "a" :new_cases 200}
            {:date "2020-06-26" :location "a" :new_cases ""}
            {:date "2020-06-27" :location "a" :new_cases 0.0}]))))


(deftest select-latest-data-per-country-test
  (is (= [{:date "2020-06-27" :location "b" :new_cases 40}
          {:date "2020-06-26" :location "a" :new_cases 30}]
         (select-latest-valid-data-per-country
           [{:date "2020-06-20" :location "b" :new_cases 10}
            {:date "2020-06-21" :location "b" :new_cases 11}
            {:date "2020-06-22" :location "b" :new_cases 12}
            {:date "2020-06-23" :location "b" :new_cases 0}
            {:date "2020-06-24" :location "b" :new_cases 0}
            {:date "2020-06-25" :location "b" :new_cases 20}
            {:date "2020-06-26" :location "b" :new_cases 30}
            {:date "2020-06-27" :location "b" :new_cases 40}
            {:date "2020-06-20" :location "a" :new_cases 10}
            {:date "2020-06-21" :location "a" :new_cases 11}
            {:date "2020-06-22" :location "a" :new_cases 12}
            {:date "2020-06-23" :location "a" :new_cases 0}
            {:date "2020-06-24" :location "a" :new_cases 0}
            {:date "2020-06-25" :location "a" :new_cases 20}
            {:date "2020-06-26" :location "a" :new_cases 30}
            {:date "2020-06-27" :location "a" :new_cases 0}]))))


(deftest string->double-test
  (is (= 1.0 (string->double "1")))
  (is (= "" (string->double ""))))


(deftest generate-message-test
  (is (= "Success (3 countries): /file/path"
         (generate-message [{} {} {}] "/file/path"))))

(deftest create-data-with-timestamp-test
  (is (= {:date "2020-07-06"
          :countries
                [{:date "2020-07-05"} {:date "2020-07-06"}]}
         (create-data-with-timestamp
           [{:date "2020-07-05"} {:date "2020-07-06"}]))))

