(ns lazada.top
  (:require [net.cgrand.enlive-html :as html]))

(defn get-top-siblings [page]
  (->> (html/select page [(html/left :a.catArrow)])
       (remove #(= % "\n"))))

(defn get-top-category-names [nodes]
  (map html/text
       (html/select nodes [:a.catArrow :> :span.navSubTxt])))

(defn get-top-categories [page]
  (html/select page [:li.multiMenu]))


(defn build-tree-root [top-page]
  (let [top-categories (get-top-categories top-page)

        name-fn #(get-top-category-names %)
        children-fn #(get-top-siblings %)]

    (map (fn [inp]
           {:name (first (name-fn inp))
            :children (children-fn inp)})
         top-categories)))
