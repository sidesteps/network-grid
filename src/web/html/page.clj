(ns web.html.page
  (:use hiccup.page))

(defn render[content]
  (html5 [:meta {:charset "utf-8"}]
         content))
