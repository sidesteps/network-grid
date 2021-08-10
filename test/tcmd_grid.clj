(ns test.tcmd-grid
  (:use [clojure.test])
  (:require [cmd-grid :as cg]
            [cmd-grid-spec :as cgs] :reload-all)
  (:require [clojure.spec.alpha :as spec]))


(deftest invalid-if-command-index-out-of-range []
  (is (not (spec/valid? (cgs/make-cmd-spec 1 1) ['toggle 1 0 0 0 ]))))

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
(run-tests)

(comment (do 
  (def n-rows 700)
  (def n-cols 500)
  (def fn-spec (spec/fspec :args (cgs/make-grid-spec n-rows n-cols)
                           :ret cgs/state-map))
  (spec/exercise-fn cg/to-state-map fn-spec)
  nil))
