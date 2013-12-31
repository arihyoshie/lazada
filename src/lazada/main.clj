(ns lazada.main
  (:import java.net.URL)
  (:require [net.cgrand.enlive-html :as html]
            [clojure.data.json :as json]
            [lazada.core :as core]
            [lazada.top :as top]
            [lazada.sub :as sub]
            [lazada.leaf :as leaf]
            [lazada.crawl :as crawl]))


(defn process

  ([] (process "http://lazada.com.ph"))
  ([url]
     (let [u (if url url "http://lazada.com.ph")

           tree-root (top/build-tree-root (core/get-page u))
           sub-raw (sub/build-sub-raw tree-root)
           sub-mid (sub/build-sub-mid sub-raw)
           sub-leaves (leaf/fillin-leaves sub-mid)
           crawled-leaves (crawl/crawl-leaves u sub-leaves)]

       crawled-leaves)))

(defn -main [ & args ]
  (process (first args)))
