(ns web.netwok-service
  (:require [aleph.http :as http]
            [mobile-network :as mn]))

(defn- to-state-string [states]
  (apply str (map #(if % \1 \0) states)))

(defn serialize-state-map [sm]
  (mapv to-state-string sm))

(defn get-network [req]
  {:status 200
   :headers {"content-type" "text/plain"}
   :body (serialize-state-map (mn/run-cmd-file))})

(defn run[& opts]
  (http/start-server get-network {:port 8080 :compression-level 9})
  (println "started serving on port 8080"))

