(ns test.tcmd-grid
  (:use [clojure.test])
  (:require [cmd-grid :as cg] :reload-all)
  (:require [clojure.spec.alpha :as spec]
            [clojure.spec.gen.alpha :as gen]))

(run-tests)

(deftest invalid-if-command-index-out-of-range []
  (is (not (spec/valid? (cg/make-cmd-spec 1 1) ['toggle 1 0 0 0 ]))))

(deftest throws-if-invoked-with-index-out-of-range []
  (is (thrown? IndexOutOfBoundsException 
               (cg/to-sparse-grid 1 1 ['on 0 0 0 2]))))

(deftest basic-cases []
  (are [cmd-grid = sparse-grid] (= (apply cg/to-sparse-grid cmd-grid) sparse-grid)
    [0 0]                  = {}
    
    [700 700]              = {} 
    
    [1 1 ['on 0 0 1 1]]     = {0 [0]}
    
    [1 1 ['off 0 0 1 1]]    = {}

    [1 1 ['on 0 0 1 1]      
         ['toggle 0 0 1 1]] = {}

    [1 1 ['on 0 0 1 1]      
         ['off 0 0 1 1]]    = {}
    
    [2 2 ['on 0 0 2 2]]     = {0 [0 1] 1 [0 1]}
    [2 2 ['on 0 0 2 2]      
         ['off 0 0 2 2]]    = {}
    
    [2 2 ['off 0 0 2 2]     
         ['on 0 0 2 2]]     = {0 [0 1] 1 [0 1]}

    [2 2 ['on 0 0 2 2]      
         ['off 0 0 2 2]     
         ['toggle 0 0 2 2]] = {0 [0 1] 1 [0 1]}))

(comment 
  (do 
    (def grid-spec (cg/make-grid-spec 40 50))
    (def grid (cg/read-grid (slurp "test/small_grid.txt")))
    (def large-grid (cg/read-grid (slurp "test/large_grid.txt")))
  (spec/valid? grid-spec grid)
  (spec/conform grid-spec grid)
  (spec/explain grid-spec [2 2 [['toggle 0 0 50 1] ['toggle 0 0 1 1]]])
  (gen/generate (spec/gen grid-spec)))
  )
