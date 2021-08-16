(ns web.html.state-map)

(def ON  "&#x2705")
(def OFF "&#x274C")

(defn render[states]
  [:span#state-map 
   (for [srow states]
     [:div (for [s srow]
             (if s ON OFF))])])
