(ns ricotta.ui-generator-test
  (:require [clojure.test :refer :all])
  (:require [ricotta.ui-generator :refer [interpolate]]))

(deftest interpolate-test
  (is (= "Hello, world!"
         (interpolate "Hello, {{data}}!" "world"))))

