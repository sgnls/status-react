(ns status-im.utils.clocks
  (:refer-clojure :exclude [merge inc sort])
  (:require [clojure.data :as data]))

(defn diff
  "Given an old and a new vector clock returns only the keys/values that have
  changed"
  [old-clock new-clock]
  (second (data/diff old-clock new-clock)))

(def merge
  "Given two vector clocks c1 c2, merge them maintaining the maximum value
  between if same key"
  (partial merge-with max))

(defn inc [pk c1]
  (update c1 pk (fnil clojure.core/inc 0)))

(defn sort
  "Given a list of vector clocks returns them sorted by the following rules:
  If every value in A is >= then B then A > B
  Concurrent clocks will be sorted by trying to preserve casual ordering, breaking
  ties by public key of from"
  [pks c1 c2]
  (let [v1 (mapv #(get c1 % 0) pks)
        v2 (mapv #(get c2 % 0) pks)]
    (compare v1 v2)))

