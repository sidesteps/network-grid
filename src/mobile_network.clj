(ns mobile-network
  (:import java.io.BufferedReader)
  (:require [cmd-grid :as cg]))

(def COMMAND-FILE "network_commands.txt")
(def GRID-ROWS 700)
(def GRID-COLS 700)

(defn- read-cmd[s]
  "Parses network command string"
  (read-string (str \[ s \])))

(defn run-cmds [nrows ncols ^BufferedReader cmd-stream] 
  "Reduces network command stream into network state map"
  (let [cmds (map read-cmd (line-seq cmd-stream))]
    (cg/to-state-map nrows ncols cmds)))

(defn run-cmd-file[]
  "Reduces network command file into network state map. Can handle large files without straining RAM."
  (with-open [cmd-stream (clojure.java.io/reader COMMAND-FILE)]
    (run-cmds GRID-ROWS GRID-COLS cmd-stream)))

