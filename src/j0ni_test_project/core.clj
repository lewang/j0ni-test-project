(ns j0ni-test-project.core
  (:require [clojure.java.io :as io]
            [j0ni-test-project.markov :as markov])
  (:import java.util.UUID))

(defn make-artifact-files [base n]
  (when (> n 0)
    (try
      (let [filename (-> (format "%s/artifact-test-%s.txt" base (UUID/randomUUID)))
            haiku (markov/build-haiku "source-material")]
        (spit filename haiku))
      (catch clojure.lang.ExceptionInfo e
        (println "Error making haiku" (.getMessage e)))) 
    (recur base (- n 1))))
