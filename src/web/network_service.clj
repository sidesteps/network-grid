(ns web.netwok-service
  "Serves networks' state map.
  State map data is very homgenous and wery well compressable/zippable. Will not stress network if responses gzipped."
  (:require [aleph.http :as http]
            [mobile-network :as mn]
            [web.html
             [page :as html-page]
             [state-map :as html-smap]]))

(defn get-network [_]
  {:status 200
   :headers {"content-type" "text/html"}
   :body (->> (mn/run-cmd-file)
              html-smap/render
              html-page/render)})

(defn run [{p :port :or {p 8080}}]
  (let [serv (http/start-server get-network {:port p :compression-level 9})]
    (println (str "Serving network state at: http://localhost:" p))
    serv))

(comment
  (def serv (run nil))
  (.close serv)
  )
