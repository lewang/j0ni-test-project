(ns j0ni-test-project.p2-test
  (:require [clojure.test :refer :all]
            [j0ni-test-project.core :refer :all]))

(defn do-a-bunch
  [n]
  (make-artifact-files (or (System/getenv "CIRCLE_ARTIFACTS")
                           (System/getenv "HOME"))
                       n))

(deftest punch-it
  (do-a-bunch 200))

(deftest little-one
  (do-a-bunch 10))
