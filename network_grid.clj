(ns network-grid
  (:import java.util.BitSet))

(defn- make-state-area[nrows ncols] 
  (vec (repeatedly nrows #(new BitSet ncols))))

(defn- pr-bit-set [bs]
  (vec (read-string (str \# bs))))

(defn- pr-state-area[sa]
  (keep-indexed #(if-not (zero? (.cardinality %2))
                   [%1 (pr-bit-set %2)]) sa))

(defn to-sparse-grid [nrows ncols & commands]
  "Transforms command grid into sparse grid only showing indices of active cells."
  (let [sa (make-state-area nrows ncols)] 
    (doseq [[cmd x1 y1 x2 y2] commands]
      (doseq [row (subvec sa y1 y2)]
        (cmd row x1 x2)))
    (pr-state-area sa)))

(defn off [bs x1 x2]
  (.clear bs x1 x2))

(defn on [bs x1 x2]
  (.set bs x1 x2))

(defn toggle [bs x1 x2]
  (.flip bs x1 x2))


