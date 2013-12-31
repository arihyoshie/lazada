(ns lazada.main
  (:import java.net.URL)
  (:require [net.cgrand.enlive-html :as html]
            [clojure.data.json :as json]
            [lazada.top :as top]
            [lazada.sub :as sub]
            [lazada.leaf :as leaf]))


(defn get-page [purl]
  (println "Getting page: " purl)
  (-> purl URL. html/html-resource))


(defn -main [ & args ]

  (let [tree-root (top/build-tree-root (get-page "http://lazada.com.ph"))
        sub-raw (sub/build-sub-raw tree-root)
        sub-mid (sub/build-sub-mid sub-raw)
        sub-leaves (leaf/fillin-leaves sub-mid)

        ;; ... foreach target-category, crawl and get each sub-page

        ;; ... get sub-links and names **structure**

        ;; ... get first 5 names **structure**

        ]

    ))
