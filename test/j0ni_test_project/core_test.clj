(ns j0ni-test-project.core-test
  (:require [clojure.test :refer :all]
            [j0ni-test-project.core :refer :all]))

(deftest foo-test
  (testing "the answer to life the universe and everything"
    (is (= (foo 10) 42))
    (is (= (foo 20) 42))))

(deftest env-var-test
  (testing "what do the environment variables look like"
    (println "FOO" (System/getenv "FOO"))))
