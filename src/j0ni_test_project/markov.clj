(ns j0ni-test-project.markov
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [com.lemonodor.pronouncing :as pronouncing]))

(defn load-text [fname]
  (-> fname io/resource slurp))

(defn build-table [text]
  (let [links (->> (string/split text #"[ \"\r\n.,;:/?!]+")
                   (partition 3 1)
                   (map (fn [[a b c]] [[a b] c])))]
    (reduce #(update-in %1 %2 (fnil inc 0)) {} links)))

(defn load-corpus [fname]
  (-> fname load-text build-table))

(defn select-suffix
  "Taken from Stackoverflow:
    http://stackoverflow.com/a/14467227"
  [suffix-map]
  (let [weights (reductions + (vals suffix-map))
        r (rand-int (last weights))]
    (nth (keys suffix-map) (count (take-while #(<= % r) weights)))))

(defn build-chain [table n]
  (loop [prefix (-> table keys rand-nth)
         acc prefix]
    (if-let [suffix-map (get table prefix)]
      (let [new-acc (->> suffix-map
                         (into (sorted-map))
                         (select-suffix)
                         (conj acc))
            new-prefix (take-last 2 new-acc)]
        (if (>= (count new-acc) n)
          new-acc
          (recur new-prefix new-acc)))
      acc)))

(defn write-junk [fname n]
  (let [corpus (load-corpus fname)]
    (string/join " " (build-chain corpus n))))

(defn syllable-count
  [strs]
  (->> strs
       (map pronouncing/syllable-count-for-word)
       (map first)
       (filter identity)
       (apply +)))

(defn next-word
  [corpus prefix]
  (if-let [suffix-map (get corpus prefix)]
    (->> suffix-map
         (into (sorted-map))
         (select-suffix))
    (throw (ex-info "Couldn't find a match" {:prefix prefix}))))

(defn make-line
  [corpus n]
  (loop [prefix (-> corpus keys rand-nth)
         acc prefix]
    (let [acc-syllable-count (syllable-count acc)]
      (cond
        ;; we're done, return the line
        (= acc-syllable-count n)
        acc

        ;; randomly selected prefix was too long
        (> (syllable-count prefix) n)
        (throw (ex-info "prefix was too long" {:prefix prefix}))

        ;; overshot, drop the last word and try again
        (> acc-syllable-count n)
        (recur (->> acc (take-last 3) butlast) (butlast acc))

        ;; find the next word
        :else (let [new-acc (conj acc (next-word corpus prefix))]
                (recur (take-last 2 new-acc) new-acc))))))

(defn build-haiku
  [fname]
  (let [corpus (load-corpus fname)
        words [(make-line corpus 7)
               (make-line corpus 5)
               (make-line corpus 7)]]
    (->> words
         (map (partial string/join " "))
         (string/join "\n"))))

