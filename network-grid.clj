(ns network-grid
  (:import java.util.BitSet))

(defn- make-state-area[nrows ncols] 
  (vec (repeatedly nrows #(new BitSet ncols))))

(defn- pr-bit-set [bs]
  (vec (load-string (str \# bs))))

(defn- pr-state-area[sa]
  (filter some? (map-indexed #(if (= 0 (.cardinality %2))
                       nil (vector %1 (pr-bit-set %2))) sa)))

(defn network-grid [nrows ncols commands]
  (let [sa (make-state-area nrows ncols)] 
    (doseq [[cmd x1 y1 x2 y2] commands]
      (doseq [i (range y1 y2)]
        (cmd (sa i) x1 x2)))
    (pr-state-area sa)))

(defn- off [bs x1 x2]
  (.clear bs x1 x2))

(defn- on [bs x1 x2]
  (.set bs x1 x2))

(defn- toggle [bs x1 x2]
  (.flip bs x1 x2))

(do
  (def nrows 20)
  (def ncols 40)
  (def commands (load-file "commands.edn"))
  (def grid (network-grid nrows ncols commands))
  grid)
