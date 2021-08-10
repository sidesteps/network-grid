(ns cmd-grid-spec
  (:require [clojure.spec.alpha :as spec]
            [clojure.spec.gen.alpha :as gen]))

(def state-map (spec/def ::state-map (spec/* (spec/coll-of boolean?))))
(def grid-fn? #{'deactivate 'activate 'toggle})

(defmacro make-cmd-spec [max-rows max-cols]
  `(spec/and (spec/cat :fn grid-fn? 
                       :x1 (spec/int-in 0 ~max-cols)
                       :y1 (spec/int-in 0 ~max-rows)
                       :x2 (spec/int-in 0 ~max-cols)
                       :y2 (spec/int-in 0 ~max-rows))
             #(<= (% :x1) (% :x2))
             #(<= (% :y1) (% :y2))))

(defmacro make-grid-spec [nrows ncols]
  `(spec/cat :nrows (spec/with-gen pos-int? #(gen/return ~nrows))
             :ncols (spec/with-gen pos-int? #(gen/return ~ncols))
             :cmds (spec/? (spec/coll-of (make-cmd-spec ~nrows ~ncols)))))

