(ns test.tnetwork-service
  "Generates command grids and serves their state maps.
  State map data is very homgenous and wery well compressable/zippable. Will not stress network if responses gzipped."
  (:require [clojure.pprint :as pp]
            [aleph.http :as http])
  (:require [cmd-grid :as cg]
            [test.grid-gen :as gg]
            [web.html
             [page :as html-page]
             [state-map :as html-smap]] :reload))

(defn test-network-view[{:keys [cmd-grid state-map]}]
  [:div 
   "Reload page to generate next grid."
   [:pre (with-out-str (pp/pprint cmd-grid))]
   (html-smap/render state-map)])
                          
(defn get-network[_]
  (let [grid (gg/make-cmd-grid)] 
    { 
     :status 200
     :headers {"content-type" "text/html"}
     :body (->> {:cmd-grid grid
                 :state-map (apply cg/to-state-map grid)}
                test-network-view
                html-page/render)
     }))

(defn run [{p :port :or {p 9090}}]
  (let [serv (http/start-server get-network {:port p :compression-level 9})] 
    (println (str "Serving generated test networks at: http://localhost:" p))
    serv))

(comment
  (def serv (run nil))
  (.close serv)
  )


