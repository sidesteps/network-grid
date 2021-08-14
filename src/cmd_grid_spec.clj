(ns cmd-grid-spec
  (:require [clojure.spec.alpha :as spec]
            [clojure.spec.gen.alpha :as gen]))

(def state-map-spec (spec/coll-of (spec/coll-of boolean?) :min-count 1))
(def grid-fn? #{'deactivate 'activate 'toggle})

(defmacro make-cmd-spec [max-rows max-cols]
  `(spec/coll-of 
     (spec/and (spec/cat :fn grid-fn? 
                         :x1 (spec/int-in 0 ~max-cols)
                         :y1 (spec/int-in 0 ~max-rows)
                         :x2 (spec/int-in 0 ~max-cols)
                         :y2 (spec/int-in 0 ~max-rows))
               #(<= (% :x1) (% :x2))
               #(<= (% :y1) (% :y2)))
     :min-count 1))
