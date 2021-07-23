(ns network-grid
  (:import java.util.BitSet)
  (:import java.lang.Integer))

(defn- make-bit-area[nrows ncols] 
  (-> (vec (repeatedly nrows #(new BitSet ncols)))
      (with-meta {:rows nrows :cols ncols})))

(defn- off [bs x1 x2]
  (.clear bs x1 x2)
  bs)

(defn- on [bs x1 x2]
  (.set bs x1 x2)
  bs)

(defn- toggle [bs x1 x2]
  (.flip bs x1 x2)
  bs)

(defn network-grid[nrows ncols commands]
  (let [a (make-bit-area nrows ncols)] 
    (doseq [[cmd x1 y1 x2 y2] commands]
      (doseq [row (range y1 y2)]
        (cmd (a row) x1 x2)))
    a))

(defn- pr-bit-row [len bit-row]
  (mapcat #(Integer/toUnsignedString % 2) (.toByteArray bit-row)))

  (comment (def row-0 (vec (repeat len \0)))
   (def     set-bits (load-string (str "#"(.toString bit-row))))
     (if (not-empty set-bits)
       ( (do (println "setyti " set-bits) (persistent! (apply assoc! (transient row-0) (interleave set-bits [\1])))))
       row-0))

(print set-bits)

(defn pr-bits [bit-area]
  (let [row-len (:cols (meta bit-area))]
     (for [bit-row bit-area]
      (pr-bit-row row-len bit-row))))

(pr-bits grid)

(def commands (load-file "grid.edn"))

(persistent! (assoc! (transient [1 2 3]) 2 10 1 22))
(def grid (network-grid 10 10 commands))
(print (map #(.toString %)grid))
(print set-bits)
(def bs (-> (new BitSet 100)
            (toggle 2 90)))

(print (.length bs))
(doseq [b (.toByteArray bs)]
       (println b))
(count (mapcat #(take 8 (reverse (Integer/toUnsignedString % 2))) (.toByteArray bs)))
(Integer/toString (.toByteArray (new BitSet 10)) 2)

