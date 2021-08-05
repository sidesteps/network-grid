(ns test.tcmd-grid
  (:use [clojure.test])
  (:require [cmd-grid :as cg] :reload-all)
  (:require [clojure.spec.alpha :as spec]
            [clojure.spec.gen.alpha :as gen]
            [clojure.test.check :as tc]
            [clojure.test.check.properties :as prop]))

(run-tests)

(deftest invalid-if-command-index-out-of-range []
  (is (not (spec/valid? (cg/make-cmd-spec 1 1) ['toggle 1 0 0 0 ]))))

(deftest throws-if-invoked-with-index-out-of-range []
  (is (thrown? IndexOutOfBoundsException 
               (cg/to-sparse-grid 1 1 ['on 0 0 0 2]))))

(deftest basic-cases []
  (are [cmd-grid = sparse-grid] (= (apply cg/to-sparse-grid cmd-grid) sparse-grid)
    [0 0]     = {}

    [700 700] = {} 
    
    [1 1 ['activate 0 0 1 1]]       = {0 [0]}
    
    [1 1 ['deactivate 0 0 1 1]]     = {}

    [1 1 ['activate 0 0 1 1]      
         ['toggle 0 0 1 1]]         = {}

    [1 1 ['activate 0 0 1 1]      
         ['deactivate 0 0 1 1]]     = {}
    
    [2 2 ['activate 0 0 2 2]]       = {0 [0 1] 1 [0 1]}

    [2 2 ['activate 0 0 2 2]      
         ['deactivate 0 0 2 2]]     = {}
    
    [2 2 ['deactivate 0 0 2 2]     
         ['activate 0 0 2 2]]       = {0 [0 1] 1 [0 1]}

    [2 2 ['activate 0 0 2 2]      
         ['deactivate 0 0 2 2]     
         ['toggle 0 0 2 2]]         = {0 [0 1] 1 [0 1]}))


(apply cg/to-sparse-grid (cg/read-grid (slurp "test/small_grid.txt")))
(- (* 700 700) (apply + (map count (vals (apply cg/to-sparse-grid (cg/read-grid (slurp "test/large_grid.txt")))))))
(spec/explain (cg/make-cmd-spec 2 2)['toggle 0 0 2 1])

(do 
  (def n-rows 700)
  (def n-cols 500)
  (tc/quick-check 100 (prop/for-all [cmds (spec/gen (cg/make-grid-spec n-rows n-cols))] 
                                    (map? (apply cg/to-sparse-grid n-rows n-cols cmds)))))
