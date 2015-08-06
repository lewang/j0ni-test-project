(ns j0ni-test-project.core
  (:require [clojure.java.io :as io]
            [j0ni-test-project.markov :as markov])
  (:import java.util.UUID))

(defn make-artifact-files [base n]
  (doseq [haiku (markov/build-haikus "source-material" n)]
    (let [filename (format "%s/artifact-test-%s.txt" base (UUID/randomUUID))]
      (spit filename haiku))))
