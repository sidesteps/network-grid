(ns web.test-mobile-network
  (:require [web.mobile-network :as wmn]
            [test.grid-gen :as gg]
            [cmd-grid :as cg] :reload))

(defn get-network[_]
  (let [grid (gg/make-cmd-grid)] 
    { 
     :status 200
     :headers {"content-type" "text/plain"}
     :body {:commands grid
            :network (wmn/serialize-state-map 
                       (apply cg/to-state-map grid))}
     }))

