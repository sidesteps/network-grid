(ns tmobile-network
  (:require [mobile-network :as mn]
            [cmd-grid-spec :as cgs] :reload)
  (:require [clojure.spec.alpha :as spec])
  (:use clojure.test))

(deftest ^:integration running-cmd-file-returns-network-state-map []
  (is (spec/valid? cgs/state-map (mn/run-cmd-file))))

(defn to-stream[s]
  (clojure.java.io/reader (char-array s)))

(deftest basic-cases []
  (are [rows cols cmd-str = state-map] (= (mn/run-cmds rows cols (to-stream cmd-str)) state-map)
       8 5
       "toggle 2 3 4 5
       deactivate 2 3 4 5
       activate 2 3 4 5
       toggle 2 3 5 8"     = [[false false false false false]
                              [false false false false false]
                              [false false false false false]
                              [false false false false true]
                              [false false false false true]
                              [false false true true true]
                              [false false true true true]
                              [false false true true true]]))

(time (do (mn/run-cmd-file) nil))
(run-tests)

