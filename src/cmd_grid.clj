(ns cmd-grid
  (:import java.util.BitSet)
  (:require [clojure.spec.alpha :as spec]
            [clojure.spec.gen.alpha :as gen]))

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

(defn- make-state-area[nrows ncols] 
  (vec (repeatedly nrows #(new BitSet ncols))))

(defn- pr-bit-set [^BitSet bs]
  (vec (read-string (str \# bs))))

(defn- pr-state-area[sa]
 (->> (keep-indexed #(if-not (zero? (.cardinality %2))
                         [%1 (pr-bit-set %2)]) sa)
      (into {})))

(defn- deactivate [bs x1 x2]
  (.clear bs x1 x2))

(defn- activate [bs x1 x2]
  (.set bs x1 x2))

(defn- toggle [bs x1 x2]
  (.flip bs x1 x2))

(defn to-sparse-grid 
  "Transforms command grid to sparse grid containing indices of only active cells."
  ([nrows ncols & cmds]
   (to-sparse-grid [nrows ncols cmds]))
  ([[nrows ncols commands]]
   (let [sa (make-state-area nrows ncols)] 
    (doseq [[cmd x1 y1 x2 y2] commands]
      (doseq [row (subvec sa y1 y2)]
        ((ns-resolve 'cmd-grid cmd) row x1 x2)))
    (pr-state-area sa))))

(defn read-grid[^String s]
  "Parses string s into cmd-grid representation"
  (let [parsed (read-string (str \[ s \])) 
        [nrows ncols & cmd-args] parsed
        grid-spec (make-grid-spec nrows ncols)
        grid [nrows ncols (partition 5 cmd-args)]]
    (if (spec/valid? grid-spec grid)
      grid
      [:invalid (spec/explain-str grid-spec grid)])))
