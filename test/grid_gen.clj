(ns test.grid-gen
  (:require [cmd-grid-spec :as cgs] :reload)
  (:require [clojure.spec.alpha :as spec]
            [clojure.spec.gen.alpha :as gen]))

(def max-rows 700)
(def max-cols 700)

(defn- gen-int[gmin gmax]
  (gen/generate (spec/gen (spec/int-in gmin gmax))))

(defn make-cmd-grid[]
  (let [rows (gen-int 1 max-rows)
        cols (gen-int 1 max-cols)
        cspec (cgs/make-cmd-spec rows cols)]
    [rows cols (gen/generate (spec/gen cspec))]))

