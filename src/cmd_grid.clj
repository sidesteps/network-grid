(ns cmd-grid
  "Space optimized, in-place updated command grid."
  (:import java.util.BitSet))

(defn- make-state-area [nrows ncols]
  (-> (vec (repeatedly nrows #(new BitSet ncols)))
      (with-meta {:rows nrows :cols ncols})))

(defn- to-states [n bs]
  (map #(.get bs %) (range n)))

(defn- get-state-map [sa]
  (let [{n :cols} (meta sa)]
    (map (partial to-states n) sa)))

(defn- update-row [^BitSet row cmd x1 x2]
  (case cmd
    activate   (.set row x1 x2)
    deactivate (.clear row x1 x2)
    toggle     (.flip row x1 x2)
    (throw (new UnsupportedOperationException
                (str "Don't recognize command: " cmd)))))

(defn to-state-map [nrows ncols commands]
  "Transforms command grid to state map"
  (let [sa (make-state-area nrows ncols)]
    (doseq [[cmd x1 y1 x2 y2] commands]
      (doseq [row (subvec sa y1 y2)]
        (update-row row cmd x1 x2)))
    (get-state-map sa)))
