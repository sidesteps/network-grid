(ns test.tcmd-grid
  (:use [clojure.test])
  (:require [cmd-grid :as cg]
            [cmd-grid-spec :as cgs]
            [test.grid-gen :as gg] :reload-all)
  (:require [clojure.spec.alpha :as spec]))


(deftest throws-if-invoked-with-index-out-of-range []
  (is (thrown? IndexOutOfBoundsException 
               (cg/to-state-map 1 1 [['on 0 0 0 2]]))))

(deftest basic-cases []
  (are [rows cols cmds = state-map] (= (cg/to-state-map rows cols cmds) state-map)
    1 1 []     = [[false]]

    700 700 [] = (repeat 700 (repeat 700 false))
    
    1 1 [['activate 0 0 1 1]]   = [[true]]
    
    1 1 [['deactivate 0 0 1 1]] = [[false]]

    1 1 [['activate 0 0 1 1]    
         ['toggle 0 0 1 1]]     = [[false]]

    1 1 [['activate 0 0 1 1]    
         ['deactivate 0 0 1 1]] = [[false]]
    
    2 2 [['activate 0 0 2 2]]   = [[true true]
                                  [true true]]

    2 2 [['activate 0 0 2 2]    
         ['deactivate 0 0 2 2]] = [[false false]
                                  [false false]]
    
    2 2 [['deactivate 0 0 2 2]  
         ['activate 0 0 2 2]]   = [[true true]
                                  [true true]]

    2 2 [['activate 0 0 2 2]    
         ['deactivate 0 0 2 2]  
         ['toggle 0 0 2 2]]     = [[true true]
                                  [true true]]
    2 3 [['activate 0 0 1 1]     
         ['activate 2 1 3 2]]   = [[true false false]
                                  [false false true]]))

(deftest test-generated-grids
  (letfn [(gen-state-map [] (apply cg/to-state-map (gg/make-cmd-grid)))
          (state-map-valid? [sm] (spec/valid? cgs/state-map-spec sm))]
  (is (every? state-map-valid? (repeatedly 10 gen-state-map)))))

(comment
  (run-tests)

  (def large-grid (read-string (slurp "test/large_grid.edn")))
  (time (do (apply cg/to-state-map large-grid) nil))
)
