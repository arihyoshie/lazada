(ns lazada.main
  (:import java.net.URL)
  (:require [net.cgrand.enlive-html :as html]
            [clojure.data.json :as json]
            [clojure.walk :as walk]
            [lazada.core :as core]
            [lazada.top :as top]
            [lazada.sub :as sub]
            [lazada.leaf :as leaf]))


(defn crawl-load [url node]
  (let [uri (:href node)
        url-full (str url uri)]
    (core/get-page url-full)))

(defn crawl-collect [url node]

  (let [crawled-page (crawl-load url node)
        itm-links (html/select crawled-page
                               [:#productsCatalog :li :ul.subGroupProducts :li.unit :.itm :a.itm-link])
        itm-names (map #(-> % :attrs :title)
                       itm-links)
        itm-first-five (take 5 itm-names)]

    (assoc node :top5 itm-first-five)))

(defn crawl-leaves [url tree]
  (walk/postwalk (fn [inp]
                   (if (and (map? inp)
                            (:leaf inp))
                     (crawl-collect url inp)
                     inp))
                 tree))

(defn -main [ & args ]

  (let [url "http://lazada.com.ph"

        tree-root (top/build-tree-root (core/get-page url))
        sub-raw (sub/build-sub-raw tree-root)
        sub-mid (sub/build-sub-mid sub-raw)
        sub-leaves (leaf/fillin-leaves sub-mid)
        crawled-leaves (crawl-leaves url sub-leaves)]

    ))
