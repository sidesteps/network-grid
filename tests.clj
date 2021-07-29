(ns tests
  (:use network-grid
        clojure.test))

(do
  (def cmd-grid (load-file "cmd-grid.edn"))
  (def sparse-grid (apply to-sparse-grid cmd-grid))
  sparse-grid)

(deftest throws-if-command-index-out-of-range []
  (is (thrown? IndexOutOfBoundsException 
               (to-sparse-grid 1 1 [on 0 0 0 2]))))

(deftest basic-cases []
  (are [cmd-grid = sparse-grid] (= sparse-grid (apply to-sparse-grid cmd-grid))
    [0 0] = []

    [700 700] = [] 

    [1 1 [on 0 0 1 1]] = [[0 [0]]]

    [1 1 [off 0 0 1 1]] = []

    [1 1 [on 0 0 1 1]
         [toggle 0 0 1 1]] = []

    [1 1 [on 0 0 1 1]
         [off 0 0 1 1]] = []

    [2 2 [on 0 0 2 2]] = [[0 [0 1]] [1 [0 1]]]

    [2 2 [on 0 0 2 2]
         [off 0 0 2 2]] = []

    [2 2 [off 0 0 2 2]
         [on 0 0 2 2]] = [[0 [0 1]] [1 [0 1]]]

    [2 2 [on 0 0 2 2]
         [off 0 0 2 2]
         [toggle 0 0 2 2]] = [[0 [0 1]] [1 [0 1]]]))

(run-tests)
